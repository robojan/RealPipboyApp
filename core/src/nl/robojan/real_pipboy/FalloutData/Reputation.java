package nl.robojan.real_pipboy.FalloutData;

import nl.robojan.real_pipboy.util.Objects;

/**
 * Created by s120330 on 12-7-2015.
 */
public class Reputation {
    public String faction;
    public String reputation;
    public String icon;

    public Reputation(String faction, String reputation){
        this.faction = faction;
        this.reputation = reputation;
        this.icon = "textures/interface/shared/missing_image.dds";
    }

    public Reputation(String faction, String reputation, String icon){
        this.faction = faction;
        this.reputation = reputation;
        this.icon = icon;
    }

    public boolean equivalent(Object obj) {
        if(obj == null || !(obj instanceof Reputation))
            return false;
        Reputation other = (Reputation)obj;

        return Objects.equals(this.faction, other.faction) &&
                Objects.equals(this.reputation, other.reputation) &&
                Objects.equals(this.icon, other.icon);
    }

}
