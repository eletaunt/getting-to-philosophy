package philosophy.data.models;

import javax.persistence.*;

@Entity
public class PathNode {
    @Id
    @Column(name = "PathNodeId")
    @GeneratedValue
    private Integer id;

    @Column(name = "Title")
    private String title;

    @Column(name = "URL")
    private String url;

    public PathNode(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
