package trainer;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "badge")
public class Badge implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String name;

    public Badge(String name) {
        this.name = name;
    }

    public Badge() {
        this.name = null;
    }

    public String getName() {
        return name;
    }

}
