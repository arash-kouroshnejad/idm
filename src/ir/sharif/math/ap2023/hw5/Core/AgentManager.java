package ir.sharif.math.ap2023.hw5.Core;


import ir.sharif.math.ap2023.hw5.Persistence.FileHandler;
import ir.sharif.math.ap2023.hw5.SourceProvider;
import ir.sharif.math.ap2023.hw5.Util.AbstractAgent;
import ir.sharif.math.ap2023.hw5.Util.Assignable;
import ir.sharif.math.ap2023.hw5.Util.Semaphore;
import ir.sharif.math.ap2023.hw5.Util.WorkerManager;

import java.util.ArrayDeque;

public class AgentManager extends AbstractAgent implements WorkerManager {

    Assignable[] allWorkers;
    FileHandler[] allFiles;
    long totalWork;
    public static final long SAFE_MARGIN = 6;
    private final ArrayDeque<Integer> idleIDs = new ArrayDeque<>();

    private long distributedWork;
    private SourceProvider sourceProvider;
    public AgentManager(Semaphore lock) {
        super(lock);
    }

    @Override
    public void init(SourceProvider sourceProvider, String dest, int workerCount) {  // populates the worker array
        // TODO : CLEAN UP REQUIRED
        totalWork = sourceProvider.size();
        distributedWork = totalWork / workerCount;
        allWorkers = new Worker[workerCount];
        allFiles = new FileHandler[workerCount];
        Semaphore semaphore = new Semaphore(workerCount);
        this.sourceProvider = sourceProvider;
        FileHandler.generateEmptyFile(dest, totalWork);
        for (int i = 0; i < workerCount; i++) {
            FileHandler file = createFileHandler(dest, i);
            Worker worker = new Worker(semaphore, this, i);
            worker.assign(distributedWork, file, sourceProvider.connect(i * distributedWork));
            allWorkers[i] = worker;
        }
    }

    private FileHandler createFileHandler(String dest, int ID) {
        FileHandler file = new FileHandler();
        file.init(dest, ID  * distributedWork);
        allFiles[ID] = file;
        return file;
    }

    @Override
    public void execute() { // reassigns the idle worker
    // TODO : CLEANUP AND ABSTRACTION REQUIRED
        int overLoadedIndex = findOverloadedWorker();
        if (overLoadedIndex == -1) {
            if (allWorkersFinished()) {
                terminate();
            }
            return;
        }
        int ID = idleIDs.pop();
        Assignable idle = allWorkers[ID];
        Assignable overLoaded = allWorkers[overLoadedIndex];
        overLoaded.pause();
        long workLoad = overLoaded.getWorkLoad();
        overLoaded.setWorkLoad(workLoad - (workLoad - overLoaded.getCompleted()) / 2);
        FileHandler file = allFiles[ID];
        long newOffset = allFiles[overLoadedIndex].getOffset() + overLoaded.getWorkLoad();
        file.setOffset(newOffset);
        idle.assign((workLoad - overLoaded.getCompleted()) / 2, file, sourceProvider.connect(newOffset)); // TODO : CLEANUP REQUIRED
        idle.restart();
        overLoaded.restart();
        lock.acquire();
    }

    @Override
    public void begin() { // starts the workers
        // TODO : call the start method of all workers
        start();
        for (Assignable w : allWorkers) {
            w.start();
        }
    }

    private boolean allWorkersFinished() {
        return allWorkers.length == idleIDs.size();
    }

    private void terminate() { // TODO : CLEANUP REQUIRED
        for (Assignable a : allWorkers) {
            a.kill();
            a.stop();
        }
        kill();
    }

    @Override
    public void reassign(int ID) { // signals the manager that the worker is idle
        // TODO : prepare the details and information
        idleIDs.addLast(ID);
        lock.release();
    }

    private int findOverloadedWorker() {
        for (int i =0; i < allWorkers.length; i++) {
            if (allWorkers[i].getWorkLoad() - allWorkers[i].getCompleted() > SAFE_MARGIN) {
                return i;
            }
        }
        return -1;
    }
}
