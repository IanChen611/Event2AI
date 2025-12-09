package drivers.core;

import adapter.dump.MiroJsonObjectComposer;
import com.google.gson.JsonObject;
import drivers.app.SetMiroApiEnv;

public class MiroBoardService {
    private JsonObject jsonObject;
    private MiroJsonTransformer miroJsonTransformer;
    private SetMiroApiEnv setMiroApiEnv;
    private MiroJsonObjectComposer miroJsonObjectComposer;

    public MiroBoardService() throws Exception {
        this.setMiroApiEnv = new SetMiroApiEnv();
        this.miroJsonTransformer = setMiroApiEnv.getMiroJsonTransformer();
        this.miroJsonObjectComposer = new MiroJsonObjectComposer();
    }

    public JsonObject run () throws Exception{
        this.jsonObject = miroJsonTransformer.run(setMiroApiEnv.getBoardId());
        return miroJsonObjectComposer.compose(jsonObject);
    }
}
