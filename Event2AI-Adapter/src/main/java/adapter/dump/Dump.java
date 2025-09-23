package adapter.dump;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface Dump {
    boolean supports(String type);
    JsonElement dump(JsonObject rawItem);
}
