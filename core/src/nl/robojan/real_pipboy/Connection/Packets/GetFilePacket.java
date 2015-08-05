package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 1-8-2015.
 */
public class GetFilePacket extends DataPacket {
    public static final byte FLAG_NOCONVERSION = 0x01;

    private String mFileName;
    private byte mId;
    private byte mFlags;

    public GetFilePacket(String name, byte id, byte flags) {
        mFileName = name;
        mId = id;
        mFlags = flags;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public GetFilePacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public GetFilePacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mId = buffer.get();
        mFlags = buffer.get();
        short fileLen = buffer.getShort();
        mFileName = new String(data, buffer.position(), fileLen, charset);
        buffer.position(buffer.position() + fileLen);
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 4 + mFileName.length();
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);
        buffer.put(mId);
        buffer.put(mFlags);
        buffer.putShort((short) mFileName.length());
        enc.encode(CharBuffer.wrap(mFileName), buffer, true);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "GetFilePacket{id=" + mId + ", flags=" + mFlags + ", file=" + mFileName + "}";
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public byte getId() {
        return mId;
    }

    public void setId(byte id) {
        mId = id;
    }

    public byte getFlags() {
        return mFlags;
    }

    public void setFlags(byte flags) {
        mFlags = flags;
    }
}
