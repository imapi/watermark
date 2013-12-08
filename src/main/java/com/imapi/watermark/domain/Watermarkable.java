package com.imapi.watermark.domain;

import java.io.Serializable;
import java.util.Map;

public interface Watermarkable extends Serializable {
    void generate(Progress progress);

    void put(Map<String, String> watermark);

    public interface Progress {
        void ok(Map<String, String> watermark);

        void fail(String message);

        void update(String info);
    }
}