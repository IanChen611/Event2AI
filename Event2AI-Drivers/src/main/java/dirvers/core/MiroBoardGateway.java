package dirvers.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MiroBoardGateway implements BoardGateway {
    private final MiroApiClient api;

    public MiroBoardGateway(String accessToken) {
        this.api = new MiroApiClient(accessToken);
    }

    @Override
    public JsonObject fetchBoard(String boardId) throws Exception {
        String boardUrl = "https://api.miro.com/v2/boards/" + boardId;
        return safeParseObject(api.get(boardUrl));
    }

    @Override
    public JsonArray fetchTagsForItem(String boardId, String itemId) throws Exception {
        String url = "https://api.miro.com/v2/boards/" + boardId + "/items/" + itemId + "/tags";
        JsonObject obj = safeParseObject(api.get(url));
        JsonArray arr = extractArray(obj, "tags", "data", "items");
        return arr != null ? arr : new JsonArray();
    }

    @Override
    public JsonArray fetchAllItems(String boardId) throws Exception {
        JsonArray all = new JsonArray();
        String url = "https://api.miro.com/v2/boards/" + boardId + "/items?limit=50";
        while (url != null) {
            JsonObject page = safeParseObject(api.get(url));
            JsonArray arr = extractArray(page, "items", "data");
            if (arr != null) for (JsonElement e : arr) all.add(e);
            url = extractNextUrl(page);
        }
        return all;
    }

    private static JsonObject safeParseObject(String body) {
        try {
            JsonElement e = JsonParser.parseString(body);
            return e.isJsonObject() ? e.getAsJsonObject() : new JsonObject();
        } catch (Exception ex) {
            return new JsonObject();
        }
    }

    private static JsonArray extractArray(JsonObject obj, String... keys) {
        if (obj == null) return null;
        for (String k : keys) {
            if (obj.has(k) && obj.get(k).isJsonArray()) return obj.getAsJsonArray(k);
        }
        return null;
    }

    private static String extractNextUrl(JsonObject obj) {
        if (obj == null) return null;
        if (obj.has("links") && obj.get("links").isJsonObject()) {
            JsonObject links = obj.getAsJsonObject("links");
            if (links.has("next") && links.get("next").isJsonPrimitive()) return links.get("next").getAsString();
        }
        if (obj.has("next") && obj.get("next").isJsonPrimitive()) return obj.get("next").getAsString();
        return null;
    }
}
