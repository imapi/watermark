package com.imapi.watermark.service;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ivan_bondarenko
 * Date: 12/6/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Status implements Serializable {
    public static final Status NOT_FOUND = new Status("Wrong ticket");
    public static final Status WAITING = new Status("Waiting for available executor");
    public static final Status DONE = new Status("Processed successfully");
    public static final Status FAIL = new Status("Failed to process document");

    private final String info;

    private Status(String info) {
        this.info = info;
    }

    public static Status newStatus(String info) {
        return new Status(info);
    }

    public String getInfo() {
        return info;
    }
}