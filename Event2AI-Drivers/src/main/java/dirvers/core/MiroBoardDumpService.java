package dirvers.core;

import dirvers.app.SetMiroApiEnv;

public class MiroBoardDumpService {
    private final MiroJsonResult result;

    public MiroBoardDumpService() throws Exception {
        SetMiroApiEnv setMiroApiEnv = new SetMiroApiEnv();
        MiroJsonTransformer client = setMiroApiEnv.getMiroDumpClient();
        this.result = client.dumpBoard(setMiroApiEnv.getBoardId());
    }

    public MiroJsonResult run (){
        return this.result;
    }
}
