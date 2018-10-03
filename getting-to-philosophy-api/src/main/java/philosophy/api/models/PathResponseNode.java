package philosophy.api.models;

public class PathResponseNode {
    private String title;

    private String url;

    public PathResponseNode(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
