package ir.sharif.math.ap2023.hw5.Util;

public abstract class AbstractWorker extends AbstractAgent{
    protected final WorkerManager manager;

    protected final int ID;
    public AbstractWorker(Semaphore lock, WorkerManager manager, int ID) {
        super(lock);
        this.manager = manager;
        this.ID = ID;
    }
}
