package usecase;

import com.google.gson.JsonArray;
import entity.StickyNote;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Optional;

public class JsonToStickyNote {
    public static ArrayList<StickyNote> convert(JsonArray jsonItems) {
        ArrayList<StickyNote> items =  new ArrayList<>();
        if(!jsonItems.isEmpty()){
            for (int i = 0; i < jsonItems.size(); i++) {
                String text = jsonItems.get(i).getAsJsonObject().get("text").getAsString();
                String tag = "";
                JsonArray tags = jsonItems.get(i).getAsJsonObject().get("tags").getAsJsonArray();
                if(!tags.isEmpty()){
                    tag = tags.get(0).getAsJsonObject().get("name").getAsString();
                }

                items.add(new StickyNote(
                        jsonItems.get(i).getAsJsonObject().get("id").getAsString(),
                        text,
                        new Point2D.Double(jsonItems.get(i).getAsJsonObject().get("x").getAsDouble(), jsonItems.get(i).getAsJsonObject().get("y").getAsDouble()),
                        new Point2D.Double(jsonItems.get(i).getAsJsonObject().get("width").getAsDouble(), jsonItems.get(i).getAsJsonObject().get("height").getAsDouble()),
                        jsonItems.get(i).getAsJsonObject().get("color").getAsString(),
                        tag
                ));
            }
        }
        else{
            System.out.println("No items found");
        }

        return items;
    }
}
