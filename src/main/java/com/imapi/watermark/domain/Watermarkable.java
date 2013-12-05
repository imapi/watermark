package com.imapi.watermark.domain;

public interface Watermarkable<T> {
    T generateWatermark();
}