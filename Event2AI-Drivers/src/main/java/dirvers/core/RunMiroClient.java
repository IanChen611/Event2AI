package dirvers.core;

import dirvers.app.SetMiroApiEnv;

public class RunMiroClient {
    private final DumpResult result;

    public RunMiroClient() throws Exception {
        SetMiroApiEnv setMiroApiEnv = new SetMiroApiEnv();
        MiroDumpClient client = setMiroApiEnv.getMiroDumpClient();
        this.result = client.dumpBoard(setMiroApiEnv.getBoardId());
    }

    public DumpResult run (){
        return this.result;
    }
}
