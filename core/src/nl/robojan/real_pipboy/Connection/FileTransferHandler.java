package nl.robojan.real_pipboy.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.TreeMap;

import nl.robojan.real_pipboy.Connection.Packets.DataPacket;
import nl.robojan.real_pipboy.Connection.Packets.GetFilePacket;
import nl.robojan.real_pipboy.Connection.Packets.PacketTypes;
import nl.robojan.real_pipboy.Connection.Packets.SendFileDataPacket;
import nl.robojan.real_pipboy.Connection.Packets.SetFilePacket;

/**
 * Created by s120330 on 2-8-2015.
 */
public class FileTransferHandler implements IPacketHandler {

    private static FileTransferHandler ourInstance = new FileTransferHandler();
    public static FileTransferHandler getInstance() {
        return ourInstance;
    }

    private static byte mId = 0;

    private class FileTransferStatus {
        public byte id;
        public byte flags;
        public String fileName;
        public int partsReceived = 0;
        public int totalParts = 0;
        public long fileSize = 0;
        public FileHandle resultFileHandle = null;
        public boolean completed = false;
        public boolean forgetOnCompletion = false;
        public RandomAccessFile randomAccessFile = null;

        public FileTransferStatus(byte id, byte flags, String fileName) {
            this.id = id;
            this.flags = flags;
            this.fileName = fileName;
        }
    }

    private TreeMap<Byte, FileTransferStatus> mTransferStatus = new TreeMap<Byte, FileTransferStatus>();

    private FileTransferHandler() {

    }

    public void init() {
        if(!Gdx.files.isLocalStorageAvailable()) {
            Gdx.app.error("FILETRANSFER", "File transfers are not possible, local storage is not available");
            return;
        }
        ConnectionManager conn = ConnectionManager.getInstance();
        conn.registerPacketHandler(PacketTypes.getInstance().getType(SetFilePacket.class), this);
        conn.registerPacketHandler(PacketTypes.getInstance().getType(SendFileDataPacket.class), this);
    }

    public void clearFinishedTransfers() {
        for(Map.Entry<Byte, FileTransferStatus> status : mTransferStatus.entrySet()) {
            if(status.getValue().completed) {
                mTransferStatus.remove(status);
            }
        }
    }

    public void clearTransfers() {
        mTransferStatus.clear();
    }

    public void removeTransfer(int id) {
        assert(Byte.MIN_VALUE <= id && id <= Byte.MAX_VALUE);
        FileTransferStatus status = mTransferStatus.get((byte)id);
        if(status != null) {
            mTransferStatus.remove((byte)id);
        }
    }

    public boolean isTransferCompleted(int id) {
        assert(Byte.MIN_VALUE <= id && id <= Byte.MAX_VALUE);
        FileTransferStatus status = mTransferStatus.get((byte)id);
        return status == null || status.completed;
    }

    public boolean isTransferSuccessful(int id) {
        assert(Byte.MIN_VALUE <= id && id <= Byte.MAX_VALUE);
        FileTransferStatus status = mTransferStatus.get((byte)id);
        return status != null && status.completed && status.resultFileHandle != null;
    }

    public FileHandle getTransferFileHandle(int id) {
        assert(Byte.MIN_VALUE <= id && id <= Byte.MAX_VALUE);
        FileTransferStatus status = mTransferStatus.get((byte)id);
        if(status != null) {
            return status.resultFileHandle;
        }
        return null;
    }

    public int getFileAsync(String file, boolean noConversion) {
        byte id = mId++;
        FileTransferStatus status = mTransferStatus.get(id);
        if(status != null && !status.completed) {
            Gdx.app.error("FILETRANSFER", "Overwriting old active transfer");
        }
        byte flags = 0;
        if(noConversion) flags |= GetFilePacket.FLAG_NOCONVERSION;
        status = new FileTransferStatus(mId++, flags, file);
        ConnectionManager conn = ConnectionManager.getInstance();
        if(conn.isConnected()) {
            conn.send(new GetFilePacket(status.fileName, status.id, status.flags));
        } else {
            status.completed = true;
        }
        mTransferStatus.put(status.id, status);
        return status.id;
    }

    public FileHandle getFileSync(String file, boolean noConversion) {
        // Start transfer
        int id = getFileAsync(file, noConversion);

        // Wait till it is completed
        while(!isTransferCompleted(id)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        FileHandle handle = getTransferFileHandle(id);

        removeTransfer(id);

        return handle;
    }

    @Override
    public void HandlePacket(DataPacket packet) {
        if(packet instanceof SetFilePacket) {
            HandleSetFilePacket((SetFilePacket) packet);
        } else if(packet instanceof SendFileDataPacket) {
            HandleSendFileDataPacket((SendFileDataPacket) packet);
        } else {
            Gdx.app.error("FILETRANSFER", "Unknown packet type received");
        }
    }

    public void HandleSetFilePacket(SetFilePacket packet) {
        byte id = packet.getId();
        if(!mTransferStatus.containsKey(id) || mTransferStatus.get(id).completed) {
            Gdx.app.error("FILETRANSFER", "Received SetFile packet for an unknown file");
            return;
        }
        FileTransferStatus status = mTransferStatus.get(id);
        if(packet.getNumPackets() < 0) {
            Gdx.app.log("FILETRANSFER", "Filetransfer failed for file " + status.fileName);
            status.completed = true;
            return;
        }
        status.totalParts = packet.getNumPackets();
        String fileName = packet.getFileName().toLowerCase().replace('\\', '/');
        FileHandle handle = Gdx.files.local(fileName);
        try {
            handle.parent().mkdirs();
            handle.file().createNewFile();
            status.randomAccessFile = new RandomAccessFile(handle.file(), "rw");
            status.fileSize = packet.getFileLength();
            status.randomAccessFile.setLength(status.fileSize);
            status.resultFileHandle = handle;
        } catch (IOException e) {
            Gdx.app.error("FILETRANSFER", "Error in file creation of file " + fileName);
            e.printStackTrace();
            status.completed = true;
            status.fileSize = 0;
        }
        mTransferStatus.put(id, status);
    }

    public void HandleSendFileDataPacket(SendFileDataPacket packet) {
        byte id = packet.getId();
        if(!mTransferStatus.containsKey(id) || mTransferStatus.get(id).completed) {
            Gdx.app.error("FILETRANSFER", "Received SendFileData packet for an unknown file");
            return;
        }
        FileTransferStatus status = mTransferStatus.get(id);
        long offset = packet.getOffset();
        int chunkIndex = packet.getChunkIdx();
        byte[] data = packet.getData();
        if(chunkIndex >= status.totalParts || chunkIndex < 0) {
            Gdx.app.error("FILETRANSFER", "File chunk index is outside the boundary");
            return;
        }
        if(offset >= status.fileSize) {
            Gdx.app.error("FILETRANSFER", "File offset is greater than the file size");
            return;
        }
        if(status.randomAccessFile == null) {
            Gdx.app.error("FILETRANSFER", "File not opened");
            return;
        }
        status.partsReceived += 1;
        try {
            status.randomAccessFile.seek(offset);
            status.randomAccessFile.write(data);
        } catch (IOException e) {
            Gdx.app.error("FILETRANSFER", "Error in writing chunk at " + Long.toHexString(offset));
            e.printStackTrace();
        }
        if(status.partsReceived >= status.totalParts) {
            status.completed = true;
            try {
                status.randomAccessFile.close();
                status.randomAccessFile = null;
                if(status.forgetOnCompletion) {
                    mTransferStatus.remove(id);
                }
            } catch (IOException e) {
                Gdx.app.error("FILETRANSFER", "Error closing file");
                e.printStackTrace();
            }
        }

    }
}
