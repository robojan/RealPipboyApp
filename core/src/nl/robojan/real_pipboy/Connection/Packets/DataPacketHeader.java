package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by s120330 on 15-7-2015.
 */
public class DataPacketHeader {
    private byte mType;
    private int mDataSize;

    public DataPacketHeader(byte[] buffer, int offset, int len) {
        readHeader(buffer, offset, len);
    }

    public DataPacketHeader(byte type, int size) {
        mType = type;
        mDataSize = size;
    }

    public int getDataSize() {
        return mDataSize;
    }

    public byte getType() {
        return mType;
    }

    protected int fillBytebuffer(ByteBuffer buffer) {
        int size = DataPacketHeader.getHeaderSize();
        buffer.put(getBytes(), 0, size);
        buffer.position(size);
        return size;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getHeaderSize());
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(0, (byte)'R');
        buffer.put(1, (byte)'P');
        buffer.put(2, getType());
        buffer.putShort(3, (short) getDataSize());
        return buffer.array();
    }

    public static int getHeaderSize() {
        return 5;
    }

    public static boolean isValidHeader(byte[] buffer) {
        return isValidHeader(buffer, 0, buffer.length);
    }

    public static boolean isValidHeader(byte[] buffer, int offset, int len) {
        return len >= getHeaderSize() && buffer[offset] == 'R' && buffer[offset + 1] == 'P';
    }

    private void readHeader(byte[] buffer, int offset, int len) {
        assert(isValidHeader(buffer, offset, len));
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, offset, len);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.position(offset + 2);
        mType = byteBuffer.get();
        mDataSize = ((int)byteBuffer.getShort())&0xFFFF;
    }
}
