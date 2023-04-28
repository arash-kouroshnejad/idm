package ir.sharif.math.ap2023.hw5.Util;

import ir.sharif.math.ap2023.hw5.Persistence.FileHandler;
import ir.sharif.math.ap2023.hw5.SourceProvider;
public interface Assignable {
    void assign(long workLoad, FileHandler file, long offset, SourceProvider sp);
    void start();
    long getCompleted();
    long getWorkLoad();
    void setWorkLoad(long workLoad);
    void restart(); // TODO : rename required

    void pause();
    void kill();
    void stop(); // TODO : CLEANUP REQUIRED
}
