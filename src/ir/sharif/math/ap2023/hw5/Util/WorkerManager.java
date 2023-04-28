package ir.sharif.math.ap2023.hw5.Util;

import ir.sharif.math.ap2023.hw5.SourceProvider;

public interface WorkerManager {
    void init(SourceProvider sourceProvider, String dest, int workerCount);
    void begin();
    void reassign(int ID);
}
