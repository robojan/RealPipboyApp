package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.GregorianCalendar;

import nl.robojan.real_pipboy.Context;

/**
 * Created by s120330 on 19-7-2015.
 */
public interface IFalloutData extends Disposable{
    public enum Limb {
        HEAD,
        TORSO,
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG
    }

    void update(Context context);

    int getLevel();
    int getMaxHP();
    int getHP();
    int getMaxAP();
    int getAP();
    int getNextLevelXP();
    int getXP();
    String getPlayerName();
    float getLimbStatus(Limb limb);
    int getMaxRads();
    int getRads();
    int getRadResist();
    Array<Effect> getRadEffects();
    int getMaxDehydration();
    int getDehydration();
    Array<Effect> getDehydrationEffects();
    int getMaxHunger();
    int getHunger();
    Array<Effect> getHungerEffects();
    int getMaxSleep();
    int getSleep();
    Array<Effect> getSleepEffects();
    StatusEffects getStatusEffects();
    boolean isHardcore();
    StatusList getSPECIALList();
    StatusList getSkillsList();
    StatusList getPerksList();
    StatusList getStatistics();
    Reputations getReputations();
    Karma.KarmaLevel getKarmaLevel();
    float getMaxWeight();
    float getWeight();
    float getDamageResistance();
    float getDamageThreshold();
    int getCaps();
    ItemsList getItems();
    String getLocationName();
    GregorianCalendar getDateTime();
    NoteList getNotes();
    QuestList getQuests();
    Vector3 getPlayerPos();
    Vector3 getPlayerRot();
    String getWorldMapPath();
    Vector2 getWorldMapUsableDim();
    Vector2 getWorldMapCellNW();
    Vector2 getWorldMapCellSE();
    Vector2 getWorldMapOffset();
    float getWorldMapScale();
}
