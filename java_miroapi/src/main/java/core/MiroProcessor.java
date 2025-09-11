package core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Optional;

public class MiroProcessor {
    private final MiroReader miroReader;

    public MiroProcessor() {
        miroReader = new MiroReader();
    }

    public ArrayList<MiroJsonItem> processFile(String filePath){
        ArrayList<MiroJsonItem> miroJsonItems = new ArrayList<>();

        JsonArray items = miroReader.readBoardItem(filePath);
        if(!items.isEmpty()){
            for(int i = 0; i < items.size(); i++){
                MiroJsonItem miroJsonItem = new MiroJsonItem(items.get(i).getAsJsonObject().get("id").getAsString(),
                        items.get(i).getAsJsonObject().get("type").getAsString(),
                        items.get(i).getAsJsonObject().get("data").getAsJsonObject(),
                        Optional.ofNullable(items.get(i).getAsJsonObject().get("style")).orElse(new JsonObject()).getAsJsonObject(),
                        items.get(i).getAsJsonObject().get("geometry").getAsJsonObject(),
                        items.get(i).getAsJsonObject().get("position").getAsJsonObject());
                miroJsonItems.add(miroJsonItem);
            }
        }
        else{
            System.out.println("No items found");
        }

        return miroJsonItems;
    }
}
