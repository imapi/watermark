package com.imapi.watermark.domain;

import java.util.Map;

/**
 * Book domain class, used in serialization, therefore should have default constructor.
 *
 * @author Ivan Bondarenko
 */
public class Journal extends Document {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(Progress progress) {
        Map<String, String> watermark = generateBaseWatermark();
        progress.update("50%");
        watermark.put("content", "journal");
        progress.ok(watermark);
    }
}
