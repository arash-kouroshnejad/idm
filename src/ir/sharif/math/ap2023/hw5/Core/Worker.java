package ir.sharif.math.ap2023.hw5.Core;

import ir.sharif.math.ap2023.hw5.Persistence.FileHandler;
import ir.sharif.math.ap2023.hw5.SourceProvider;
import ir.sharif.math.ap2023.hw5.SourceReader;
import ir.sharif.math.ap2023.hw5.Util.*;


public class Worker extends AbstractWorker implements Assignable{
    long workLoad;
    FileHandler file;
    SourceReader reader;
    private SourceProvider sp;
    private long offset;
    long completed;

    public Worker(Semaphore lock, WorkerManager manager, int ID) {
        super(lock, manager, ID);
    }

    public void assign (long workLoad, FileHandler file, long offset, SourceProvider sp) {
        this.workLoad = workLoad;
        this.file = file;
        this.offset = offset;
        this.sp = sp;
        completed = 0;
    }

    public void start() {
        this.reader = sp.connect(offset);
        super.start();
    }


    @Override
    public void execute() {
        byte b = reader.read();
        file.write(b);
        completed++;
        if (completed == workLoad) {
            // TODO : signal the manager to reassign if necessary
            pause();
            manager.reassign(ID);
            // kill();
        }
    }

    public long getCompleted() {
        return completed;
    }

    public long getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(long workLoad) {
        this.workLoad = workLoad;
    }
}
