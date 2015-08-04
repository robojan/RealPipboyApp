package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by s120330 on 17-7-2015.
 */
public class SetPlayerInfoPacket extends DataPacket {
    private short mLevel;
    private short mHP, mMaxHP, mAP, mMaxAP;
    private int mXP, mMaxXP;
    private byte mHead, mTorso, mLeftArm, mRightArm, mLeftLeg, mRightLeg, mRadResist;
    private short mRads, mMaxRads, mH2o, mMaxH2o, mFod, mMaxFod, mSlp, mMaxSlp, mWeight, mMaxWeight;
    private short mKarma;
    private float mDR, mDT;
    private int mCaps;
    private String mPlayerName;
    private float mX, mY, mZ, mRotX, mRotY, mRotZ;

    public SetPlayerInfoPacket(short level, short hp, short maxHP, short ap, short maxAP, int xp,
                               int maxXP, byte head, byte torso, byte leftArm, byte rightArm,
                               byte leftLeg, byte rightLeg,byte radResist, short rads,
                               short maxRads, short h2o, short maxH2o, short fod, short maxFod,
                               short slp, short maxSlp, short weight, short maxWeight, float dr,
                               float dt, int caps, short karma, String playerName, float x,
                               float y, float z, float rotX, float rotY, float rotZ) {

        mLevel = level;
        mHP = hp;
        mMaxHP = maxHP;
        mAP = ap;
        mMaxAP = maxAP;
        mXP = xp;
        mMaxXP = maxXP;
        mHead = head;
        mTorso = torso;
        mLeftArm = leftArm;
        mRightArm = rightArm;
        mLeftLeg = leftLeg;
        mRightLeg = rightLeg;
        mRadResist = radResist;
        mRads = rads;
        mMaxRads = maxRads;
        mH2o = h2o;
        mMaxH2o = maxH2o;
        mFod = fod;
        mMaxFod = maxFod;
        mSlp = slp;
        mMaxSlp = maxSlp;
        mWeight = weight;
        mMaxWeight = maxWeight;
        mDR = dr;
        mDT = dt;
        mCaps = caps;
        mKarma = karma;
        mPlayerName = playerName;
        mX = x;
        mY = y;
        mZ = z;
        mRotX = rotX;
        mRotY = rotY;
        mRotZ = rotZ;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetPlayerInfoPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetPlayerInfoPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);

        mLevel = buffer.getShort();
        mHP = buffer.getShort();
        mMaxHP = buffer.getShort();
        mAP = buffer.getShort();
        mMaxAP = buffer.getShort();
        mXP = buffer.getInt();
        mMaxXP = buffer.getInt();
        mHead = buffer.get();
        mTorso = buffer.get();
        mLeftArm = buffer.get();
        mRightArm = buffer.get();
        mLeftLeg = buffer.get();
        mRightLeg = buffer.get();
        mRadResist = buffer.get();
        mRads = buffer.getShort();
        mMaxRads = buffer.getShort();
        mH2o = buffer.getShort();
        mMaxH2o = buffer.getShort();
        mFod = buffer.getShort();
        mMaxFod = buffer.getShort();
        mSlp = buffer.getShort();
        mMaxSlp = buffer.getShort();
        mWeight = buffer.getShort();
        mMaxWeight = buffer.getShort();
        mDR = buffer.getFloat();
        mDT = buffer.getFloat();
        mCaps = buffer.getInt();
        mKarma = buffer.getShort();

        int strLen = buffer.getShort();
        Charset charset = Charset.forName("ISO-8859-1");
        assert(strLen <= len - 61);
        mPlayerName = new String(data, buffer.position(), strLen, charset);
        buffer.position(buffer.position() + strLen);
        mX = buffer.getFloat();
        mY = buffer.getFloat();
        mZ = buffer.getFloat();
        mRotX = buffer.getFloat();
        mRotY = buffer.getFloat();
        mRotZ = buffer.getFloat();
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 61 + mPlayerName.length() + 24;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.putShort(mLevel);
        buffer.putShort(mHP);
        buffer.putShort(mMaxHP);
        buffer.putShort(mAP);
        buffer.putShort(mMaxAP);
        buffer.putInt(mXP);
        buffer.putInt(mMaxXP);
        buffer.put(mHead);
        buffer.put(mTorso);
        buffer.put(mLeftArm);
        buffer.put(mRightArm);
        buffer.put(mLeftLeg);
        buffer.put(mRightLeg);
        buffer.put(mRadResist);
        buffer.putShort(mRads);
        buffer.putShort(mMaxRads);
        buffer.putShort(mH2o);
        buffer.putShort(mMaxH2o);
        buffer.putShort(mFod);
        buffer.putShort(mMaxFod);
        buffer.putShort(mSlp);
        buffer.putShort(mMaxSlp);
        buffer.putShort(mWeight);
        buffer.putShort(mMaxWeight);
        buffer.putFloat(mDR);
        buffer.putFloat(mDT);
        buffer.putInt(mCaps);
        buffer.putShort(mKarma);
        buffer.putShort((short)mPlayerName.length());

        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        enc.encode(CharBuffer.wrap(mPlayerName), buffer, true);
        buffer.putFloat(mX);
        buffer.putFloat(mY);
        buffer.putFloat(mZ);
        buffer.putFloat(mRotX);
        buffer.putFloat(mRotY);
        buffer.putFloat(mRotZ);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetPlayerInfoPacket{Name=\"" + mPlayerName + "\", ...}";
    }

    public short getLevel() {
        return mLevel;
    }

    public void setLevel(short level) {
        mLevel = level;
    }

    public short getHP() {
        return mHP;
    }

    public void setHP(short HP) {
        mHP = HP;
    }

    public short getMaxHP() {
        return mMaxHP;
    }

    public void setMaxHP(short maxHP) {
        mMaxHP = maxHP;
    }

    public short getAP() {
        return mAP;
    }

    public void setAP(short AP) {
        mAP = AP;
    }

    public short getMaxAP() {
        return mMaxAP;
    }

    public void setMaxAP(short maxAP) {
        mMaxAP = maxAP;
    }

    public int getXP() {
        return mXP;
    }

    public void setXP(int XP) {
        mXP = XP;
    }

    public int getMaxXP() {
        return mMaxXP;
    }

    public void setMaxXP(int maxXP) {
        mMaxXP = maxXP;
    }

    public byte getHead() {
        return mHead;
    }

    public void setHead(byte head) {
        mHead = head;
    }

    public byte getTorso() {
        return mTorso;
    }

    public void setTorso(byte torso) {
        mTorso = torso;
    }

    public byte getLeftArm() {
        return mLeftArm;
    }

    public void setLeftArm(byte leftArm) {
        mLeftArm = leftArm;
    }

    public byte getRightArm() {
        return mRightArm;
    }

    public void setRightArm(byte rightArm) {
        mRightArm = rightArm;
    }

    public byte getLeftLeg() {
        return mLeftLeg;
    }

    public void setLeftLeg(byte leftLeg) {
        mLeftLeg = leftLeg;
    }

    public byte getRightLeg() {
        return mRightLeg;
    }

    public void setRightLeg(byte rightLeg) {
        mRightLeg = rightLeg;
    }

    public byte getRadResist() {
        return mRadResist;
    }

    public void setRadResist(byte radResist) {
        mRadResist = radResist;
    }

    public short getRads() {
        return mRads;
    }

    public void setRads(short rads) {
        mRads = rads;
    }

    public short getMaxRads() {
        return mMaxRads;
    }

    public void setMaxRads(short maxRads) {
        mMaxRads = maxRads;
    }

    public short getH2o() {
        return mH2o;
    }

    public void setH2o(short h2o) {
        mH2o = h2o;
    }

    public short getMaxH2o() {
        return mMaxH2o;
    }

    public void setMaxH2o(short maxH2o) {
        mMaxH2o = maxH2o;
    }

    public short getFod() {
        return mFod;
    }

    public void setFod(short fod) {
        mFod = fod;
    }

    public short getMaxFod() {
        return mMaxFod;
    }

    public void setMaxFod(short maxFod) {
        mMaxFod = maxFod;
    }

    public short getSlp() {
        return mSlp;
    }

    public void setSlp(short slp) {
        mSlp = slp;
    }

    public short getMaxSlp() {
        return mMaxSlp;
    }

    public void setMaxSlp(short maxSlp) {
        mMaxSlp = maxSlp;
    }

    public short getWeight() {
        return mWeight;
    }

    public void setWeight(short weight) {
        mWeight = weight;
    }

    public short getMaxWeight() {
        return mMaxWeight;
    }

    public void setMaxWeight(short maxWeight) {
        mMaxWeight = maxWeight;
    }

    public float getDR() {
        return mDR;
    }

    public void setDR(float DR) {
        mDR = DR;
    }

    public float getDT() {
        return mDT;
    }

    public void setDT(float DT) {
        mDT = DT;
    }

    public int getCaps() {
        return mCaps;
    }

    public void setCaps(int caps) {
        mCaps = caps;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String playerName) {
        mPlayerName = playerName;
    }

    public short getKarma() {
        return mKarma;
    }

    public void setKarma(short karma) {
        mKarma = karma;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getZ() {
        return mZ;
    }

    public void setZ(float z) {
        mZ = z;
    }

    public float getRotX() {
        return mRotX;
    }

    public void setRotX(float rotX) {
        mRotX = rotX;
    }

    public float getRotY() {
        return mRotY;
    }

    public void setRotY(float rotY) {
        mRotY = rotY;
    }

    public float getRotZ() {
        return mRotZ;
    }

    public void setRotZ(float rotZ) {
        mRotZ = rotZ;
    }
}
