package philosophy.data.models;

import javax.persistence.*;

@Entity
@Table(name = "Path")
public class Path {
    @Id
    @Column(name = "PathId")
    @GeneratedValue
    private Integer id;

    @Column(name = "Hops")
    private Integer hops;

    @Column(name = "InitialURL")
    private String initialURL;

    public Path(String initialURL, Integer hops) {
        this.initialURL = initialURL;
        this.hops = hops;
    }

    public Integer getId() {
        return id;
    }

    public String getInitialURL() { return initialURL; }

    public Integer getHops() {
        return hops;
    }
}
