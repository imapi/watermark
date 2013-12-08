package com.imapi.watermark.domain;

import java.util.Map;

public class Book extends Document {
    private String topic;

    @Override
    public void generate(Progress progress) {
        Map<String, String> watermark = generateBaseWatermark();
        progress.update("40%");
        watermark.put("content", "book");
        watermark.put("topic", topic);
        progress.ok(watermark);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
