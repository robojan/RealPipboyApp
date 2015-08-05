package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 2-8-2015.
 */
public class SetFilePacket extends DataPacket {

    private String mFileName;
    private byte mId;
    private long mFileLength;
    private int mNumPackets;

    public SetFilePacket(String fileName, byte id, long fileLength, int numPackets) {
        mFileName = fileName;
        mId = id;
        mFileLength = fileLength;
        mNumPackets = numPackets;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetFilePacket(String fileName, byte id) {
        mFileName = fileName;
        mId = id;
        mFileLength = 0;
        mNumPackets = -1;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetFilePacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetFilePacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        int nameLen = buffer.getShort();
        mFileName = new String(data, buffer.position(), nameLen, charset);
        buffer.position(buffer.position() + nameLen);
        mId = buffer.get();
        //noinspection PointlessBitwiseExpression
        mFileLength = ((long)buffer.getInt()) & 0xFFFFFFFF;
        mNumPackets = buffer.getInt();
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 11 + mFileName.length();
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);
        buffer.putShort((short) mFileName.length());
        enc.encode(CharBuffer.wrap(mFileName), buffer, true);
        buffer.put(mId);
        buffer.putInt((int)mFileLength);
        buffer.putInt(mNumPackets);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetFilePacket{" + mFileName + ", id=" + mId + ", " +
                (mNumPackets >= 0 ? "Successful, size=" + mFileLength + ", #packets=" + mNumPackets
                        : "Failed") + "}";
    }

    public int getNumPackets() {
        return mNumPackets;
    }

    public void setNumPackets(int numPackets) {
        mNumPackets = numPackets;
    }

    public long getFileLength() {
        return mFileLength;
    }

    public void setFileLength(long fileLength) {
        mFileLength = fileLength;
    }

    public byte getId() {
        return mId;
    }

    public void setId(byte id) {
        mId = id;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }
}
