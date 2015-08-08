package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.math.Vector3;

import java.util.Objects;

/**
 * Created by s120330 on 5-8-2015.
 */
public class MapMarker {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_CITY = 1;
    public static final int TYPE_SETTLEMENT = 2;
    public static final int TYPE_ENCAMPMENT = 3;
    public static final int TYPE_NATURALLANDMARK = 4;
    public static final int TYPE_CAVE = 5;
    public static final int TYPE_FACTORY = 6;
    public static final int TYPE_MEMORIAL = 7;
    public static final int TYPE_MILITARY = 8;
    public static final int TYPE_OFFICE = 9;
    public static final int TYPE_TOWNRUINS = 10;
    public static final int TYPE_URBANRUINS = 11;
    public static final int TYPE_SEWERRUINS = 12;
    public static final int TYPE_METRO = 13;
    public static final int TYPE_VAULT = 14;

    public static final int FLAG_VISIBLE = 0x1;
    public static final int FLAG_CANTRAVEL = 0x2;
    public static final int FLAG_HIDDEN = 0x4;

    private Vector3 mPos;
    private String mName;
    private String mReputation;
    private short mType;
    private short mFlags;
    private int mId;

    public MapMarker(int id, Vector3 pos, String name, String reputation, short type, short flags) {
        mPos = pos;
        mName = name;
        mReputation = reputation;
        mType = type;
        mFlags = flags;
        mId = id;
    }

    public String getIcon(boolean inverted) {
        if(inverted) {
            switch (mType) {
                case TYPE_NONE: return "textures/interface/icons/world map/inverted/icon_map_undiscovered_inverted.dds";
                case TYPE_CITY: return "textures/interface/icons/world map/inverted/icon_map_city_inverted.dds";
                case TYPE_SETTLEMENT: return "textures/interface/icons/world map/inverted/icon_map_city_inverted.dds";
                case TYPE_ENCAMPMENT: return "textures/interface/icons/world map/inverted/icon_map_encampment_inverted.dds";
                case TYPE_NATURALLANDMARK: return "textures/interface/icons/world map/inverted/icon_map_natural_landmark_inverted.dds";
                case TYPE_CAVE: return "textures/interface/icons/world map/inverted/icon_map_cave_inverted.dds";
                case TYPE_FACTORY: return "textures/interface/icons/world map/inverted/icon_map_factory_inverted.dds";
                case TYPE_MEMORIAL: return "textures/interface/icons/world map/inverted/icon_map_monument_inverted.dds";
                case TYPE_MILITARY: return "textures/interface/icons/world map/inverted/icon_map_military_inverted.dds";
                case TYPE_OFFICE: return "textures/interface/icons/world map/inverted/icon_map_office_inverted.dds";
                case TYPE_TOWNRUINS: return "textures/interface/icons/world map/inverted/icon_map_ruins_town_inverted.dds";
                case TYPE_URBANRUINS: return "textures/interface/icons/world map/inverted/icon_map_ruins_urban_inverted.dds";
                case TYPE_SEWERRUINS: return "textures/interface/icons/world map/inverted/icon_map_ruins_sewer_inverted.dds";
                case TYPE_METRO: return "textures/interface/icons/world map/inverted/icon_map_metro_inverted.dds";
                case TYPE_VAULT: return "textures/interface/icons/world map/inverted/icon_map_vault_inverted.dds";
                default: return "textures/interface/shared/solid.dds";
            }
        } else {
            switch (mType) {
                case TYPE_NONE: return "textures/interface/icons/world map/icon_map_undiscovered.dds";
                case TYPE_CITY: return "textures/interface/icons/world map/icon_map_city.dds";
                case TYPE_SETTLEMENT: return "textures/interface/icons/world map/icon_map_city.dds";
                case TYPE_ENCAMPMENT: return "textures/interface/icons/world map/icon_map_encampment.dds";
                case TYPE_NATURALLANDMARK: return "textures/interface/icons/world map/icon_map_natural_landmark.dds";
                case TYPE_CAVE: return "textures/interface/icons/world map/icon_map_cave.dds";
                case TYPE_FACTORY: return "textures/interface/icons/world map/icon_map_factory.dds";
                case TYPE_MEMORIAL: return "textures/interface/icons/world map/icon_map_monument.dds";
                case TYPE_MILITARY: return "textures/interface/icons/world map/icon_map_military.dds";
                case TYPE_OFFICE: return "textures/interface/icons/world map/icon_map_office.dds";
                case TYPE_TOWNRUINS: return "textures/interface/icons/world map/icon_map_ruins_town.dds";
                case TYPE_URBANRUINS: return "textures/interface/icons/world map/icon_map_ruins_urban.dds";
                case TYPE_SEWERRUINS: return "textures/interface/icons/world map/icon_map_ruins_sewer.dds";
                case TYPE_METRO: return "textures/interface/icons/world map/icon_map_metro.dds";
                case TYPE_VAULT: return "textures/interface/icons/world map/icon_map_vault.dds";
                default: return "textures/interface/shared/solid.dds";
            }
        }
    }

    public String toString() {
        return "MapMarker{" + mName + ", Pos=" + mPos.toString() +"}";
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(MapMarker.class.isAssignableFrom(obj.getClass())))
            return false;
        MapMarker other = (MapMarker)obj;

        return Objects.equals(this.mName, other.mName) &&
                Objects.equals(this.mReputation, other.mReputation) &&
                Objects.equals(this.mPos, other.mPos) &&
                this.mType == other.mType &&
                this.mFlags == other.mFlags;
    }

    public Vector3 getPos() {
        return mPos;
    }

    public void setPos(Vector3 pos) {
        mPos = pos;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getReputation() {
        return mReputation;
    }

    public void setReputation(String reputation) {
        mReputation = reputation;
    }

    public short getType() {
        return mType;
    }

    public void setType(short type) {
        mType = type;
    }

    public short getFlags() {
        return mFlags;
    }

    public void setFlags(short flags) {
        mFlags = flags;
    }

    public boolean isVisible() {
        return (mFlags & FLAG_VISIBLE) != 0;
    }

    public void setVisible(boolean visible) {
        if(visible) {
            mFlags |= FLAG_VISIBLE;
        } else {
            mFlags &= ~FLAG_VISIBLE;
        }
    }

    public boolean canTravel() {
        return (mFlags & FLAG_CANTRAVEL) != 0;
    }

    public void setCanTravel(boolean canTravel) {
        if(canTravel) {
            mFlags |= FLAG_CANTRAVEL;
        } else {
            mFlags &= ~FLAG_CANTRAVEL;
        }
    }

    public boolean isHidden() {
        return (mFlags & FLAG_HIDDEN) != 0;
    }

    public void setHidden(boolean hidden) {
        if(hidden) {
            mFlags |= FLAG_HIDDEN;
        } else {
            mFlags &= ~FLAG_HIDDEN;
        }
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
