package com.example.sportspocialmediaapp;
import com.example.sportspocialmediaapp.AppExecutors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private final ExecutorService diskIO;

    // Singleton instance
    private static final AppExecutors INSTANCE = new AppExecutors();

    private AppExecutors() {
        diskIO = Executors.newSingleThreadExecutor();
    }

    public static AppExecutors getInstance() {
        return INSTANCE;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }
}
