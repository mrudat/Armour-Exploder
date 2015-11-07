package com.toraboka.Skyrim.ArmourExploder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

import skyproc.ARMO;
import skyproc.FormID;
import skyproc.GRUP;
import skyproc.Mod;
import skyproc.SPGlobal;

public class ArmourPickerEntry implements Comparable<ArmourPickerEntry> {

    private static final class ArmourPickerEntryExecutor implements Executor, Runnable {
        ConcurrentLinkedQueue<Runnable> foo = new ConcurrentLinkedQueue<Runnable>();

        AtomicBoolean executeImmediately = new AtomicBoolean(false);

        @Override
        public void execute(Runnable command) {
            if (executeImmediately.get()) {
                if (!foo.isEmpty()) {
                    drainQueue();
                }
                command.run();
            } else {
                foo.add(command);
            }
        }

        private void drainQueue() {
            foo.forEach(a -> {
                foo.remove(a);
                a.run();
            });
        }

        public void run() {
            drainQueue();
            executeImmediately.set(true);
        }
    }

    private static ConcurrentMap<FormID, ArmourPickerEntry> cache = new ConcurrentHashMap<FormID, ArmourPickerEntry>();

    private static GRUP<ARMO> armors;

    static ArmourPickerEntryExecutor queue = new ArmourPickerEntryExecutor();

    private final FormID formid;

    private final FutureTask<String> name;

    private ArmourPickerEntry(FormID id) {
        this.formid = id;
        name = new FutureTask<String>(() -> {
            ARMO armo = armors.get(id);
            if (armo.getName()
                .isEmpty()) {
                return id.toString();
            } else {
                return armo.getName();
            }
        });
        queue.execute(name);
    }

    public FormID getFormID() {
        // TODO Auto-generated method stub
        return formid;
    }

    public String toString() {
        try {
            if (name.isDone()) {
                return name.get() + " (" + formid.toString() + ")";
            }
        } catch (InterruptedException | ExecutionException e) {
            SPGlobal.logException(e);
        }
        return "(" + formid.toString() + ")";
    }

    public static ArmourPickerEntry get(FormID formid) {
        return cache.computeIfAbsent(formid, ArmourPickerEntry::new);
    }

    public static void setMerger(Mod merger) {
        armors = merger.getArmors();
        queue.run();
    }

    @Override
    public int compareTo(ArmourPickerEntry o) {
        if (o == null) {
            return -1;
        }
        return this.toString()
            .compareTo(o.toString());
    }
}
