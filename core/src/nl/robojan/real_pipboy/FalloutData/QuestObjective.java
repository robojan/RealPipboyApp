package nl.robojan.real_pipboy.FalloutData;

import java.util.Objects;

/**
 * Created by s120330 on 29-7-2015.
 */
public class QuestObjective {
    private String mText;
    private int mObjectiveID;
    private int mFlags;

    public QuestObjective(int id, String text, int flags) {
        mObjectiveID = id;
        mText = text;
        mFlags = flags;
    }

    public boolean isCompleted() {
        return (mFlags & Quest.FLAG_COMPLETED) != 0;
    }

    public void setCompleted(boolean completed) {
        if (completed) {
            mFlags |= Quest.FLAG_COMPLETED;
        } else {
            mFlags &= ~Quest.FLAG_COMPLETED;
        }
    }

    public boolean isDisplayed() {
        return (mFlags & Quest.FLAG_DISPLAYED) != 0;
    }

    public void setDisplayed(boolean displayed) {
        if (displayed) {
            mFlags |= Quest.FLAG_DISPLAYED;
        } else {
            mFlags &= ~Quest.FLAG_DISPLAYED;
        }
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getObjectiveID() {
        return mObjectiveID;
    }

    public void setObjectiveID(int objectiveID) {
        mObjectiveID = objectiveID;
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public boolean equivalent(Object obj) {
        if (obj == null || !(QuestObjective.class.isAssignableFrom(obj.getClass())))
            return false;
        QuestObjective other = (QuestObjective) obj;
        return (Objects.equals(this.mText, other.mText) &&
                this.mObjectiveID == other.mObjectiveID &&
                this.mFlags == other.mFlags);
    }
}
