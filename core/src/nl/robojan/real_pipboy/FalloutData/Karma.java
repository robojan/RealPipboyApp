package nl.robojan.real_pipboy.FalloutData;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Karma {
    public enum KarmaLevel {
        VERY_GOOD,
        GOOD,
        NEUTRAL,
        EVIL,
        VERY_EVIL
    };

    public static KarmaLevel getKarmaLevel(int value) {
        if(value < -750) {
            return KarmaLevel.VERY_EVIL;
        } else if(value < -250) {
            return KarmaLevel.EVIL;
        } else if(value < 250) {
            return KarmaLevel.NEUTRAL;
        } else if(value < 750) {
            return KarmaLevel.GOOD;
        } else {
            return KarmaLevel.VERY_GOOD;
        }
    }

    public static String getKarmaName(KarmaLevel level) {
        switch(level) {
            case VERY_GOOD:
                return "Very good";
            case GOOD:
                return "Good";
            case NEUTRAL:
                return "Neutral";
            case EVIL:
                return "Evil";
            case VERY_EVIL:
                return "Very evil";
        }
        return "Error";
    }

    public static String getKarmaIcon(KarmaLevel level) {
        switch(level) {
            case VERY_GOOD:
                return "textures/interface/icons/pipboyimages/karma icons/karma_saintly.dds";
            case GOOD:
                return "textures/interface/icons/pipboyimages/karma icons/karma_good.dds";
            case NEUTRAL:
                return "textures/interface/icons/pipboyimages/karma icons/karma_neutral.dds";
            case EVIL:
                return "textures/interface/icons/pipboyimages/karma icons/karma_bad.dds";
            case VERY_EVIL:
                return "textures/interface/icons/pipboyimages/karma icons/karma_evil.dds";
        }
        return "textures/interface/shared/missing_image.dds";
    }

    public static String getKarmaTitle(KarmaLevel level, int playerLevel) {
        int karmaIndex = 1;
        if(level == KarmaLevel.EVIL || level == KarmaLevel.VERY_EVIL) {
            karmaIndex = 2;
        } else if(level == KarmaLevel.GOOD || level == KarmaLevel.VERY_GOOD) {
            karmaIndex = 0;
        }
        if(playerLevel < 1) // for whatever reason
            playerLevel = 1;
        if(playerLevel > 30)
            playerLevel = 30;
        playerLevel--;
        return KARMATITLESNV[playerLevel][karmaIndex];
    }

    private static final String[][] KARMATITLESNV = new String[][]{
            {"Samaritan", "Drifter", "Grifter"}, // Level 1
            {"Martyr", "Renegade", "Outlaw"},
            {"Sentinel", "Seeker", "Opportunist"},
            {"Defender", "Wanderer", "Plunderer"},
            {"Dignitary", "Citizen", "Fat Cat"}, // Level 5
            {"Peacekeeper", "Adventurer", "Marauder"},
            {"Ranger of the Wastes", "Vagabond of the Wastes", "Pirate of the Wastes"},
            {"Protector", "Mercenary", "Betrayer"},
            {"Desert Avenger", "Desert Scavenger", "Desert Terror"},
            {"Exemplar", "Observer", "Ne'er-do-well"}, // Level 10
            {"Vegas Crusader", "Vegas Councilor", "Vegas Crime lord"},
            {"Paladin", "Keeper", "Defiler"},
            {"Mojave Legend", "Mojave Myth", "Mojave Boogeyman"},
            {"Shield of Hope", "Pinnacle of Survival", "Sword of Despair"},
            {"Vegas Legend", "Vegas Myth", "Vegas Boogeyman"},// Level 15
            {"Hero of the Wastes", "Strider of the Wastes", "Villain of the Wastes"},
            {"Paragon", "Beholder", "Fiend"},
            {"Wasteland Savior", "Wasteland Watcher", "Wasteland Destroyer"},
            {"Saint", "Super-Human", "Evil Incarnate"},
            {"Guardian of the Wastes", "Renegade of the Wastes", "Scourge of the Wastes"},// Level 20
            {"Restorer of Faith", "Soldier of Fortune", "Architect of Doom"},
            {"Model of Selflessness", "Profiteer", "Bringer of Sorrow"},
            {"Shepherd", "Egocentric", "Deceiver"},
            {"Friend of the People", "Loner", "Consort of Discord"},
            {"Champion of Justice", "Hero for Hire", "Stuff of Nightmares"},// Level 25
            {"Symbol of Order", "Model of Apathy", "Agent of Chaos"},
            {"Herald of Tranquillity", "Person of Refinement", "Instrument of Ruin"},
            {"Last, Best Hope of Humanity", "Moneygrubber", "Soultaker"},
            {"Savior of the Damned", "Gray Stranger", "Demon's Spawn"},
            {"Messiah", "True Mortal", "Devil"} // Level 30
    };
}
