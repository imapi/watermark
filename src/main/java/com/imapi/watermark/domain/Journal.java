package com.imapi.watermark.domain;

import java.util.Map;

public class Journal extends Document {

    @Override
    public Map<String, String> generateWatermark() {
        Map<String, String> watermark = super.generateWatermark();
        watermark.put("content", "journal");
        return watermark;
    }
}
