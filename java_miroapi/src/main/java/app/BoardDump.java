package app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.MiroApiClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/**
 * Fetches the board metadata and all items, then saves a consolidated JSON
 * to target/board_dump.json so you can study the raw structure.
 */
public class BoardDump {
    public static void run(MiroApiClient api, String boardId, String outputPath) throws Exception {
        if (outputPath == null || outputPath.isBlank()) {
            outputPath = "target/board_dump.json";
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // 1) Board meta
        String boardUrl = "https://api.miro.com/v2/boards/" + boardId;
        String boardJson = api.get(boardUrl);
        JsonObject boardObj = safeParseObject(boardJson, "board meta");

        // 2) Items (all pages)
        JsonArray allItems = new JsonArray();
    String itemsUrl = "https://api.miro.com/v2/boards/" + boardId + "/items?limit=50";
        while (itemsUrl != null) {
            String page = api.get(itemsUrl);
            JsonObject pageObj = safeParseObject(page, "items page");

            JsonArray arr = extractItemsArray(pageObj);
            if (arr != null) {
                for (JsonElement e : arr) allItems.add(e);
            } else {
                // Help diagnose if the endpoint returned an error structure
                System.out.println("[warn] items array not found in response for: " + itemsUrl);
                System.out.println("[peek] " + page.substring(0, Math.min(page.length(), 200)) + "...");
            }

            String next = extractNextUrl(pageObj);
            itemsUrl = (next != null && !next.isBlank()) ? next : null;
        }

        // 2b) Fallback: some tenants/types may not appear in /items — fetch frames explicitly
        if (allItems.size() == 0) {
            JsonArray frames = fetchAllFrames(api, boardId);
            if (frames.size() > 0) {
                System.out.println("[info] /items returned 0; added " + frames.size() + " frames from /frames endpoint");
                for (JsonElement f : frames) allItems.add(wrapType(f.getAsJsonObject(), "frame"));
            }
        }

        // 3) Consolidated dump
        JsonObject dump = new JsonObject();
        dump.addProperty("fetchedAt", Instant.now().toString());
        dump.add("board", boardObj);
        dump.add("items", allItems);
        dump.addProperty("itemCount", allItems.size());

        // 4) Write to file (pretty)
        Path out = Path.of(outputPath);
        Files.createDirectories(out.getParent());
        Files.write(out, gson.toJson(dump).getBytes(StandardCharsets.UTF_8));

        System.out.println("Board dump saved to: " + out.toAbsolutePath());
        System.out.println("Items total: " + allItems.size());
    }

    private static JsonArray extractItemsArray(JsonObject obj) {
        if (obj == null) return null;
        if (obj.has("items") && obj.get("items").isJsonArray()) {
            return obj.getAsJsonArray("items");
        }
        if (obj.has("data") && obj.get("data").isJsonArray()) {
            return obj.getAsJsonArray("data");
        }
        return null;
    }

    private static String extractNextUrl(JsonObject obj) {
        if (obj == null) return null;
        // Prefer HAL-like links.next
        if (obj.has("links") && obj.get("links").isJsonObject()) {
            JsonObject links = obj.getAsJsonObject("links");
            if (links.has("next") && links.get("next").isJsonPrimitive()) {
                return links.get("next").getAsString();
            }
        }
        // Fallbacks seen in some APIs
        if (obj.has("next") && obj.get("next").isJsonPrimitive()) {
            return obj.get("next").getAsString();
        }
        return null;
    }

    private static JsonObject safeParseObject(String body, String label) {
        try {
            JsonElement e = JsonParser.parseString(body);
            if (e.isJsonObject()) return e.getAsJsonObject();
            System.out.println("[warn] Non-object JSON for " + label + ": " + preview(body));
            return null;
        } catch (Exception ex) {
            System.out.println("[error] Failed parsing JSON for " + label + ": " + ex.getMessage());
            System.out.println("[peek] " + preview(body));
            return null;
        }
    }

    private static String preview(String s) {
        if (s == null) return "<null>";
        return s.substring(0, Math.min(s.length(), 200)) + (s.length() > 200 ? "..." : "");
    }

    private static JsonArray fetchAllFrames(MiroApiClient api, String boardId) throws Exception {
        JsonArray frames = new JsonArray();
    String url = "https://api.miro.com/v2/boards/" + boardId + "/frames?limit=50";
        while (url != null) {
            String page = api.get(url);
            JsonObject obj = safeParseObject(page, "frames page");
            JsonArray arr = extractItemsArray(obj);
            if (arr != null) {
                for (JsonElement e : arr) frames.add(e);
            }
            url = extractNextUrl(obj);
        }
        return frames;
    }

    private static JsonObject wrapType(JsonObject obj, String type) {
        JsonObject w = new JsonObject();
        w.addProperty("type", type);
        w.add("raw", obj);
        return w;
    }
}
