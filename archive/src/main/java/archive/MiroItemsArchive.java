package archive;

import core.MiroJsonItem;
import event2ai.stickynote.StickyNote;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MiroItemsArchive {
    private ArrayList<MiroJsonItem> miroJsonItems;

    public MiroItemsArchive() {}

    public ArrayList<MiroJsonItem> load() {
        return this.miroJsonItems;
    }

    public void save(ArrayList<MiroJsonItem> miroJsonItems) {
        this.miroJsonItems = miroJsonItems;
    }

    public <T> ArrayList<T> getItemsWith(String type) {
        ArrayList<T> specificItems = new ArrayList<>();

        switch(type) {
            case "StickyNote":
                for(MiroJsonItem miroJsonItem : miroJsonItems) {
                    if(miroJsonItem.getType().equals("sticky_note")) {
                        StickyNote stickyNote = new StickyNote(
                                miroJsonItem.getId(),
                                miroJsonItem.getData().get("content").getAsString().replaceAll("<[^>]+>", "").trim(),
                                new Point2D.Double(miroJsonItem.getPosition().get("x").getAsDouble(), miroJsonItem.getPosition().get("y").getAsDouble()),
                                new Point2D.Double(miroJsonItem.getGeometry().get("width").getAsDouble(), miroJsonItem.getGeometry().get("height").getAsDouble()),
                                miroJsonItem.getStyle().get("fillColor").getAsString()
                        );

                        specificItems.add((T) stickyNote);
                    }
                }
                break;
            default:
                System.out.println("Invalid item type");
        }

        return specificItems;
    }
}
