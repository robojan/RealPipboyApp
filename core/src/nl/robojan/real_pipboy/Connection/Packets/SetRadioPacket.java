package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.Note;
import nl.robojan.real_pipboy.FalloutData.NoteList;
import nl.robojan.real_pipboy.FalloutData.Radio;
import nl.robojan.real_pipboy.FalloutData.RadioList;

/**
 * Created by s120330 on 28-7-2015.
 */
public class SetRadioPacket extends DataPacket {

    private RadioList mStations;

    public SetRadioPacket(RadioList items) {
        mStations = items;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetRadioPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetRadioPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mStations = new RadioList();

        int numStations = buffer.getShort();
        for(int i = 0; i < numStations; i++) {
            int id = buffer.getInt();
            byte flags = buffer.get();
            short nameLen = buffer.getShort();
            String name = new String(data, buffer.position(), nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            mStations.add(new Radio(id, name, flags));
        }
    }

    @Override
    public int getSize() {
        int itemsSize = 0;

        for(Radio radio : mStations.list) {
            itemsSize += 7 + radio.getName().length();
        }

        return DataPacketHeader.getHeaderSize() + 2 + itemsSize;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);

        buffer.putShort((short) mStations.list.size);

        for(Radio radio : mStations.list) {
            buffer.putInt(radio.getId());
            buffer.put(radio.getFlags());
            buffer.putShort((short)radio.getName().length());
            enc.encode(CharBuffer.wrap(radio.getName()), buffer, true);
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetRadioPacket{stations=" + Integer.toString(mStations.list.size) + "}";
    }

    public RadioList getStations() {
        return mStations;
    }

    public void setStations(RadioList stations) {
        mStations = stations;
    }
}
