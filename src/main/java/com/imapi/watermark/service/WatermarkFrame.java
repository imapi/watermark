package com.imapi.watermark.service;

import com.imapi.watermark.domain.Watermarkable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ivan_bondarenko
 * Date: 12/6/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
class WatermarkFrame implements Serializable {
    private final Watermarkable watermarkable;
    private final Status status;
    private final UUID id;
    public WatermarkFrame(UUID id, Watermarkable watermarkable, Status status) {
        this.id = id;
        this.watermarkable = watermarkable;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Watermarkable getWatermarkable() {
        return watermarkable;
    }

    UUID getId() {
        return id;
    }
}
