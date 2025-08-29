package core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Frame;

public class FrameService {
    private final MiroApiClient api;
    private final String boardId;

    public FrameService(MiroApiClient api, String boardId) {
        this.api = api;
        this.boardId = boardId;
    }

    public Frame createFrame(String title, int x, int y) throws Exception {
        // Build JSON payload without Java 15+ text blocks (keep Java 11 compatible)
        JsonObject payload = new JsonObject();
        JsonObject data = new JsonObject();
        data.addProperty("title", title);
        JsonObject position = new JsonObject();
        position.addProperty("x", x);
        position.addProperty("y", y);
        position.addProperty("origin", "center");
        payload.add("data", data);
        payload.add("position", position);
        String body = payload.toString();

        String url = "https://api.miro.com/v2/boards/" + boardId + "/frames";
        String response = api.postJson(url, body);

        JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
        String id = obj.get("id").getAsString();
        String frameTitle = obj.getAsJsonObject("data").get("title").getAsString();
        return new Frame(id, frameTitle, x, y);
    }

    public Frame getFrame(String frameId) throws Exception {
        String url = "https://api.miro.com/v2/boards/" + boardId + "/frames/" + frameId;
        String response = api.get(url);

        JsonObject obj = JsonParser.parseString(response).getAsJsonObject();
        String id = obj.get("id").getAsString();
        String title = obj.getAsJsonObject("data").get("title").getAsString();
        int x = obj.getAsJsonObject("position").get("x").getAsInt();
        int y = obj.getAsJsonObject("position").get("y").getAsInt();
        return new Frame(id, title, x, y);
    }
}
