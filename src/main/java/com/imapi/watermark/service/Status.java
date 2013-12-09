package com.imapi.watermark.service;

import java.io.Serializable;

/**
 * Completion status class, which is serialized and send to client on poll requests.
 */
public class Status implements Serializable {

    public static final Status NOT_FOUND = new Status("Wrong ticket");
    public static final Status WAITING = new Status("Waiting for available executor");
    public static final Status DONE = new Status("Processed successfully");
    public static final Status FAIL = new Status("Failed to process document");

    /**
     * Create new status with information. Used for progress statuses.
     *
     * @param info Informational message.
     * @return new Status
     */
    public static Status newStatus(String info) {
        return new Status(info);
    }

    public String getInfo() {
        return info;
    }

    private final String info;

    private Status(String info) {
        this.info = info;
    }
}