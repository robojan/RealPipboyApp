package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.GregorianCalendar;

import nl.robojan.real_pipboy.Context;

/**
 * Created by s120330 on 6-7-2015.
 */
public class FalloutDataTest implements IFalloutData {

    @Override
    public void dispose() {

    }

    public void update(Context context) {

    }

    public int getLevel()
    {
        return 7;
    }

    public int getMaxHP()
    {
        return 200;
    }

    public int getHP()
    {
        return 200;
    }

    public int getMaxAP()
    {
        return 96;
    }

    public int getAP()
    {
        return 96;
    }

    public int getNextLevelXP()
    {
        return 200;
    }

    public int getXP()
    {
        return 10;
    }

    public String getPlayerName() { return "Player Name"; }

    public float getLimbStatus(Limb limb) {
        switch(limb)
        {
            case HEAD:
                return 0.0f;
            case TORSO:
                return 0.2f;
            case LEFT_ARM:
                return 0.5f;
            case RIGHT_ARM:
                return 1.0f;
            case LEFT_LEG:
                return 0.0f;
            case RIGHT_LEG:
                return 0.8f;
        }
        return 0.0f;
    }

    public int getMaxRads() {
        return 1000;
    }

    public int getRads() {
        return 247;
    }

    public int getRadResist() {
        return 12;
    }

    public Array<Effect> getRadEffects() {
        Array<Effect> effects = new Array<Effect>();
        effects.add(new Effect("STR", -1));
        effects.add(new Effect("END", -3));
        effects.add(new Effect("AGL", -2));
        effects.add(new Effect("RAD", +5));
        return effects;
    }

    public int getMaxDehydration() {
        return 1000;
    }

    public int getDehydration() {
        return 50;
    }

    public Array<Effect> getDehydrationEffects() {
        Array<Effect> effects = new Array<Effect>();
        effects.add(new Effect("STR", -1));
        effects.add(new Effect("END", -3));
        effects.add(new Effect("AGL", -2));
        effects.add(new Effect("H2O", +5));
        return effects;
    }

    public int getMaxHunger() {
        return 1000;
    }

    public int getHunger() {
        return 560;
    }

    public Array<Effect> getHungerEffects() {
        Array<Effect> effects = new Array<Effect>();
        effects.add(new Effect("STR", -1));
        effects.add(new Effect("END", -3));
        effects.add(new Effect("AGL", -2));
        effects.add(new Effect("FOD", +5));
        return effects;
    }

    public int getMaxSleep() {
        return 1000;
    }

    public int getSleep() {
        return 900;
    }

    public Array<Effect> getSleepEffects() {
        Array<Effect> effects = new Array<Effect>();
        effects.add(new Effect("STR", -1));
        effects.add(new Effect("END", -3));
        effects.add(new Effect("AGL", -2));
        effects.add(new Effect("SLP", +5));
        return effects;
    }

    public StatusEffects getStatusEffects() {
        StatusEffects statusEffects = new StatusEffects();
        Array<Effect> radEffects = getRadEffects();
        Array<Effect> slpEffects = getSleepEffects();
        Array<Effect> fodEffects = getHungerEffects();
        Array<Effect> h2oEffects = getDehydrationEffects();
        if(radEffects.size > 0){
            statusEffects.add(new EffectInfo("Deadly Rad Poison.",
                    radEffects));
        }
        if(slpEffects.size > 0) {
            statusEffects.add(new EffectInfo("Sleep deprivation",
                    slpEffects));
        }
        if(fodEffects.size > 0) {
            statusEffects.add(new EffectInfo("Hunger", fodEffects));
        }
        if(h2oEffects.size > 0) {
            statusEffects.add(new EffectInfo("Dehydration", h2oEffects));
        }
        return statusEffects;
    }

    public boolean isHardcore() {
        return false;
    }

    public StatusList getSPECIALList() {
        StatusList list = new StatusList();
        list.add(new StatusItem("Strength", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_strength.dds",
                "Strength is a measure of your raw physical power. It affects how much you " +
                "can carry, the power of all melee attacks, and your effectiveness with many " +
                "heavy weapons."));
        list.add(new StatusItem("Perception", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_perception.dds",
                "A high Perception grants a bonus to the Explosives, Lockpick and Energy Weapons " +
                "skills, and determines when red compass markings appear " +
                "(which indicate threats)."));
        list.add(new StatusItem("Endurance", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_endurance.dds",
                "Endurance is a measure of your overall physical fitness. A high Endurance gives " +
                "bonuses to health, environmental resistances, and the Survival and Unarmed " +
                "skills."));
        list.add(new StatusItem("Charisma", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_charisma.dds",
                "Having a high Charisma will improve people's disposition toward you, and" +
                "give bonuses to both the Barter and Speech skills"));
        list.add(new StatusItem("Intelligence", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_intelligence.dds",
                "Intelligence affects the Science, Repair and Medicine skills. The higher " +
                "your Intelligence, the more Skill points you'll be able to distribute when " +
                "you level up."));
        list.add(new StatusItem("Agility", "7", "(+)",
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_agility.dds",
                "Agility affects your Guns and Sneak skills, and the number of Action " +
                "Points available for V.A.T.S."));
        list.add(new StatusItem("Luck", "10", null,
                "textures/interface/icons/pipboyimages/s.p.e.c.i.a.l/special_luck.dds",
                "Raising your Luck will raise all of your skills a little. Having a high Luck " +
                "will also improve your critical chance with all weapons."));
        return list;
    }

    public StatusList getSkillsList() {
        StatusList list = new StatusList();
        list.add(new StatusItem("Barter", "21", "(+)",
                "textures/interface/icons/stats/skills_barter.dds",
                null,
                "The Barter skill affects the prices you get for buying and selling items. In " +
                "general, the higher your Barter skill, the lower your prices on purchased items."));
        list.add(new StatusItem("Energy Weapons", "21", "(+)",
                "textures/interface/icons/stats/skills_energy_weapons.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_energy.dds",
                "The Energy Weapons skill determines yout effectiveness with any weapon that uses" +
                " Small Energy Cells, Micro Fusion Cells, EC Packs, or Flamer Fuel as" +
                " ammunition."));
        list.add(new StatusItem("Explosives", "21", "(+)",
                "textures/interface/icons/stats/skills_explosives.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_explosives.dds",
                "Explosives skill determines the ease of disarming any hostile mines and the " +
                "effectiveness of any explosive weapon(all mines, all grenades, Missile Launcher," +
                " Fat Man, etc.)."));
        list.add(new StatusItem("Guns", "21", "(+)",
                "textures/interface/icons/stats/skills_small_guns.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_sm_arms.dds",
                "Guns determines your effectiveness with any weapon that uses conventional ammu" +
                "nition (.22 LR, .357 Magnum, 5mm, 10mm, 5.56mm, .308, .45-70 Gov't etc.)."));
        list.add(new StatusItem("Lockpick", "36", "(+)",
                "textures/interface/icons/stats/skills_lockpick.dds",
                null,
                "The Lockpick skill is used to open locked doors and containers."));
        list.add(new StatusItem("Medicine", "21", "(+)",
                "textures/interface/icons/stats/skills_medicine.dds",
                null,
                "The Medicine skill determines how many Hit Points you'll replenish upon using a " +
                "Stimpak, and the effectiveness of Rad-X and RadAway"));
        list.add(new StatusItem("Melee Weapons", "23", "(+)",
                "textures/interface/icons/stats/skills_melee_weapons.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_melee.dds",
                "The Melee Weapons skill determines your effectiveness with any melee weapon, " +
                "from the simple lead pipe all the way up to the high-tech Super Sledge."));
        list.add(new StatusItem("Repair", "21", "(+)",
                "textures/interface/icons/stats/skills_repair.dds",
                null,
                "The Repair skill allows you to maintain any weapons and apparel. In addition, " +
                "Repair allows you to create items and Guns ammunition at reloading benches."));
        list.add(new StatusItem("Science", "36", "(+)",
                "textures/interface/icons/stats/skills_science.dds",
                null,
                "The Science skill represents your combined scientific knowledge, and is prima" +
                "rily used to hack restricted computer terminals. It can also be used to recycle" +
                " Energy Weapon ammunition at workbenches."));
        list.add(new StatusItem("Sneak", "36", "(+)",
                "textures/interface/icons/stats/skills_sneak.dds",
                null,
                "The higher your Sneak skill, the easier it is to remain undetected, steal an " +
                "item, or pick someone's pocket. Successfully attacking while undetected grants" +
                " an automatic critical hit."));
        list.add(new StatusItem("Speech", "23", "(+)",
                "textures/interface/icons/stats/skills_speech.dds",
                null,
                "The Speech skill governs how much you can influence someone through dialogue, " +
                "and gain access to information they might otherwise not want to share."));
        list.add(new StatusItem("Survival", "21", "(+)",
                "textures/interface/icons/stats/skills_survival.dds",
                null,
                "The Survival skill increases the Hit Points you receive from food and drink. " +
                "It also helps you create consumable items at campfires."));
        list.add(new StatusItem("Unarmed", "36", "(+)",
                "textures/interface/icons/stats/skills_unarmed.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_unarmed.dds",
                "The Unarmed skill is used for fighting without a weapon, or with weapons " +
                "designed for hand-to-hand combat, like Brass Knuckles, Power Fists, and " +
                "Displace Gloves."));
        return list;
    }

    public StatusList getPerksList() {
        StatusList list = new StatusList();
        list.add(new StatusItem("Early Bird", null, null,
                "textures/interface/icons/pipboyimages/perks/perk_fast_metabolism.dds",
                null,
                "Hey early risers! Enjoy a +2 to each of your S.P.E.C.I.A.L. attributes from 6 am" +
                " to 12 pm, but suffer a -1 from 6 pm to 6 am when you're not at your best."));
        list.add(new StatusItem("Kamikaze", null, null,
                "textures/interface/icons/pipboyimages/perks/perk_toughness.dds",
                null,
                "You have +10 Action Points but your reckless nature causes you to have -2 " +
                "Damage Theshold."));
        return list;
    }

    public StatusList getStatistics() {
        StatusList stats = new StatusList();
        stats.add(new StatusItem("Quests Completed", "1", null, null));
        stats.add(new StatusItem("Locations Discovered", "1", null, null));
        stats.add(new StatusItem("People Killed", "0", null, null));
        stats.add(new StatusItem("Creatures Killed",  "0", null, null));
        stats.add(new StatusItem("Locks Picked",  "0", null, null));
        stats.add(new StatusItem("Computers Hacked",  "0", null, null));
        stats.add(new StatusItem("Stimpacks Taken",  "0", null, null));
        stats.add(new StatusItem("Rad-X Taken",  "0", null, null));
        stats.add(new StatusItem("RadAway Taken",  "0", null, null));
        stats.add(new StatusItem("Chems Taken",  "0", null, null));
        stats.add(new StatusItem("Times Addicted",  "0", null, null));
        stats.add(new StatusItem("Mines Disarmed",  "0", null, null));
        stats.add(new StatusItem("Speech Successes",  "0", null, null));
        stats.add(new StatusItem("Pockets Picked",  "0", null, null));
        stats.add(new StatusItem("Pants Exploded",  "0", null, null));
        stats.add(new StatusItem("Books Read",  "0", null, null));
        stats.add(new StatusItem("Health From Stimpaks",  "0", null, null));
        stats.add(new StatusItem("Weapons Created",  "0", null, null));
        return stats;
    }

    public Reputations getReputations() {
        Reputations result = new Reputations();
        result.add(new Reputation("Goodsprings", "Vilified",
                "textures/interface/icons/pipboyimages/reputations/reputations_goodsprings.dds"));
        result.add(new Reputation("Great Khans", "Saint",
                "textures/interface/icons/pipboyimages/reputations/reputations_great_khans.dds"));
        return result;
    }

    public Karma.KarmaLevel getKarmaLevel() {
        return Karma.KarmaLevel.NEUTRAL;
    }

    public float getMaxWeight() {
        return 220;
    }

    public float getWeight() {
        return 98.5f;
    }

    public float getDamageResistance() {
        return 5;
    }

    public float getDamageThreshold() {
        return -2;
    }

    public int getCaps() {
        return 26;
    }

    public ItemsList getItems() {
        ItemsList items = new ItemsList();
        items.add(new WeaponItem(0l, "9mm Pistol", 1, 16, 1.5f,
                "textures/interface/icons/pipboyimages/weapons/weapons_9mm_pistol.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_sm_arms.dds",
                true, true, 22, 7, 0.8f, 2));
        items.add(new WeaponItem(1l,"Binoculars", 1, 50, 1.5f,
                "textures/interface/icons/pipboyimages/items/items_binoculars.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_melee.dds",
                true, false, 0, 0, 1, 0));
        items.add(new WeaponItem(2l,"Boxing Gloves", 1, 6, 6.0f,
                "textures/interface/icons/pipboyimages/weapons/weapons_boxing_gloves.dds",
                "textures/interface/icons/typeicons/weap_skill_icon_unarmed.dds",
                false, false, 2, 2, 0.15f, 1, "Inflicts Fatigue Damage (KO)"));
        items.add(new ApparelItem(3l,"Armored Vault 13 Jumpsuit", 1, 70, 15,
                "textures/interface/icons/pipboyimages/apparel/apparel_armored_vault13_jumpsuit.dds",
                true, false, 0, 8, 1));
        items.add(new ApparelItem(4l,"Lightweight Metal Armor", 1, 460, 20,
                "textures/interface/icons/pipboyimages/apparel/apperal_metal_armor.dds",
                true, false, "AGL -1", 0, 12, 1));
        items.add(new AidItem(5l,"Bleak Venom", 5, 5, 2.5f,
                "textures/interface/icons/pipboyimages/items/item_bleak_venom.dds",
                true, false, "HP -15(10s)"));
        items.add(new AidItem(6l,"Docter's Bag", 3, 5, 2.5f,
                "textures/interface/icons/pipboyimages/items/item_doctors_bag.dds",
                true, false, "Restore All Body Parts"));
        items.add(new AidItem(7l,"Stimpak", 9, 150, 0,
                "textures/interface/icons/pipboyimages/items/items_stimpack.dds",
                true, false, "HP +42"));
        items.add(new MiscItem(8l,"Vault 13 Canteen", 1, 2, 1.00f));
        items.add(new AmmoItem(9l,"9mm Round", 21, 21, 0,
                "textures/interface/icons/pipboyimages/items/items_9mm_ammo.dds"));
        items.add(new AmmoItem(10l,"40mm Grenade", 21, 21, 0,
                "textures/interface/icons/pipboyimages/items/items_40mm.dds"));
        return items;
    }

    public String getLocationName() {
        return "Mojave Wasteland";
    }

    public GregorianCalendar getDateTime() {
        return new GregorianCalendar(2281, 10-1, 19, 8, 10);
        //return new GregorianCalendar();
    }

    @Override
    public NoteList getNotes() {
        NoteList list = new NoteList();
        list.add(new Note("Text note", 1, "This is a test note.\r\n It does not contain very " +
                "Usefull information, but it can be used!!!\r\n\r\n\r\n After a few line breaks\r\n" +
                "Hsu told me that he'd pay me for taking out Motor-Runner. I can find him at" +
                " Vault 3, and the NCR will pay well if I'm able to take him out. Hsu also "+
                "mentioned the last soldier he sent to take care of Motor-Runner has missed his"+
                " scheduled radio contact.", 0));
        list.add(new Note("Image note", 2, "textures/load_roulette_wheel.dds", 0));
        list.add(new Note("Audio note", 3, "UNK", 0));
        return list;
    }

    @Override
    public QuestList getQuests() {
        return new QuestList();
    }

    @Override
    public Vector3 getPlayerPos() {
        return new Vector3(0,0,0);
    }

    @Override
    public Vector3 getPlayerRot() {
        return new Vector3(0,0,0);
    }

    @Override
    public String getWorldMapPath() {
        return "textures/interface/worldmap/wasteland_nv_1024_no_map.dds";
    }

    @Override
    public Vector2 getWorldMapUsableDim() {
        return new Vector2(1024, 1024);
    }

    @Override
    public Vector2 getWorldMapCellNW() {
        return new Vector2(-35, 35);
    }

    @Override
    public Vector2 getWorldMapCellSE() {
        return new Vector2(25, -25);
    }

    @Override
    public Vector2 getWorldMapOffset() {
        return new Vector2(0,0);
    }

    @Override
    public float getWorldMapScale() {
        return 1;
    }

    @Override
    public MapMarkerList getMapMarkers() {
        return new MapMarkerList();
    }
}
