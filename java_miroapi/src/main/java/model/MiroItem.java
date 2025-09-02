package model;

import com.google.gson.JsonObject;

public class MiroItem {
    private final String id;
    private final String type;
    private final JsonObject data;
    private final JsonObject style;
    private final JsonObject geometry;
    private final JsonObject position;

    public MiroItem(String id,
                    String type,
                    JsonObject data,
                    JsonObject style,
                    JsonObject geometry,
                    JsonObject position
    ) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.style =  style;
        this.geometry = geometry;
        this.position = position;
    }

    public String getId() {return id;}
    public String getType() {return type;}
    public JsonObject getData() {return data;}
    public JsonObject getStyle() {return style;}
    public JsonObject getGeometry() {return geometry;}
    public JsonObject getPosition() {return position;}
}
