package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 18-7-2015.
 */
public class SetGameInfoPacket extends DataPacket {
    private byte mGameVersion;
    private boolean mHardcore;

    public SetGameInfoPacket(byte gameVersion, boolean hardcore) {
        mGameVersion = gameVersion;
        mHardcore = hardcore;
        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetGameInfoPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetGameInfoPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        mGameVersion = buffer.get();
        mHardcore = buffer.get() != 0;
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 2;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.put(mGameVersion);
        buffer.put((byte)(mHardcore ? 1 : 0));
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetGameInfoPacket{GameVersion=" + Byte.toString(mGameVersion) +", Hardcore=" +
                Boolean.toString(mHardcore) + "}";
    }

    public byte getGameVersion() {
        return mGameVersion;
    }

    public void setGameVersion(byte gameVersion) {
        mGameVersion = gameVersion;
    }

    public boolean isHardcore() {
        return mHardcore;
    }

    public void setHardcore(boolean hardcore) {
        mHardcore = hardcore;
    }
}
