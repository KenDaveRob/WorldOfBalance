package utility;

// Java Imports
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The GameTimer class derives from the Timer class, which includes additional
 * methods to retrieve amount of elapsed, time started, and more.
 */
public class GameTimer extends Timer {

    private int delay;
    private Date startTime;
    private TimerTask task;

    public long getTimeElapsed() {
        if (startTime != null) {
            return new Date().getTime() - startTime.getTime();
        }

        return 0;
    }

    public int getTimeRemaining() {
        return (int) (delay - getTimeElapsed());
    }

    public long getStartTime() {
        return startTime.getTime();
    }

    public TimerTask getTask() {
        return task;
    }

    public boolean end() {
        if (task != null) {
            return task.cancel();
        }

        return false;
    }

    public boolean finish() {
        if (task != null) {
            task.run();
        }

        return end();
    }

    public void schedule(TimerTask task, int delay) {
        this.delay = delay;
        startTime = new Date();
        this.task = task;

        super.schedule(task, delay);
    }

    public void schedule(TimerTask task, int delay, int period) {
        this.delay = delay;
        startTime = new Date();
        this.task = task;

        super.schedule(task, delay, period);
    }
}
