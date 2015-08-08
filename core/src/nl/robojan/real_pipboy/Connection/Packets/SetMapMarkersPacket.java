package nl.robojan.real_pipboy.Connection.Packets;

import com.badlogic.gdx.math.Vector3;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.MapMarker;
import nl.robojan.real_pipboy.FalloutData.MapMarkerList;

/**
 * Created by s120330 on 28-7-2015.
 */
public class SetMapMarkersPacket extends DataPacket {

    private MapMarkerList mMarkers;

    public SetMapMarkersPacket(MapMarkerList markers) {
        mMarkers = markers;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetMapMarkersPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetMapMarkersPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mMarkers = new MapMarkerList();

        int numNotes = buffer.getShort();
        for(int i = 0; i < numNotes; i++) {
            short type = buffer.getShort();
            short flags = buffer.getShort();
            int id = buffer.getInt();
            float x = buffer.getFloat();
            float y = buffer.getFloat();
            float z = buffer.getFloat();
            String name = "";
            String reputation = "";
            short nameLen = buffer.getShort();
            if(nameLen > 0)
                name = new String(data, buffer.position(), nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            short reputationLen = buffer.getShort();
            if(reputationLen > 0)
                reputation = new String(data, buffer.position(), reputationLen, charset);
            buffer.position(buffer.position() + reputationLen);
            mMarkers.add(new MapMarker(id, new Vector3(x, y, z), name, reputation, type, flags));
        }
    }

    @Override
    public int getSize() {
        int itemsSize = 0;

        for(MapMarker marker : mMarkers.markers) {
            itemsSize += 24;
            if(marker.getName() != null)
                itemsSize += marker.getName().length();
            if(marker.getReputation() != null)
                itemsSize += marker.getReputation().length();
        }

        return DataPacketHeader.getHeaderSize() + 2 + itemsSize;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);

        buffer.putShort((short) mMarkers.markers.size);

        for(MapMarker marker : mMarkers.markers) {
            buffer.putShort(marker.getType());
            buffer.putShort(marker.getFlags());
            buffer.putInt(marker.getId());
            buffer.putFloat(marker.getPos().x);
            buffer.putFloat(marker.getPos().y);
            buffer.putFloat(marker.getPos().z);
            if(marker.getName() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)marker.getName().length());
                enc.encode(CharBuffer.wrap(marker.getName()), buffer, true);
            }
            if(marker.getReputation() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)marker.getReputation().length());
                enc.encode(CharBuffer.wrap(marker.getReputation()), buffer, true);
            }
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetMapMarkerPacket{markers=" + Integer.toString(mMarkers.markers.size) + "}";
    }

    public MapMarkerList getMarkers() {
        return mMarkers;
    }

    public void setMarkers(MapMarkerList markers) {
        mMarkers = markers;
    }
}
