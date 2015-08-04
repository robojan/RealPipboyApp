package nl.robojan.real_pipboy.FalloutData;

import java.util.Objects;

/**
 * Created by s120330 on 28-7-2015.
 */
public class Note {
    public static final int TYPE_SOUND = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_VOICE = 3;

    public static final int FLAG_READ = 1;

    private String mTitle;
    private int mType;
    private String mContent;
    private int mFlags;

    public Note(String title, int type, String content, int flags) {
        mTitle = title;
        mType = type;
        mContent = content;
        mFlags = flags;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getType() {
        return mType;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public String toString() {
        return "Note{" + mTitle + "}";
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(Note.class.isAssignableFrom(obj.getClass())))
            return false;
        Note other = (Note)obj;

        return Objects.equals(this.mTitle, other.mTitle) &&
                Objects.equals(this.mContent, other.mContent) &&
                this.mType == other.mType &&
                this.mFlags == other.mFlags;
    }
}
