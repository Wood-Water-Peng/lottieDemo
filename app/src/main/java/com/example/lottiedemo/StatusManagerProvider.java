package com.example.lottiedemo;

import java.util.HashMap;
import java.util.Map;

///
///@author jacky.peng on 
///
public class StatusManagerProvider {
    private static StatusManagerProvider mInstance = new StatusManagerProvider();
    Map<String, StatusManager> statusManagerMap = new HashMap<>();

    public static StatusManagerProvider getInstance() {
        return mInstance;
    }

    public StatusManager getStatusManager(String key) {
        StatusManager manager = statusManagerMap.get(key);
        if (manager == null) {
            manager = new StatusManager();
            statusManagerMap.put(key, manager);
        }
        return manager;
    }
}
