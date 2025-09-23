package adapter.dump;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TagDump implements Dump {
    @Override
    public boolean supports(String type) {
        // Keep accepting legacy "tag" type; per-item tags don't come via items list.
        return "tag".equals(type);
    }

    @Override
    public JsonElement dump(JsonObject raw) {
        JsonObject out = new JsonObject();
        if (raw.has("id") && raw.get("id").isJsonPrimitive()) {
            out.addProperty("id", raw.get("id").getAsString());
        }

        // Name can be under different keys depending on API: title | name | text
        if (raw.has("title") && raw.get("title").isJsonPrimitive()) {
            out.addProperty("name", raw.get("title").getAsString());
        } else if (raw.has("name") && raw.get("name").isJsonPrimitive()) {
            out.addProperty("name", raw.get("name").getAsString());
        } else if (raw.has("text") && raw.get("text").isJsonPrimitive()) {
            out.addProperty("name", raw.get("text").getAsString());
        }

        // Color can be at color | backgroundColor | fillColor | style.color
        if (raw.has("color") && raw.get("color").isJsonPrimitive()) {
            out.addProperty("color", raw.get("color").getAsString());
        } else if (raw.has("backgroundColor") && raw.get("backgroundColor").isJsonPrimitive()) {
            out.addProperty("color", raw.get("backgroundColor").getAsString());
        } else if (raw.has("fillColor") && raw.get("fillColor").isJsonPrimitive()) {
            out.addProperty("color", raw.get("fillColor").getAsString());
        } else if (raw.has("style") && raw.get("style").isJsonObject()) {
            JsonObject style = raw.getAsJsonObject("style");
            if (style.has("color") && style.get("color").isJsonPrimitive()) {
                out.addProperty("color", style.get("color").getAsString());
            }
        }
        return out;
    }
}
