package nl.robojan.real_pipboy.Connection.Packets;


import java.nio.ByteBuffer;
/**
 * Created by s120330 on 15-7-2015.
 */
public class DataPacket {
    protected DataPacketHeader mHeader;
    protected byte[] mData;

    public DataPacket(byte[] headerBuffer, int headerOffset, int headerLen,
                      byte[] buffer, int dataOffset, int dataLen) {
        mHeader = new DataPacketHeader(headerBuffer, headerOffset, headerLen);
        assert(dataLen >= mHeader.getDataSize());
        readPacket(buffer, dataOffset, dataLen);
    }

    public DataPacket(byte[] buffer, int offset, int len) {
        mHeader = new DataPacketHeader(buffer, offset, len);
        int headerSize = DataPacketHeader.getHeaderSize();
        assert(len - headerSize >= mHeader.getDataSize());
        readPacket(buffer, offset + headerSize, len - headerSize);
    }

    public DataPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        mHeader = header;
        assert(len >= mHeader.getDataSize());
        readPacket(data, offset, len);
    }

    protected DataPacket(DataPacketHeader header) {
        mHeader = header;
    }

    protected DataPacket() {
        mHeader = null;
    }

    public byte getType() {
        return mHeader.getType();
    }

    public int getSize() {
        return mHeader.getDataSize() + DataPacketHeader.getHeaderSize();
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(mHeader.getBytes(), 0, DataPacketHeader.getHeaderSize());
        buffer.put(mData, DataPacketHeader.getHeaderSize(), mData.length);
        return buffer.array();
    }

    public static boolean containsCompletePacket(byte[] buffer, int offset, int len) {
        if(!DataPacketHeader.isValidHeader(buffer, offset, len))
            return false;
        DataPacketHeader header = new DataPacketHeader(buffer, offset, len);
        return DataPacketHeader.getHeaderSize() + header.getDataSize() <= len;
    }

    public static boolean containsCompletePacket(byte[] buffer) {
        return containsCompletePacket(buffer, 0, buffer.length);
    }

    private void readPacket(byte[] buffer, int off, int len) {
        int dataSize = mHeader.getDataSize();
        assert(len >= dataSize);
        System.arraycopy(buffer, off,  mData, 0, dataSize);
    }

    @Override
    public String toString() {
        return "DataPacket{type=" + Byte.toString(getType()) + ", Size=" +
                Integer.toString(getSize()) + "}";
    }
}
