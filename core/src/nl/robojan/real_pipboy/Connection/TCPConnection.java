package nl.robojan.real_pipboy.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import nl.robojan.real_pipboy.Connection.Packets.DataPacket;
import nl.robojan.real_pipboy.Connection.Packets.DataPacketHeader;
import nl.robojan.real_pipboy.Connection.Packets.PacketTypes;

/**
 * Created by s120330 on 14-7-2015.
 */
public class TCPConnection {
    private Socket mSocket;
    private InputStream mInputStream;

    private byte[] mReceiveBuffer;
    private int mDataAvailable;

    public TCPConnection() {
        mReceiveBuffer = null;
        mDataAvailable = 0;
    }

    public boolean connect(String hostname, int port) {
        return connect(hostname,port, 3000, 5);
    }

    public boolean connect(String hostname, int port, int timeout, int retries) {
        if(isConnected()) {
            Gdx.app.error("TCP", "Tried to connect while already connected");
            return false;
        }

        mSocket = new Socket();

        try {
            mSocket.setPerformancePreferences(0, 1, 2);
            mSocket.setTrafficClass(0x14);
            mSocket.setTcpNoDelay(true);
            mSocket.setKeepAlive(true);
            mSocket.setSendBufferSize(128 * 1024);
            mSocket.setReceiveBufferSize(128 * 1024);
            mSocket.setSoLinger(false, 0);
            mSocket.setSoTimeout(0);
            InetSocketAddress address = new InetSocketAddress(hostname, port);
            mSocket.connect(address, timeout);
            mInputStream = mSocket.getInputStream();
            return true;
        } catch (SocketException e) {
            Gdx.app.error("TCP", "Error creating socket: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch(SocketTimeoutException e) {
            if(retries > 0) {
                return connect(hostname, port, timeout, retries - 1);
            }
            Gdx.app.error("TCP", "Timeout while connecting to server");
            return false;
        } catch (IOException e) {
            Gdx.app.error("TCP", "Error creating socket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        //mSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostname, port, hints);
    }


    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    public void disconnect() {
        if(!isConnected())
            return;
        if(mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch(IOException e) {
                Gdx.app.error("TCP", "Error closing socket: " + e.getMessage());
            }
        }
        mInputStream = null;
    }

    public void write(DataPacket packet) throws IOException {
        if(!isConnected()) {
            throw new IOException("Socket is not connected");
        }
        OutputStream os = mSocket.getOutputStream();

        byte[] data = packet.getBytes();
        os.write(data, 0, data.length);
    }

    public void fillReceiveBuffer() throws IOException {
        int available = mInputStream.available();
        int read = 0;
        if(mReceiveBuffer == null) {
            mReceiveBuffer = new byte[available];
            read = mInputStream.read(mReceiveBuffer, 0, available);
            mDataAvailable = 0;
        } else {
            if(mDataAvailable + available > mReceiveBuffer.length) {
                byte[] newBuffer = new byte[mDataAvailable + available];
                System.arraycopy(mReceiveBuffer, 0, newBuffer, 0, mDataAvailable);
                read = mInputStream.read(newBuffer, mDataAvailable, available);
                mReceiveBuffer = newBuffer;
            } else {
                read = mInputStream.read(mReceiveBuffer, mDataAvailable, available);
            }
        }
        mDataAvailable += read;
    }

    public boolean isAvailable() throws IOException {
        //if(!isConnected())
        //    return false;
        fillReceiveBuffer();
        int headerSize = DataPacketHeader.getHeaderSize();
        // Search for packet
        for(int i = 0; i < mDataAvailable - headerSize; i++) {
            if(DataPacket.containsCompletePacket(mReceiveBuffer, i, mDataAvailable - i)) {
                // Contains a valid packet
                if(i > 0) {
                    mDataAvailable -= i;
                    System.arraycopy(mReceiveBuffer, i, mReceiveBuffer, 0, mDataAvailable);
                }
                return true;
            } else if(DataPacketHeader.isValidHeader(mReceiveBuffer, i , mDataAvailable - i)) {
                // Does not contain a complete packet, but does contain a header so stop searching
                if(i > 0) {
                    mDataAvailable -= i;
                    System.arraycopy(mReceiveBuffer, i, mReceiveBuffer, 0, mDataAvailable);
                }
                return false;
            }
        }
        // No packet found
        return false;
    }

    public DataPacket read() throws IOException {
        while(!isAvailable()) {
            if(!isConnected())
                throw new IOException("Socket is not connected");
        };

        DataPacketHeader header = new DataPacketHeader(mReceiveBuffer, 0, mDataAvailable);
        int headerSize = DataPacketHeader.getHeaderSize();
        DataPacket packet = PacketTypes.getInstance().getDataPacket(header, mReceiveBuffer,
                headerSize, mDataAvailable - headerSize);
        int packetSize = packet.getSize();
        if(mDataAvailable - packetSize < 0) {
            Gdx.app.error("TCP", "Packetsize is greater than the buffer, Avail: " +
                mDataAvailable + ", size: " + packetSize + ", packet: " + packet);
            packetSize = mDataAvailable;
        }
        System.arraycopy(mReceiveBuffer, packetSize, mReceiveBuffer, 0,
                mDataAvailable - packetSize);
        mDataAvailable -= packetSize;

        return packet;
    }
}
