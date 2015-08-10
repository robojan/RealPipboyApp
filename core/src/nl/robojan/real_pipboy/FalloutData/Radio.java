package nl.robojan.real_pipboy.FalloutData;

import java.util.Objects;

/**
 * Created by s120330 on 10-8-2015.
 */
public class Radio {
    public static final byte FLAG_ACTIVE = 0x1;
    public static final byte FLAG_PLAYING = 0x2;

    private int mId;
    private String mName;
    private byte mFlags;

    public Radio(int id, String name, byte flags) {
        mId = id;
        mName = name;
        mFlags = flags;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public byte getFlags() {
        return mFlags;
    }

    public void setFlags(byte flags) {
        mFlags = flags;
    }

    public boolean isActive() {
        return (mFlags & FLAG_ACTIVE) != 0;
    }

    public boolean isPlaying() {
        return (mFlags & FLAG_PLAYING) != 0;
    }

    public String toString() {
        return "Radio{" + mName + ", active=" + isActive() + ", playing=" + isPlaying() + "}";
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(Radio.class.isAssignableFrom(obj.getClass())))
            return false;
        Radio other = (Radio)obj;

        return Objects.equals(this.mName, other.mName) &&
                this.mId == other.mId &&
                this.mFlags == other.mFlags;
    }
}
