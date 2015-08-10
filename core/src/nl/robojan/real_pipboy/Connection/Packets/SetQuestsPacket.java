package nl.robojan.real_pipboy.Connection.Packets;

import com.badlogic.gdx.math.Vector3;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.Quest;
import nl.robojan.real_pipboy.FalloutData.QuestList;
import nl.robojan.real_pipboy.FalloutData.QuestObjective;
import nl.robojan.real_pipboy.FalloutData.QuestTarget;

/**
 * Created by s120330 on 29-7-2015.
 */
public class SetQuestsPacket extends DataPacket {

    private QuestList mQuests;

    public SetQuestsPacket(QuestList items) {
        mQuests = items;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetQuestsPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetQuestsPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mQuests = new QuestList();

        int numQuests = buffer.getShort();
        for(int i = 0; i < numQuests; i++) {
            int questID = buffer.getInt();
            int flags = buffer.getInt();
            String name = "";
            short nameLen = buffer.getShort();
            if(nameLen > 0)
                name = new String(data, buffer.position(), nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            Quest quest = new Quest(questID, name, flags);

            short numObjectives = buffer.getShort();
            for(int j = 0; j < numObjectives; j++) {
                int objectiveID = buffer.getInt();
                int objectiveFlags = buffer.getInt();
                String text = "";
                short textLen = buffer.getShort();
                if(textLen > 0)
                    text = new String(data, buffer.position(), textLen, charset);
                buffer.position(buffer.position() + textLen);

                QuestObjective objective = new QuestObjective(objectiveID, text, objectiveFlags);

                short numTargets = buffer.getShort();
                for(int k = 0; k < numTargets; k++) {
                    int targetID = buffer.getInt();
                    float x = buffer.getFloat();
                    float y = buffer.getFloat();
                    float z = buffer.getFloat();
                    objective.addTarget(new QuestTarget(targetID, new Vector3(x, y, z)));
                }
                quest.addObjective(objective);
            }
            mQuests.add(quest);
        }
    }

    @Override
    public int getSize() {
        int itemsSize = 0;

        for(Quest quest : mQuests.list) {
            itemsSize += 12;
            if(quest.getName() != null)
                itemsSize += quest.getName().length();
            for(QuestObjective objective : quest.getObjectives()) {
                itemsSize += 12;
                if(objective.getText() != null) {
                    itemsSize += objective.getText().length();
                }
                for(QuestTarget target :objective.getTargets()) {
                    itemsSize += 4 * 4;
                }
            }
        }

        return DataPacketHeader.getHeaderSize() + 2 + itemsSize;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);

        buffer.putShort((short) mQuests.list.size);

        for(Quest quest : mQuests.list) {
            buffer.putInt(quest.getQuestID());
            buffer.putInt(quest.getFlags());
            if(quest.getName() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short) quest.getName().length());
                enc.encode(CharBuffer.wrap(quest.getName()), buffer, true);
            }
            buffer.putShort((short) quest.getObjectives().size);
            for(QuestObjective objective : quest.getObjectives()) {
                buffer.putInt(objective.getObjectiveID());
                buffer.putInt(objective.getFlags());
                if(objective.getText() == null) {
                    buffer.putShort((short)0);
                } else {
                    buffer.putShort((short) objective.getText().length());
                    enc.encode(CharBuffer.wrap(objective.getText()), buffer, true);
                }
                buffer.putShort((short)objective.getTargets().size);
                for(QuestTarget target : objective.getTargets()) {
                    buffer.putInt(target.id);
                    buffer.putFloat(target.pos.x);
                    buffer.putFloat(target.pos.y);
                    buffer.putFloat(target.pos.z);
                }
            }
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetQuestsPacket{quests=" + Integer.toString(mQuests.list.size) + "}";
    }

    public QuestList getQuests() {
        return mQuests;
    }

    public void setQuests(QuestList quests) {
        mQuests = quests;
    }
}
