package edu.java.bot.configuration;

import lombok.Setter;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.SleepingBackOffPolicy;
import org.springframework.retry.backoff.ThreadWaitSleeper;

@SuppressWarnings("MagicNumber")
public class LinearBackoffPolicy implements SleepingBackOffPolicy<LinearBackoffPolicy> {
    private long initialInterval = 100L;
    @Setter
    private Sleeper sleeper = new ThreadWaitSleeper();

    @Override
    public LinearBackoffPolicy withSleeper(Sleeper sleeper) {
        LinearBackoffPolicy res = new LinearBackoffPolicy();
        res.initialInterval = this.initialInterval;
        res.setSleeper(sleeper);
        return res;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext(this.initialInterval);
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LinearBackOffContext context = (LinearBackOffContext) backOffContext;
        long sleepTime = context.getSleepAndIncrement();
        try {
            sleeper.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BackOffInterruptedException("Thread interrupted while sleeping", e);
        }
    }

    public void setInitialInterval(long initialInterval) {
        this.initialInterval = initialInterval > 1L ? initialInterval : 1L;
    }

    static class LinearBackOffContext implements BackOffContext {
        private final long initialInterval;
        private long interval;

        LinearBackOffContext(long initialInterval) {
            this.initialInterval = initialInterval;
            interval = initialInterval;
        }

        public synchronized long getSleepAndIncrement() {
            long sleep = interval;
            interval += initialInterval;
            return sleep;
        }
    }
}
