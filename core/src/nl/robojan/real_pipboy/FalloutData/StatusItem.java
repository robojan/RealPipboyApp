package nl.robojan.real_pipboy.FalloutData;

import java.util.Objects;

/**
 * Created by s120330 on 11-7-2015.
 */
public class StatusItem {
    public String name = null;
    public String level = null;
    public String extra = null;
    public String icon = null;
    public String badge = null;
    public String description = null;

    public StatusItem(String name, String level, String extra, String icon, String badge,
                      String description) {
        this.name = name;
        this.level = level;
        this.extra = extra;
        this.icon = icon;
        this.badge = badge;
        this.description = description;
    }

    public StatusItem(String name, String level, String extra, String icon,
                      String description) {
        this.name = name;
        this.level = level;
        this.extra = extra;
        this.icon = icon;
        this.description = description;
    }

    public StatusItem(String name, String level, String icon,
                      String description) {
        this.name = name;
        this.level = level;
        this.icon = icon;
        this.description = description;
    }

    public StatusItem(String name, String icon,
                      String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public StatusItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean equivalent(Object obj) {
        if(obj == null || obj.getClass() != StatusItem.class)
            return false;
        StatusItem other = (StatusItem)obj;

        return Objects.equals(this.name, other.name) &&
                Objects.equals(this.level, other.level) &&
                Objects.equals(this.extra, other.extra) &&
                Objects.equals(this.icon, other.icon) &&
                Objects.equals(this.badge, other.badge) &&
                Objects.equals(this.description, other.description);
    }
}
