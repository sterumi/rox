package rox.main.event;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CTH {
    private static Queue<CTH> HANDLERS = new ConcurrentLinkedQueue();
    private final String name;
    private final CTH parent;
    private long count;
    private long start;
    private long timingDepth;
    private long totalTime;
    private long curTickTotal;
    private long violations;

    public CTH(String name) {
        this(name, null);
    }

    public CTH(String name, CTH parent) {
        this.count = 0L;
        this.start = 0L;
        this.timingDepth = 0L;
        this.totalTime = 0L;
        this.curTickTotal = 0L;
        this.violations = 0L;
        this.name = name;
        this.parent = parent;
        HANDLERS.add(this);
    }

    public static void printTimings(PrintStream printStream) {
        Iterator iterator = HANDLERS.iterator();

        while (iterator.hasNext()) {
            CTH timings = (CTH) iterator.next();
            long time = timings.totalTime;
            long count = timings.count;
            if (count != 0L) {
                long avg = time / count;
                printStream.println("    " + timings.name + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + timings.violations);
            }
        }
    }

    public static void reload() {
        Iterator iterator = HANDLERS.iterator();

        while (iterator.hasNext()) {
            CTH timings = (CTH) iterator.next();
            timings.reset();
        }
    }

    public static void tick() {
        CTH timings;
        for (Iterator iterator = HANDLERS.iterator(); iterator.hasNext(); timings.timingDepth = 0L) {
            timings = (CTH) iterator.next();
            if (timings.curTickTotal > 50000000L) {
                timings.violations = (long) ((double) timings.violations + Math.ceil((double) (timings.curTickTotal / 50000000L)));
            }

            timings.curTickTotal = 0L;
        }

    }

    public void startTiming() {
        if (++this.timingDepth == 1L) {
            this.start = System.nanoTime();
            if (this.parent != null && ++this.parent.timingDepth == 1L) {
                this.parent.start = this.start;
            }
        }

    }

    public void stopTiming() {
        if (--this.timingDepth != 0L || this.start == 0L) {
            return;
        }

        long diff = System.nanoTime() - this.start;
        this.totalTime += diff;
        this.curTickTotal += diff;
        ++this.count;
        this.start = 0L;
        if (this.parent != null) {
            this.parent.stopTiming();
        }

    }

    public void reset() {
        this.count = 0L;
        this.violations = 0L;
        this.curTickTotal = 0L;
        this.totalTime = 0L;
        this.start = 0L;
        this.timingDepth = 0L;
    }

    public String getName() {
        return name;
    }

    public CTH getParent() {
        return parent;
    }

    public long getCount() {
        return count;
    }

    public long getCurTickTotal() {
        return curTickTotal;
    }

    public long getStart() {
        return start;
    }

    public long getTimingDepth() {
        return timingDepth;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getViolations() {
        return violations;
    }

    public static Queue<CTH> getHANDLERS() {
        return HANDLERS;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setCurTickTotal(long curTickTotal) {
        this.curTickTotal = curTickTotal;
    }

    public static void setHANDLERS(Queue<CTH> HANDLERS) {
        CTH.HANDLERS = HANDLERS;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setTimingDepth(long timingDepth) {
        this.timingDepth = timingDepth;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void setViolations(long violations) {
        this.violations = violations;
    }
}
