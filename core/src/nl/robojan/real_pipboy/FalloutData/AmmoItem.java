package nl.robojan.real_pipboy.FalloutData;

/**
 * Created by s120330 on 13-7-2015.
 */
public class AmmoItem extends Item {

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon, String badge,
                    boolean equippable, boolean equipped, String effect) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped, effect);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon,
                    boolean equippable, boolean equipped, String effect) {
        super(id, name, amount, value, weight, icon, equippable, equipped, effect);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon,
                    String badge, boolean equippable, boolean equipped) {
        super(id, name, amount, value, weight, icon, badge, equippable, equipped);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon, String badge) {
        super(id, name, amount, value, weight, icon, badge);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon,
                    boolean equippable, boolean equipped) {
        super(id, name, amount, value, weight, icon, equippable, equipped);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight, String icon) {
        super(id, name, amount, value, weight, icon);
    }

    public AmmoItem(Long id, String name, int amount, int value, float weight) {
        super(id, name, amount, value, weight);
    }
}
