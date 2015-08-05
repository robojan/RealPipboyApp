package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 2-8-2015.
 */
public class SendFileDataPacket extends DataPacket {

    private byte mId;
    private long mOffset;
    private int mChunkIdx;
    private byte[] mData;

    public SendFileDataPacket(byte id, long offset, int chunkIdx, byte[] data) {
        mId = id;
        mOffset = offset;
        mChunkIdx = chunkIdx;
        mData = data;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SendFileDataPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SendFileDataPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);

        mId = buffer.get();
        //noinspection PointlessBitwiseExpression
        mOffset = ((long)buffer.getInt()) & 0xFFFFFFFF;
        mChunkIdx = buffer.getInt();
        mData = new byte[mHeader.getDataSize() - 9];
        buffer.get(mData);
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 9 + mData.length;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.put(mId);
        buffer.putInt((int)mOffset);
        buffer.putInt(mChunkIdx);
        buffer.put(mData);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SendFileDataPacket{id=" + mId + ", offset=" + mOffset + ", chunkIndex=" +
                mChunkIdx + ", datasize=" + mData.length + "}";
    }

    public byte getId() {
        return mId;
    }

    public void setId(byte id) {
        mId = id;
    }

    public long getOffset() {
        return mOffset;
    }

    public void setOffset(long offset) {
        mOffset = offset;
    }

    public int getChunkIdx() {
        return mChunkIdx;
    }

    public void setChunkIdx(int chunkIdx) {
        mChunkIdx = chunkIdx;
    }

    public byte[] getData() {
        return mData;
    }

    public void setData(byte[] data) {
        mData = data;
    }
}
