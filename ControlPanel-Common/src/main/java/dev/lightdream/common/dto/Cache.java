package dev.lightdream.common.dto;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Cache<T> {

    public long updatePeriod;
    public T value;
    public Consumer<Cache<T>> updater;
    public boolean enabled = true;

    public Cache(Consumer<Cache<T>> updater, long updatePeriod) {
        this.updater = updater;
        this.updatePeriod = updatePeriod;
        registerUpdater();
        update();
    }

    public void update() {
        this.updater.accept(this);
    }

    public void update(T value) {
        this.value = value;
    }

    public void registerUpdater() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (enabled) {
                    update();
                }
            }
        };
        Timer timer = new Timer();

        timer.schedule(task, 0, updatePeriod);
    }

    public T get() {
        return value;
    }

    public void cancel() {
        enabled = false;
    }

}
