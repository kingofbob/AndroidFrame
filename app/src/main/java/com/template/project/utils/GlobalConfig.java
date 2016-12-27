package com.template.project.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 00020443 on 15/9/2016.
 */
public class GlobalConfig {

    private static GlobalConfig instance;
    private EventBus eventBus;


    public static GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    private GlobalConfig() {
        eventBus = new EventBus();
    }

    public static void setInstance(GlobalConfig instance) {
        GlobalConfig.instance = instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
