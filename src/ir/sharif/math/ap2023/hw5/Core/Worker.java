package ir.sharif.math.ap2023.hw5.Core;

import ir.sharif.math.ap2023.hw5.Persistence.FileHandler;
import ir.sharif.math.ap2023.hw5.SourceReader;
import ir.sharif.math.ap2023.hw5.Util.*;


public class Worker extends AbstractWorker implements Assignable{
    long workLoad;
    FileHandler file;
    SourceReader reader;
    long completed;

    public Worker(Semaphore lock, WorkerManager manager, int ID) {
        super(lock, manager, ID);
    }

    public void assign (long workLoad, FileHandler file, SourceReader reader) {
        this.workLoad = workLoad;
        this.file = file;
        this.reader = reader;
        completed = 0;
    }


    @Override
    public void execute() {
        byte b = reader.read();
        file.write(b);
        completed++;
        if (completed == workLoad) {
            // TODO : signal the manager to lock and reassign if necessary
            pause();
            manager.reassign(ID);
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
