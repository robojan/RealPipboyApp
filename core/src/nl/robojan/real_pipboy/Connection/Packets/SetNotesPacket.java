package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.Note;
import nl.robojan.real_pipboy.FalloutData.NoteList;

/**
 * Created by s120330 on 28-7-2015.
 */
public class SetNotesPacket extends DataPacket {

    private NoteList mNotes;

    public SetNotesPacket(NoteList items) {
        mNotes = items;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetNotesPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetNotesPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mNotes = new NoteList();

        int numNotes = buffer.getShort();
        for(int i = 0; i < numNotes; i++) {
            int type = buffer.get();
            int flags = buffer.get();
            String title = "";
            String content = "";
            short titleLen = buffer.getShort();
            if(titleLen > 0)
                title = new String(data, buffer.position(), titleLen, charset);
            buffer.position(buffer.position() + titleLen);
            short contentLen = buffer.getShort();
            if(contentLen > 0)
                content = new String(data, buffer.position(), contentLen, charset);
            if(type == Note.TYPE_IMAGE) {
                content = content.replace('\\', '/');
            }
            mNotes.add(new Note(title, type, content, flags));
            buffer.position(buffer.position() + contentLen);
        }
    }

    @Override
    public int getSize() {
        int itemsSize = 0;

        for(Note note : mNotes.list) {
            itemsSize += 6;
            if(note.getTitle() != null)
                itemsSize += note.getTitle().length();
            if(note.getContent() != null)
                itemsSize += note.getContent().length();
        }

        return DataPacketHeader.getHeaderSize() + 2 + itemsSize;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);

        buffer.putShort((short) mNotes.list.size);

        for(Note note : mNotes.list) {
            buffer.put((byte) note.getType());
            buffer.put((byte) note.getFlags());
            if(note.getTitle() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)note.getTitle().length());
                enc.encode(CharBuffer.wrap(note.getTitle()), buffer, true);
            }
            if(note.getContent() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)note.getContent().length());
                enc.encode(CharBuffer.wrap(note.getContent()), buffer, true);
            }
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetNotesPacket{notes=" + Integer.toString(mNotes.list.size) + "}";
    }

    public NoteList getNotes() {
        return mNotes;
    }

    public void setNotes(NoteList notes) {
        mNotes = notes;
    }
}
