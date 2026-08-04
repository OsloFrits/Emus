package service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TemporizadorService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}