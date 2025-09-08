package archive;

import core.MiroItem;
import event2ai.stickynote.StickyNote;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MiroItemsArchive {
    private ArrayList<MiroItem> miroItems;

    public MiroItemsArchive() {}

    public ArrayList<MiroItem> load() {
        return this.miroItems;
    }

    public void save(ArrayList<MiroItem> miroItems) {
        this.miroItems = miroItems;
    }

    public <T> ArrayList<T> getItemsWith(String type) {
        ArrayList<T> specificItems = new ArrayList<>();

        switch(type) {
            case "StickyNote":
                for(MiroItem miroItem : miroItems) {
                    if(miroItem.getType().equals("sticky_note")) {
                        StickyNote stickyNote = new StickyNote(
                                miroItem.getId(),
                                miroItem.getData().get("content").getAsString().replaceAll("<[^>]+>", "").trim(),
                                new Point2D.Double(miroItem.getPosition().get("x").getAsDouble(), miroItem.getPosition().get("y").getAsDouble()),
                                new Point2D.Double(miroItem.getGeometry().get("width").getAsDouble(), miroItem.getGeometry().get("height").getAsDouble()),
                                miroItem.getStyle().get("fillColor").getAsString()
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
