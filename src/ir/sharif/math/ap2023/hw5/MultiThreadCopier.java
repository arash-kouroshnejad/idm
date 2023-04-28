package ir.sharif.math.ap2023.hw5;

import ir.sharif.math.ap2023.hw5.Core.AgentManager;
import ir.sharif.math.ap2023.hw5.Util.Semaphore;

import java.util.Set;

public class MultiThreadCopier {
    public static final long SAFE_MARGIN = 6;

    AgentManager manager;

    public MultiThreadCopier(SourceProvider sourceProvider, String dest, int workerCount) {
        manager = new AgentManager(new Semaphore(0)); // hibernated manager
        manager.init(sourceProvider, dest, workerCount);
    }

    public void start() {
        manager.begin();
    }
}
