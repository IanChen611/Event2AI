package adapter.dump;

import java.util.List;
import java.util.Optional;

public class DumperRegistry {
    private final List<Dump> dumpers;

    public DumperRegistry(List<Dump> dumpers) {
        this.dumpers = dumpers;
    }

    public Optional<Dump> find(String type) {
        return dumpers.stream().filter(d -> d.supports(type)).findFirst();
    }
}
