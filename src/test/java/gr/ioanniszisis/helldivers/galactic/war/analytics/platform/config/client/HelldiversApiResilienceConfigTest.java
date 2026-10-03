package gr.ioanniszisis.helldivers.galactic.war.analytics.platform.config.client;

import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelldiversApiResilienceConfigTest {

    private final Retry retry = new HelldiversApiResilienceConfig().helldiversApiRetry();

    @Test
    void retriesBadGatewayWithExponentialBackoffUntilSuccess() {
        AtomicInteger calls = new AtomicInteger();
        Supplier<String> flaky = () -> {
            if (calls.incrementAndGet() < 3) {
                throw HttpServerErrorException.create(HttpStatus.BAD_GATEWAY, "Bad Gateway", null, null, null);
            }
            return "ok";
        };

        long start = System.nanoTime();
        String result = Retry.decorateSupplier(retry, flaky).get();
        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;

        assertEquals("ok", result);
        assertEquals(3, calls.get());
        assertTrue(elapsedMillis >= 3_000, "expected 1s + 2s of backoff, waited " + elapsedMillis + "ms");
    }

    @Test
    void givesUpOnTimeoutsAfterMaxAttempts() {
        AtomicInteger calls = new AtomicInteger();
        Supplier<String> hung = () -> {
            calls.incrementAndGet();
            throw new ResourceAccessException("Read timed out");
        };

        assertThrows(ResourceAccessException.class, () -> Retry.decorateSupplier(retry, hung).get());
        assertEquals(3, calls.get());
    }

    @Test
    void doesNotRetryInternalServerError() {
        AtomicInteger calls = new AtomicInteger();
        Supplier<String> broken = () -> {
            calls.incrementAndGet();
            throw HttpServerErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null, null, null);
        };

        assertThrows(HttpServerErrorException.class, () -> Retry.decorateSupplier(retry, broken).get());
        assertEquals(1, calls.get());
    }
}
