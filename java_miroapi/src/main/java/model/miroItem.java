package model;

public class miroItem {
    private final String id;
    private final String type;
    private final Object data;
    private final Object style;
    private final Object geometry;
    private final Object position;
    private final Object parent;

    public miroItem(String id,
                    String type,
                    Object data,
                    Object style,
                    Object geometry,
                    Object position,
                    Object parent
    ) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.style = style;
        this.geometry = geometry;
        this.position = position;
        this.parent = parent;
    }

    public String getId() {return id;}
    public String getType() {return type;}
    public Object getData() {return data;}
    public Object getStyle() {return style;}
    public Object getGeometry() {return geometry;}
    public Object getPosition() {return position;}
    public Object getParent() {return parent;}
}
