package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 15-7-2015.
 */
public class HelloPacket extends DataPacket {
    private String mName;
    private short mProtocolVersion;

    public HelloPacket(String name, short protocolVersion) {
        mName = name;
        mProtocolVersion = protocolVersion;
        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public HelloPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public HelloPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        mProtocolVersion = buffer.getShort();
        int strLen = buffer.getShort();
        assert(strLen <= len - 4);
        mName = new String(data, buffer.position(), strLen, charset);
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 4 + mName.length();
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.putShort(mProtocolVersion);
        buffer.putShort((short)mName.length());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        enc.encode(CharBuffer.wrap(mName), buffer, true);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "HelloPacket{ProtocolVersion=" + Short.toString(mProtocolVersion) +", Name=\"" +
            mName + "\"}";
    }

    public short getProtocolVersion() {
        return mProtocolVersion;
    }

    public void setProtocolVersion(short protocolVersion) {
        mProtocolVersion = protocolVersion;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
