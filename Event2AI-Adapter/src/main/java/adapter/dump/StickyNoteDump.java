package adapter.dump;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StickyNoteDump implements Dump {
    @Override
    public boolean supports(String type) {
        return "sticky_note".equals(type);
    }

    @Override
    public JsonElement dump(JsonObject raw) {
        JsonObject out = new JsonObject();
        out.addProperty("id", raw.get("id").getAsString());
        JsonObject data = raw.has("data") && raw.get("data").isJsonObject() ? raw.getAsJsonObject("data") : new JsonObject();
        String html = data.has("content") && data.get("content").isJsonPrimitive() ? data.get("content").getAsString() : " ";
        out.addProperty("html", html);
        out.addProperty("text", HtmlUtils.toPlainText(html));

        JsonObject style = raw.has("style") && raw.get("style").isJsonObject() ? raw.getAsJsonObject("style") : new JsonObject();
        if (style.has("fillColor")) out.addProperty("color", style.get("fillColor").getAsString());

        JsonObject geom = raw.has("geometry") && raw.get("geometry").isJsonObject() ? raw.getAsJsonObject("geometry") : new JsonObject();
        if (geom.has("width")) out.addProperty("width", geom.get("width").getAsDouble());
        if (geom.has("height")) out.addProperty("height", geom.get("height").getAsDouble());

        JsonObject pos = raw.has("position") && raw.get("position").isJsonObject() ? raw.getAsJsonObject("position") : new JsonObject();
        if (pos.has("x")) out.addProperty("x", pos.get("x").getAsDouble());
        if (pos.has("y")) out.addProperty("y", pos.get("y").getAsDouble());

        return out;
    }
}
