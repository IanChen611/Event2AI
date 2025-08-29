package model;

public class Frame {
    private String id;
    private String title;
    private int x;
    private int y;

    public Frame() {}

    public Frame(String id, String title, int x, int y) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    @Override
    public String toString() {
        return "Frame{id='" + id + "', title='" + title + "', x=" + x + ", y=" + y + "}";
    }
}
