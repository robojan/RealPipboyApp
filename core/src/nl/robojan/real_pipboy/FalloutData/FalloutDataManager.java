package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.GregorianCalendar;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.IPacketHandler;
import nl.robojan.real_pipboy.Connection.Packets.*;
import nl.robojan.real_pipboy.Context;

/**
 * Created by s120330 on 19-7-2015.
 */
public class FalloutDataManager implements IFalloutData, IPacketHandler {
    private String mPlayerName, mCurrentLocation;
    private int mPlayerLevel, mHP, mMaxHP, mAP, mMaxAP, mXP, mMaxXP, mRads, mMaxRads, mH2o, mMaxH2o;
    private int mFod, mMaxFod, mSlp, mMaxSlp, mCaps, mWeight, mMaxWeight, mRadResist;
    private float mHeadCondition, mTorsoCondition, mLeftArmCondition, mRightArmCondition;
    private float mLeftLegCondition, mRightLegCondition, mDR, mDT;
    private GregorianCalendar mDate;
    private StatusList mSpecial,  mSkills, mPerks, mStats;
    private boolean mHardcore;
    private int mGameVersion;
    private Array<Effect> mRadEffects, mH2oEffects, mFodEffects, mSlpEffects;
    private StatusEffects mPlayerEffects;
    private Reputations mReputations;
    private Karma.KarmaLevel mPlayerKarma;
    private ItemsList mItems;
    private NoteList mNotes;
    private QuestList mQuests;
    private Vector3 mPlayerPos, mPlayerRot;
    private Vector2 mUsableWorldMapDimensions, mCellNW, mCellSE, mWorldMapOffset;
    private float mWorldMapScale;
    private String mWorldMapTexture;
    private MapMarkerList mMapMarkers;

    public FalloutDataManager() {
        mPlayerName = "Player name";
        mCurrentLocation = "Some location";
        mDate = new GregorianCalendar();
        mSpecial = new StatusList();
        mSkills = new StatusList();
        mPerks = new StatusList();
        mStats = new StatusList();
        mRadEffects = new Array<Effect>(0);
        mH2oEffects = new Array<Effect>(0);
        mFodEffects = new Array<Effect>(0);
        mSlpEffects = new Array<Effect>(0);
        mPlayerEffects = new StatusEffects();
        mReputations = new Reputations();
        mPlayerKarma = Karma.KarmaLevel.NEUTRAL;
        mItems = new ItemsList();
        mNotes = new NoteList();
        mQuests = new QuestList();
        mPlayerPos = new Vector3();
        mPlayerRot = new Vector3();
        mUsableWorldMapDimensions = new Vector2();
        mCellNW = new Vector2();
        mCellSE = new Vector2();
        mWorldMapOffset = new Vector2();
        mWorldMapScale = 1;
        mWorldMapTexture = "";
        mMapMarkers = new MapMarkerList();

        ConnectionManager conn = ConnectionManager.getInstance();
        PacketTypes pt = PacketTypes.getInstance();
        conn.registerPacketHandler(pt.getType(SetPlayerInfoPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetWorldInfoPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetStatsPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetGameInfoPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetPlayerEffectsPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetInventoryPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetNotesPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetQuestsPacket.class), this);
        conn.registerPacketHandler(pt.getType(SetMapMarkersPacket.class), this);
    }

    @Override
    public void dispose() {
        ConnectionManager conn = ConnectionManager.getInstance();
        PacketTypes pt = PacketTypes.getInstance();
        conn.deregisterPacketHandler(pt.getType(SetPlayerInfoPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetWorldInfoPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetStatsPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetGameInfoPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetPlayerEffectsPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetInventoryPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetNotesPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetQuestsPacket.class), this);
        conn.deregisterPacketHandler(pt.getType(SetMapMarkersPacket.class), this);
    }

    @Override
    public void HandlePacket(DataPacket packet) {
        // Handle received packed
        if(packet instanceof SetPlayerInfoPacket) {
            setPlayerInfo((SetPlayerInfoPacket)packet);
        } else if (packet instanceof SetWorldInfoPacket) {
            setWorldInfo((SetWorldInfoPacket)packet);
        } else if (packet instanceof SetStatsPacket) {
            setStats((SetStatsPacket) packet);
        } else if (packet instanceof SetGameInfoPacket) {
            setGameInfo((SetGameInfoPacket) packet);
        } else if (packet instanceof SetPlayerEffectsPacket) {
            setPlayerEffects((SetPlayerEffectsPacket) packet);
        } else if (packet instanceof SetInventoryPacket) {
            setInventory((SetInventoryPacket)packet);
        } else if (packet instanceof SetNotesPacket) {
            setNotes((SetNotesPacket)packet);
        } else if (packet instanceof SetQuestsPacket) {
            setQuests((SetQuestsPacket) packet);
        } else if (packet instanceof SetMapMarkersPacket) {
            setMapMarkers((SetMapMarkersPacket)packet);
        } else {
            Gdx.app.error("DATA", "Unknown packet received: " + packet.toString());
        }
    }

    @Override
    public void update(Context context) {
        ConnectionManager conn = ConnectionManager.getInstance();
        if(conn.getTCPStatus() != ConnectionManager.TCPStatus.Connected) {
            return;
        }
    }

    private void setPlayerInfo(SetPlayerInfoPacket packet) {
        mPlayerName = packet.getPlayerName();
        mPlayerLevel = packet.getLevel();
        mHP = packet.getHP();
        mMaxHP = packet.getMaxHP();
        mAP = packet.getAP();
        mMaxAP = packet.getMaxAP();
        mXP = packet.getXP();
        mMaxXP = packet.getMaxXP();
        mRads = packet.getRads();
        mMaxRads = packet.getMaxRads();
        mH2o = packet.getH2o();
        mMaxH2o = packet.getMaxH2o();
        mFod = packet.getFod();
        mMaxFod = packet.getMaxFod();
        mSlp = packet.getSlp();
        mMaxSlp = packet.getMaxSlp();
        mCaps = packet.getCaps();
        mWeight = packet.getWeight();
        mMaxWeight = packet.getMaxWeight();
        mHeadCondition = packet.getHead()/100.0f;
        mTorsoCondition = packet.getTorso()/100.0f;
        mLeftArmCondition = packet.getLeftArm()/100.0f;
        mRightArmCondition = packet.getRightArm()/100.0f;
        mLeftLegCondition = packet.getLeftLeg()/100.0f;
        mRightLegCondition = packet.getRightLeg()/100.0f;
        mRadResist = packet.getRadResist();
        mDR = packet.getDR();
        mDT = packet.getDT();

        mPlayerKarma = Karma.getKarmaLevel(packet.getKarma());
        mPlayerPos = new Vector3(packet.getX(), packet.getY(), packet.getZ());
        mPlayerRot = new Vector3(packet.getRotX(), packet.getRotY(), packet.getRotZ());
    }

    private void setWorldInfo(SetWorldInfoPacket packet) {
        mCurrentLocation = packet.getLocationName();
        mDate = packet.getCalendar();
        mUsableWorldMapDimensions = new Vector2(packet.getUsableWidth(), packet.getUsableHeight());
        mCellNW = new Vector2(packet.getCellNWX(), packet.getCellNWY());
        mCellSE = new Vector2(packet.getCellSEX(), packet.getCellSEY());
        mWorldMapScale = packet.getMapScale();
        mWorldMapOffset = new Vector2(packet.getMapOffsetX(), packet.getMapOffsetY());
        mWorldMapTexture = packet.getMapPath();
    }

    private void setStats(SetStatsPacket packet) {
        switch(packet.getStatType()) {
            case 0: // Special
                mSpecial = packet.getStats();
                break;
            case 1: // Skills
                mSkills = packet.getStats();
                break;
            case 2: // Perks
                mPerks = packet.getStats();
                break;
            case 3: // Statistics
                mStats = packet.getStats();
                break;
            default:
                Gdx.app.error("DATA", "Unknown statistics packet type received: " +
                        packet.getStatType());
                break;
        }
    }

    private void setGameInfo(SetGameInfoPacket packet) {
        mHardcore = packet.isHardcore();
        mGameVersion = packet.getGameVersion();
    }

    private void setPlayerEffects(SetPlayerEffectsPacket packet) {
        mRadEffects = packet.getRadEffects();
        mH2oEffects = packet.getH2oEffects();
        mFodEffects = packet.getFodEffects();
        mSlpEffects = packet.getSlpEffects();
        mPlayerEffects = packet.getPlayerEffects();
    }

    private void setInventory(SetInventoryPacket packet) {
        mItems = packet.getItems();
    }

    private void setNotes(SetNotesPacket packet) {
        mNotes = packet.getNotes();
    }

    private void setQuests(SetQuestsPacket packet) {
        mQuests = packet.getQuests();
    }

    private void setMapMarkers(SetMapMarkersPacket packet) {
        mMapMarkers = packet.getMarkers();
    }

    @Override
    public int getLevel() {
        return mPlayerLevel;
    }

    @Override
    public int getMaxHP() {
        return mMaxHP;
    }

    @Override
    public int getHP() {
        return mHP;
    }

    @Override
    public int getMaxAP() {
        return mMaxAP;
    }

    @Override
    public int getAP() {
        return mAP;
    }

    @Override
    public int getNextLevelXP() {
        return mMaxXP;
    }

    @Override
    public int getXP() {
        return mXP;
    }

    @Override
    public String getPlayerName() {
        return mPlayerName;
    }

    @Override
    public float getLimbStatus(Limb limb) {
        switch(limb)
        {
            case HEAD:
                return mHeadCondition;
            case TORSO:
                return mTorsoCondition;
            case LEFT_ARM:
                return mLeftArmCondition;
            case RIGHT_ARM:
                return mRightArmCondition;
            case LEFT_LEG:
                return mLeftLegCondition;
            case RIGHT_LEG:
                return mRightLegCondition;
        }
        return 1.0f;
    }

    @Override
    public int getMaxRads() {
        return mMaxRads;
    }

    @Override
    public int getRads() {
        return mRads;
    }

    @Override
    public int getRadResist() {
        return mRadResist;
    }

    @Override
    public Array<Effect> getRadEffects() {
        return mRadEffects;
    }

    @Override
    public int getMaxDehydration() {
        return mMaxH2o;
    }

    @Override
    public int getDehydration() {
        return mH2o;
    }

    @Override
    public Array<Effect> getDehydrationEffects() {
        return mH2oEffects;
    }

    @Override
    public int getMaxHunger() {
        return mMaxFod;
    }

    @Override
    public int getHunger() {
        return mFod;
    }

    @Override
    public Array<Effect> getHungerEffects() {
        return mFodEffects;
    }

    @Override
    public int getMaxSleep() {
        return mMaxSlp;
    }

    @Override
    public int getSleep() {
        return mSlp;
    }

    @Override
    public Array<Effect> getSleepEffects() {
        return mSlpEffects;
    }

    @Override
    public StatusEffects getStatusEffects() {
        return mPlayerEffects;
    }

    @Override
    public boolean isHardcore() {
        return mHardcore;
    }

    @Override
    public StatusList getSPECIALList() {
        return mSpecial;
    }

    @Override
    public StatusList getSkillsList() {
        return mSkills;
    }

    @Override
    public StatusList getPerksList() {
        return mPerks;
    }

    @Override
    public StatusList getStatistics() {
        return mStats;
    }

    @Override
    public Reputations getReputations() {
        return mReputations;
    }

    @Override
    public Karma.KarmaLevel getKarmaLevel() {
        return mPlayerKarma;
    }

    @Override
    public float getMaxWeight() {
        return mMaxWeight;
    }

    @Override
    public float getWeight() {
        return mWeight;
    }

    @Override
    public float getDamageResistance() {
        return mDR;
    }

    @Override
    public float getDamageThreshold() {
        return mDT;
    }

    @Override
    public int getCaps() {
        return mCaps;
    }

    @Override
    public ItemsList getItems() {
        return mItems;
    }

    @Override
    public String getLocationName() {
        return mCurrentLocation;
    }

    @Override
    public GregorianCalendar getDateTime() {
        return mDate;
    }

    @Override
    public NoteList getNotes() {
        return mNotes;
    }

    @Override
    public QuestList getQuests() {
        return mQuests;
    }

    @Override
    public float getWorldMapScale() {
        return mWorldMapScale;
    }

    @Override
    public Vector2 getWorldMapOffset() {
        return mWorldMapOffset;
    }

    @Override
    public Vector2 getWorldMapCellSE() {
        return mCellSE;
    }

    @Override
    public Vector2 getWorldMapCellNW() {
        return mCellNW;
    }

    @Override
    public Vector2 getWorldMapUsableDim() {
        return mUsableWorldMapDimensions;
    }

    @Override
    public String getWorldMapPath() {
        return mWorldMapTexture;
    }

    @Override
    public Vector3 getPlayerRot() {
        return mPlayerRot;
    }

    @Override
    public Vector3 getPlayerPos() {
        return mPlayerPos;
    }

    @Override
    public MapMarkerList getMapMarkers() {
        return mMapMarkers;
    }
}
