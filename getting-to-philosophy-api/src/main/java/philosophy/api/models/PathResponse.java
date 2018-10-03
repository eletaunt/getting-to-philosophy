package philosophy.api.models;

import java.util.ArrayList;

public class PathResponse {

    private int hops;
    private boolean foundPhilosophy;
    private String content;
    private ArrayList<PathResponseNode> pathNodes;

    public PathResponse(int hops, String content, boolean foundPhilosophy, ArrayList<PathResponseNode> path) {
        this.hops = hops;
        this.content = content;
        this.foundPhilosophy = foundPhilosophy;
        this.pathNodes = path;
    }

    public int getHops() {
        return hops;
    }

    public String getContent() {
        return content;
    }

    public boolean getFoundPhilosophy() {
        return foundPhilosophy;
    }

    public ArrayList<PathResponseNode> getPathNodes() {
        return pathNodes;
    }

    public void addPathNode(PathResponseNode node) {
        this.pathNodes.add(node);
    }
}
