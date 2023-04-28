package ir.sharif.math.ap2023.hw5.Util;

public abstract class AbstractAgent extends Thread{
    boolean alive;
    protected final Semaphore lock;

    private boolean paused;

    /*
    executes the task in thread-safe manner
     */

    public AbstractAgent(Semaphore lock) {
        alive = true;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (alive) {
            checkInterruption();
            lock.acquire();
            execute();
            lock.release();
        }
    }

    private void checkInterruption() {
        if (paused) {
            lock.forceLock();
            paused = false;
        }
    }

    public abstract void execute();

    public void kill() {
        alive = false;
    }

    public void restart() {
        lock.forceRelease();
    }

    public void pause() {
        paused = true;
    }

}
