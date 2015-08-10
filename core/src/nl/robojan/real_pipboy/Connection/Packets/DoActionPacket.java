package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 6-8-2015.
 */
public class DoActionPacket extends DataPacket {
    public static final int ACTION_USESTIMPACK = 0;
    public static final int ACTION_USEDRBAG = 1;
    public static final int ACTION_USERADAWAY = 2;
    public static final int ACTION_USERADX = 3;
    public static final int ACTION_EQUIPITEM = 4;
    public static final int ACTION_UNEQUIPITEM = 5;
    public static final int ACTION_FASTTRAVEL = 6;
    public static final int ACTION_SETACTIVEQUEST = 7;
    public static final int ACTION_TUNERADIO = 8;

    private int mId;
    private long[] mParam = new long[4];

    public DoActionPacket(int id) {
        mId = id;
        mParam[0] = 0;
        mParam[1] = 0;
        mParam[2] = 0;
        mParam[3] = 0;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(int id, long param1) {
        mId = id;
        mParam[0] = param1;
        mParam[1] = 0;
        mParam[2] = 0;
        mParam[3] = 0;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(int id, long param1, long param2) {
        mId = id;
        mParam[0] = param1;
        mParam[1] = param2;
        mParam[2] = 0;
        mParam[3] = 0;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(int id, float param1, float param2, float param3) {
        mId = id;
        mParam[0] = Float.floatToIntBits(param1);
        mParam[1] = Float.floatToIntBits(param2);
        mParam[2] = Float.floatToIntBits(param3);
        mParam[3] = 0;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(int id, long param1, long param2, long param3) {
        mId = id;
        mParam[0] = param1;
        mParam[1] = param2;
        mParam[2] = param3;
        mParam[3] = 0;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(int id, long param1, long param2, long param3, long param4) {
        mId = id;
        mParam[0] = param1;
        mParam[1] = param2;
        mParam[2] = param3;
        mParam[3] = param4;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public DoActionPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public DoActionPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);

        mId = buffer.getInt();
        mParam[0] = ((long)buffer.getInt()) & 0xFFFFFFFF;
        mParam[1] = ((long)buffer.getInt()) & 0xFFFFFFFF;
        mParam[2] = ((long)buffer.getInt()) & 0xFFFFFFFF;
        mParam[3] = ((long)buffer.getInt()) & 0xFFFFFFFF;
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 20;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.putInt(mId);
        buffer.putInt((int)mParam[0]);
        buffer.putInt((int)mParam[1]);
        buffer.putInt((int)mParam[2]);
        buffer.putInt((int)mParam[3]);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "DoActionPacket{id=" + mId + ", param=" + mParam + "}";
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long[] getParam() {
        return mParam;
    }

    public void setParam(long[] param) {
        mParam = param;
    }

    public long getParam(int index) {
        return mParam[index];
    }
}