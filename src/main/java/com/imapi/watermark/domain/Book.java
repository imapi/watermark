package com.imapi.watermark.domain;

import java.util.Map;

public class Book extends Document {
    private String topic;

    @Override
    public Map<String, String> generateWatermark() {
        Map<String, String> watermark = super.generateWatermark();
        watermark.put("content", "journal");
        watermark.put("topic", topic);
        return watermark;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
