package nl.robojan.real_pipboy.Connection.Packets;

import java.io.InvalidObjectException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.StatusItem;
import nl.robojan.real_pipboy.FalloutData.StatusList;

/**
 * Created by s120330 on 18-7-2015.
 */
public class SetStatsPacket extends DataPacket {
    // type of stats:
    // 0 Special
    // 1 Skills
    // 2 Perks
    // 3 Statistics
    byte mStatType;
    StatusList mStats;

    public SetStatsPacket(byte type, StatusList stats) {
        mStatType = type;
        mStats = stats;
        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetStatsPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetStatsPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        mStatType = buffer.get();
        mStats = new StatusList();
        while(buffer.position() + 6 <= mHeader.getDataSize() + offset) {
            String name = null, value = null, extra = null, description = null, icon = null,
                    badge = null;
            int statLen = buffer.getShort();
            int bufferStartPos = buffer.position();
            if(statLen <= 0)
                return;
            int nameLen = buffer.getShort();
            if(nameLen > 0)
                name = new String(data, buffer.position(), nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            int valueLen = buffer.getShort();
            if(valueLen > 0)
                value = new String(data, buffer.position(), valueLen, charset);
            buffer.position(buffer.position() + valueLen);
            int extraLen = buffer.getShort();
            if(extraLen > 0)
                extra = new String(data, buffer.position(), extraLen, charset);
            buffer.position(buffer.position() + extraLen);
            int descriptionLen = buffer.getShort();
            if(descriptionLen > 0)
                description = new String(data, buffer.position(), descriptionLen, charset);
            buffer.position(buffer.position() + descriptionLen);
            int iconLen = buffer.getShort();
            if(iconLen > 0)
                icon = new String(data, buffer.position(), iconLen, charset).replace('\\', '/');
            buffer.position(buffer.position() + iconLen);
            int badgeLen = buffer.getShort();
            if(badgeLen > 0)
                badge = new String(data, buffer.position(), badgeLen, charset).replace('\\', '/');
            buffer.position(buffer.position() + badgeLen);
            mStats.add(new StatusItem(name, value, extra, icon, badge, description));
            buffer.position(bufferStartPos+statLen);
        }
    }

    @Override
    public int getSize() {
        int contentSize = 0;
        for(StatusItem item : mStats.items) {
            contentSize += 6*2;
            if(item.name != null)
                contentSize += item.name.length();
            if(item.level != null)
                contentSize += item.level.length();
            if(item.extra != null)
                contentSize += item.extra.length();
            if(item.description != null)
                contentSize += item.description.length();
            if(item.icon != null)
                contentSize += item.icon.length();
            if(item.badge != null)
                contentSize += item.badge.length();
        }
        return DataPacketHeader.getHeaderSize() + 1 + mStats.items.size * 2 + contentSize;
    }

    @Override
    public byte[] getBytes() {
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.put(mStatType);

        for(StatusItem item : mStats.items) {
            // Stat length
            int statLen = 6*2;
            if(item.name != null)
                statLen += item.name.length();
            if(item.level != null)
                statLen += item.level.length();
            if(item.extra != null)
                statLen += item.extra.length();
            if(item.description != null)
                statLen += item.description.length();
            if(item.icon != null)
                statLen += item.icon.length();
            if(item.badge != null)
                statLen += item.badge.length();
            buffer.putShort((short)statLen);
            if(item.name != null) {
                buffer.putShort((short)item.name.length());
                enc.encode(CharBuffer.wrap(item.name), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
            if(item.level != null) {
                buffer.putShort((short)item.level.length());
                enc.encode(CharBuffer.wrap(item.level), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
            if(item.extra != null) {
                buffer.putShort((short)item.extra.length());
                enc.encode(CharBuffer.wrap(item.extra), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
            if(item.description != null) {
                buffer.putShort((short)item.description.length());
                enc.encode(CharBuffer.wrap(item.description), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
            if(item.icon != null) {
                buffer.putShort((short)item.icon.length());
                enc.encode(CharBuffer.wrap(item.icon), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
            if(item.badge != null) {
                buffer.putShort((short)item.badge.length());
                enc.encode(CharBuffer.wrap(item.badge), buffer, true);
            } else {
                buffer.putShort((short)0);
            }
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetStatsPacket{type=" + Byte.toString(mStatType) +"}";
    }

    public byte getStatType() {
        return mStatType;
    }

    public void setStatType(byte statType) {
        mStatType = statType;
    }

    public StatusList getStats() {
        return mStats;
    }

    public void setStats(StatusList stats) {
        mStats = stats;
    }
}
