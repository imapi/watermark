package com.imapi.watermark.service;

import com.imapi.watermark.domain.Watermarkable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Internal serializable object used for computations.
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
