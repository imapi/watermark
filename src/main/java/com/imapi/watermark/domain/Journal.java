package com.imapi.watermark.domain;

import java.util.Map;

public class Journal extends Document {

    @Override
    public void generate(Progress progress) {
        Map<String, String> watermark = generateBaseWatermark();
        progress.update("50%");
        watermark.put("content", "journal");
        progress.ok(watermark);
    }
}
