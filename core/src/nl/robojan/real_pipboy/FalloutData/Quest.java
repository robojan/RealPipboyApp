package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.utils.Array;

import java.util.Objects;

/**
 * Created by s120330 on 29-7-2015.
 */
public class Quest {
    public static final int FLAG_COMPLETED = 1;
    public static final int FLAG_ACTIVEQUEST = 2;
    public static final int FLAG_DISPLAYED = 4;

    private String mName;
    private int mFlags;
    private int mQuestID;
    private Array<QuestObjective> mObjectives = new Array<QuestObjective>(4);

    public Quest(int questID, String name, int flags) {
        mQuestID = questID;
        mName = name;
        mFlags = flags;
    }

    public void addObjective(QuestObjective objective) {
        mObjectives.add(objective);
    }

    public Array<QuestObjective> getObjectives() {
        return mObjectives;
    }

    public boolean isCompleted() {
        return (mFlags & FLAG_COMPLETED) != 0;
    }

    public void setCompleted(boolean completed) {
        if(completed) {
            mFlags |= FLAG_COMPLETED;
        } else {
            mFlags &= ~FLAG_COMPLETED;
        }
    }

    public boolean isActive() {
        return (mFlags & FLAG_ACTIVEQUEST) != 0;
    }

    public void setActive(boolean active) {
        if(active) {
            mFlags |= FLAG_ACTIVEQUEST;
        } else {
            mFlags &= ~FLAG_ACTIVEQUEST;
        }
    }

    public int getQuestID() {
        return mQuestID;
    }

    public void setQuestID(int questID) {
        mQuestID = questID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(Quest.class.isAssignableFrom(obj.getClass())))
            return false;
        Quest other = (Quest)obj;
        if(!(Objects.equals(this.mName, other.mName) &&
                this.mQuestID == other.mQuestID &&
                this.mFlags == other.mFlags &&
                this.mObjectives.size == other.mObjectives.size)) {
            return false;
        }
        for(int i = 0; i < this.mObjectives.size; i++) {
            if(!this.mObjectives.get(i).equivalent(other.mObjectives.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Quest{\"" + mName + "\"}";
    }
}
