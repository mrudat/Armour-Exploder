package com.toraboka.Skyrim.ArmourExploder;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Timer;

import lev.gui.LProgressBarInterface;
import skyproc.SPGlobal;
import skyproc.gui.SUMGUI;

public class ParallelProgressBar {

    private static final Timer timer = new Timer(1000 / 30, e -> update());

    private static LProgressBarInterface progressBar = SUMGUI.progress;

    private static MRUList<WeakReference<ParallelProgressBar>> progressList = new MRUList<WeakReference<ParallelProgressBar>>();

    public static void setProgressBar(LProgressBarInterface progressBar) {
        ParallelProgressBar.progressBar = progressBar;
        timer.setDelay(1000/30);
    }

    private static void update() {
        if (progressList.size() == 0) {
            timer.stop();
            return;
        }
        ParallelProgressBar pb = progressList.getFirst().get();
        
        if (progressBar == null) {
            timer.setDelay(1000);
            SPGlobal.log(pb.title,pb.doneCount.get() + "/" + pb.totalCount);
            return;
        }
        progressBar.setStatusNumbered(pb.doneCount.get(), pb.totalCount,
                pb.title);
    }

    private final AtomicInteger doneCount = new AtomicInteger();

    private final int totalCount;

    private final String title;

    private final WeakReference<ParallelProgressBar> weakReference;

    public void increment() {
        doneCount.incrementAndGet();
        progressList.use(weakReference);
    }
    
    public void done() {
        progressList.remove(weakReference);
    }
    
    protected void finalize() throws Throwable {
        SPGlobal.log("Parallel Progress Bar", this.title, " destroyed but not done().");
        done();
    };

    public ParallelProgressBar(int totalCount, String title) {
        this.totalCount = totalCount;
        this.title = title;
        this.weakReference = new WeakReference<ParallelProgressBar>(this);
        progressList.add(weakReference);
        timer.start();
    }

}
