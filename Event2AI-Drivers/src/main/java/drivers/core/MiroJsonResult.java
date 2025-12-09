package drivers.core;

import com.google.gson.JsonObject;

public class MiroJsonResult {
    private final JsonObject rawRoot;
    private final JsonObject aiResult;

    public MiroJsonResult(JsonObject rawRoot, JsonObject aiDump) {
        this.rawRoot = rawRoot;
        this.aiResult = aiDump;
    }

    public JsonObject getRawRoot() {
        return rawRoot;
    }

    public JsonObject getAiresult() {
        return aiResult;
    }
}
