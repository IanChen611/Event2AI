package dirvers.core;

public class MiroJsonTransformer {
    private final ApiController controller;

    public MiroJsonTransformer(String accessToken) {
        this.controller = new ApiController(accessToken);
    }

    public DumpResult dumpBoard(String boardId) throws Exception {
        return controller.run(boardId);
    }
}
