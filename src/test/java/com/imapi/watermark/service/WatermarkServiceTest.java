package com.imapi.watermark.service;

import com.imapi.watermark.domain.Document;
import com.jayway.awaitility.Duration;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.with;
import static org.hamcrest.Matchers.*;

/**
 * Created with IntelliJ IDEA.
 * User: ivan_bondarenko
 * Date: 12/6/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkServiceTest {

    @Test
    public void testPoll() throws Exception {
        UUID idSuccess = WatermarkService.submit(new MockDocumentSuccess());
        with().pollInterval(20, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until processing status will be WAITING")
                .until(poll(idSuccess), equalTo(Status.WAITING));

        with().pollInterval(20, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until processing status will be WAITING")
                .until(poll(idSuccess), not(equalTo(Status.WAITING)));

        with().pollInterval(20, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until processing status will be DONE")
                .until(poll(idSuccess), equalTo(Status.DONE));

        UUID idFail = WatermarkService.submit(new MockDocumentFail());
        with().pollInterval(20, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until processing status will be FAIL")
                .until(poll(idFail), equalTo(Status.FAIL));
    }

    @Test
    public void testPick() throws Exception {
        UUID idSuccess = WatermarkService.submit(new MockDocumentSuccess());
        with().pollDelay(300, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until watermark will be ready")
                .until(pick(idSuccess), hasEntry("ok", "ok"));

        UUID idFail = WatermarkService.submit(new MockDocumentFail());
        with().pollDelay(300, TimeUnit.MILLISECONDS)
                .timeout(Duration.ONE_SECOND)
                .await("Until watermark will be ready")
                .until(pick(idFail), not(hasKey("title")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPickWithWrongId() throws Exception {
        UUID id = UUID.randomUUID();
        Assert.assertNull(WatermarkService.pick(id));
    }

    @Test
    public void testPollWithWrongId() throws Exception {
        UUID id = UUID.randomUUID();
        Assert.assertEquals(Status.NOT_FOUND, WatermarkService.poll(id));
    }

    @Test
    public void testSubmitAlreadyWatermarked() throws Exception {
        Document document = new MockDocumentSuccess();
        document.setWatermark(Collections.singletonMap("test", "test"));
        UUID id = WatermarkService.submit(document);
        Assert.assertEquals(Status.DONE, WatermarkService.poll(id));
    }

    private Callable<Status> poll(final UUID id) {
        return new Callable<Status>() {
            @Override
            public Status call() throws Exception {
                return WatermarkService.poll(id);
            }
        };
    }

    private Callable<Map<String, String>> pick(final UUID id) {
        return new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                return ((Document) WatermarkService.pick(id)).getWatermark();
            }
        };
    }

    private static class MockDocumentSuccess extends Document {
        @Override
        public void generate(Progress progress) {
            Map<String, String> watermark = generateBaseWatermark();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            watermark.put("ok", "ok");
            progress.update("50%");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress.ok(watermark);
        }
    }

    private static class MockDocumentFail extends Document {
        @Override
        public void generate(Progress progress) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress.fail("Fail");
        }
    }
}
