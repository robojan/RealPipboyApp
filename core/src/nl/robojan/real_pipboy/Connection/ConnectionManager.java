package nl.robojan.real_pipboy.Connection;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

import nl.robojan.real_pipboy.Connection.Packets.*;

/**
 * Created by s120330 on 15-7-2015.
 */
public class ConnectionManager {

    private static ConnectionManager ourInstance = new ConnectionManager();

    public static ConnectionManager getInstance() {
        return ourInstance;
    }

    public enum TCPStatus {
        Disconnected,
        Connecting,
        ConnectionFailed,
        Connected
    }

    private Thread mThread;

    private String mTCPHostname;
    private int mTCPPort;

    private AtomicBoolean mConnected = new AtomicBoolean(false);
    private LinkedBlockingQueue<DataPacket> mTxBuffer = new LinkedBlockingQueue<DataPacket>(128);
    private LinkedBlockingQueue<DataPacket> mRxBuffer = new LinkedBlockingQueue<DataPacket>(128);

    private TCPStatus mTCPStatus = TCPStatus.Disconnected;
    private ReentrantLock mTCPStatusLock = new ReentrantLock();

    private HashMap<Byte, HashSet<IPacketHandler>> mPacketHandlers =
            new HashMap<Byte, HashSet<IPacketHandler>>();

    private ConnectionManager() {
        registerPacketTypes();

        mThread = new Thread(mRunnable, "ConnectionManager");
        mThread.start();
    }

    private void registerPacketTypes() {
        PacketTypes types = PacketTypes.getInstance();
        types.registerClass((byte) 0, DataPacket.class);
        types.registerClass((byte) 1, HelloPacket.class);
        types.registerClass((byte) 2, SetPlayerInfoPacket.class);
        types.registerClass((byte) 3, SetStatsPacket.class);
        types.registerClass((byte) 4, GetFilePacket.class);
        types.registerClass((byte) 5, SetFilePacket.class);
        types.registerClass((byte) 6, SetGameInfoPacket.class);
        types.registerClass((byte) 7, SetPlayerEffectsPacket.class);
        types.registerClass((byte) 8, SetInventoryPacket.class);
        types.registerClass((byte) 9, SetQuestsPacket.class);
        types.registerClass((byte) 10, SetNotesPacket.class);

        types.registerClass((byte) 12, SetWorldInfoPacket.class);
        types.registerClass((byte) 13, SendFileDataPacket.class);
        types.registerClass((byte) 14, SetMapMarkersPacket.class);
    }

    public TCPStatus getTCPStatus() {
        TCPStatus status;
        mTCPStatusLock.lock();
        status = mTCPStatus;
        mTCPStatusLock.unlock();
        return status;
    }

    public boolean isConnected() {
        return getTCPStatus() == TCPStatus.Connected;
    }

    public void connect(String hostname, int port) {
        if(mConnected.get()) {
            Gdx.app.error("TCP", "Tried to connect while already connected");
            return;
        }
        mTCPHostname = hostname;
        mTCPPort = port;
        mConnected.set(true);
    }

    public void disconnect() {
        mConnected.set(false);
    }

    public void send(DataPacket packet) {
        if(!mTxBuffer.offer(packet)) {
            Gdx.app.error("TCP", "Error TX buffer full");
        }
    }

    public DataPacket recv() {
        return mRxBuffer.poll();
    }

    public int available() {
        return mRxBuffer.size();
    }

    public void registerPacketHandler(byte type, IPacketHandler handler) {
        HashSet<IPacketHandler> handlers = mPacketHandlers.get(type);
        if(handlers == null) {
            handlers = new HashSet<IPacketHandler>(1);
        }
        handlers.add(handler);
        mPacketHandlers.put(type, handlers);
    }

    public void deregisterPacketHandler(byte type, IPacketHandler handler) {
        HashSet<IPacketHandler> handlers = mPacketHandlers.get(type);
        if(handlers == null)
            return;
        handlers.remove(handler);
        if(handlers.isEmpty()) {
            mPacketHandlers.remove(type);
        } else {
            mPacketHandlers.put(type, handlers);
        }
    }

    public void dispatchPackets() {
        DataPacket packet;
        while((packet = recv())!= null) {
            HashSet<IPacketHandler> handlers = mPacketHandlers.get(packet.getType());
            if(handlers == null) {
                Gdx.app.error("PACKET", "No packet handler registered for packet with type " +
                    Byte.toString(packet.getType()));
            } else {
                for(IPacketHandler handler : handlers) {
                    handler.HandlePacket(packet);
                }
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        private TCPConnection mTCP = new TCPConnection();

        @Override
        public void run() {

            while(true) {
                boolean connected = mConnected.get();
                if(mTCP.isConnected() != connected) {
                    if(connected) {
                        mTCPStatusLock.lock();
                        mTCPStatus = TCPStatus.Connecting;
                        mTCPStatusLock.unlock();
                        if(!mTCP.connect(mTCPHostname, mTCPPort)){
                            mConnected.set(false);
                        }
                        mTCPStatusLock.lock();
                        if(mConnected.get()) {
                            mTCPStatus = TCPStatus.Connected;
                        } else {
                            mTCPStatus = TCPStatus.ConnectionFailed;
                        }
                        mTCPStatusLock.unlock();
                    } else {
                        mTCP.disconnect();
                        mTCPStatusLock.lock();
                        mTCPStatus = TCPStatus.Disconnected;
                        mTCPStatusLock.unlock();
                    }
                }
                if(mTCP.isConnected()) {
                    try {
                        // Try send packets
                        while(mTxBuffer.size() > 0) {
                            DataPacket packet = mTxBuffer.poll();
                            if(packet != null) {
                                mTCP.write(packet);
                                Gdx.app.debug("TCP", "Packet transmitted: " + packet.toString());
                            }
                        }
                    } catch (IOException e) {
                        Gdx.app.error("Error while transmitting packet: " , e.getMessage());
                        disconnect();
                    }
                    try {
                        // Try receive packets
                        while(mTCP.isAvailable()) {
                            DataPacket packet = mTCP.read();
                            Gdx.app.debug("TCP", "Packet received: " + packet.toString());
                            if (!mRxBuffer.offer(packet)) {
                                Gdx.app.error("TCP", "RX buffer full");
                            }
                        }
                    } catch (IOException e) {
                        Gdx.app.error("Error while receiving packet: ", e.getMessage());
                        disconnect();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
            }
        }
    };
}
