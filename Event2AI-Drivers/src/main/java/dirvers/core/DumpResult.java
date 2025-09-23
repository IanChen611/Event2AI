package dirvers.core;

import com.google.gson.JsonObject;

public class DumpResult {
    private final JsonObject rawRoot;
    private final JsonObject aiDump;

    public DumpResult(JsonObject rawRoot, JsonObject aiDump) {
        this.rawRoot = rawRoot;
        this.aiDump = aiDump;
    }

    public JsonObject getRawRoot() {
        return rawRoot;
    }

    public JsonObject getAiDump() {
        return aiDump;
    }
}
