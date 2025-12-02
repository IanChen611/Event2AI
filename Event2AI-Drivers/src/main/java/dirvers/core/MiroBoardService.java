package dirvers.core;

import dirvers.app.SetMiroApiEnv;

public class MiroBoardService {
    private MiroJsonResult result;
    private MiroJsonTransformer client;
    private SetMiroApiEnv setMiroApiEnv;

    public MiroBoardService() throws Exception {
        this.setMiroApiEnv = new SetMiroApiEnv();
        this.client = setMiroApiEnv.getMiroDumpClient();
    }

    public MiroJsonResult run () throws Exception{
        this.result = client.dumpBoard(setMiroApiEnv.getBoardId());
        return this.result;
    }
}
