package com.imapi.watermark.domain;

import java.util.Map;

/**
 * Book domain class, used in serialization, therefore should have default constructor.
 *
 * @author Ivan Bondarenko
 */
public class Book extends Document {
    private String topic;

    /**
     * {@inheritDoc}
     */
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
