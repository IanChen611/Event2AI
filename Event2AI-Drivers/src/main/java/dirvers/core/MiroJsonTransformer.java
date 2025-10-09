package dirvers.core;

import adapter.dump.MiroJsonObjectComposer;

public class MiroJsonTransformer {
    private final DumpRunner runner;

    public MiroJsonTransformer(String accessToken) {
        BoardGateway gateway = new MiroBoardGateway(accessToken);
        MiroJsonObjectComposer composer = new MiroJsonObjectComposer();
        this.runner = new DumpRunner(gateway, composer);
    }

    public DumpResult dumpBoard(String boardId) throws Exception {
        return runner.run(boardId);
    }
}
