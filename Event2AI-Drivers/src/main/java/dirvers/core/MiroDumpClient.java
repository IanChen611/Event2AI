package dirvers.core;

import adapter.dump.DumpComposer;
import adapter.dump.DumperRegistry;
import adapter.dump.StickyNoteDump;
import adapter.dump.TagDump;
import com.google.gson.JsonObject;

import java.util.List;

public class MiroDumpClient {
    private final DumpRunner runner;

    public MiroDumpClient(String accessToken) {
        BoardGateway gateway = new MiroBoardGateway(accessToken);
        DumpComposer composer = new DumpComposer(new DumperRegistry(List.of(
                new StickyNoteDump(), new TagDump()
        )));
        this.runner = new DumpRunner(gateway, composer);
    }

    public DumpResult dumpBoard(String boardId) throws Exception {
        return runner.run(boardId);
    }
}
