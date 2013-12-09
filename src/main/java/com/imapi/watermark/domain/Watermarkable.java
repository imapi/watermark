package com.imapi.watermark.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * Classes implementing this interface could generate watermarks for themselves.
 * Also extends {@link Serializable}
 */
public interface Watermarkable extends Serializable {

    /**
     * Watermark generation method. Designed to be asynchronous, but could be used as synchronous as well.
     *
     * @param progress instance implementing {@link Progress} interface. Should not be {@code null}.
     */
    void generate(Progress progress);

    /**
     * Add generated watermark to the instance.
     *
     * @param watermark Watermark with all properties filled out.
     */
    void put(Map<String, String> watermark);

    /**
     * Helper for tracking watermark job progress. All notifications about the progress, completion
     * or fail should go through instance implementing this interface.
     */
    public interface Progress {

        /**
         * When watermark was successfully generated.
         *
         * @param watermark Watermark with all properties
         */
        void ok(Map<String, String> watermark);

        /**
         * Indicates a failure during watermark generation.
         *
         * @param message Error message
         */
        void fail(String message);

        /**
         * Progress updater, could be called as often as needed.
         *
         * @param info Informational message
         */
        void update(String info);
    }
}