package com.imapi.watermark.service;

import com.imapi.watermark.Config;
import com.imapi.watermark.domain.Watermarkable;
import org.apache.commons.configuration.Configuration;
import org.mapdb.Bind;
import org.mapdb.HTreeMap;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Internal processing helper.
 */
class WorkersBootstrap implements Bind.MapListener<UUID, WatermarkFrame> {

    private static final Configuration CONF = Config.getConfig();

    private final HTreeMap<UUID, WatermarkFrame> queue;
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(CONF.getInt("workers.num", 10));

    /**
     * Queue cannot be changed after construction.
     *
     * @param queue Queue for intercommunication
     */
    public WorkersBootstrap(HTreeMap<UUID, WatermarkFrame> queue) {
        this.queue = queue;
    }

    /**
     * Bind this listener to the queue and add shutdown hook.
     */
    public void bind() {
        queue.addModificationListener(this);

        for (Map.Entry<UUID, WatermarkFrame> entry : queue.entrySet()) {
            WatermarkFrame frame = entry.getValue();
            if (frame.getStatus() != Status.DONE) {
                queue.replace(frame.getId(), new WatermarkFrame(frame.getId(), frame.getWatermarkable(), Status.WAITING));
            }
        }

        addShutdownHook();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                EXECUTOR.shutdown();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(UUID key, WatermarkFrame oldVal, final WatermarkFrame newVal) {
        if (newVal.getStatus() == Status.WAITING) {
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    newVal.getWatermarkable().generate(new Progress(newVal));
                }
            });
        }
    }

    private class Progress implements Watermarkable.Progress {
        private final WatermarkFrame frame;

        Progress(WatermarkFrame frame) {
            this.frame = frame;
        }

        @Override
        public void ok(Map<String, String> watermark) {
            Watermarkable watermarkable = frame.getWatermarkable();
            watermarkable.put(watermark);
            queue.replace(frame.getId(), new WatermarkFrame(frame.getId(), watermarkable, Status.DONE));
        }

        @Override
        public void fail(String message) {
            queue.replace(frame.getId(), new WatermarkFrame(frame.getId(), frame.getWatermarkable(), Status.FAIL));
        }

        @Override
        public void update(String info) {
            queue.replace(frame.getId(), new WatermarkFrame(frame.getId(), frame.getWatermarkable(), Status.newStatus(info)));
        }
    }

}
