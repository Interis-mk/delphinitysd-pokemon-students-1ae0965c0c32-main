package trainer;

import area.Area;
import jakarta.persistence.*;

@Entity
public class GymLeader extends Trainer {

    private boolean isDefeated;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Badge badge;

    public GymLeader() {
        super();
        this.badge = null;
    }

    public GymLeader(String name, Badge badge, Area area) {
        super(name, area);
        this.badge = badge;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public Badge getBadge() {
        return badge;
    }
}
