package ru.myword.service.jms;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import ru.myword.service.services.ProcessingInspection;

/**
 *
 * @author Борис Лисков
 */
public class Receiver
{
    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private final ProcessingInspection processingInspection;

    public Receiver(ProcessingInspection processingInspection)
    {
        this.processingInspection = processingInspection;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @JmsListener(destination = "processing")
    public void receive(long message) {
        LOG.debug("received message='{}'", message);
        latch.countDown();

        processingInspection.exec(message);
    }
}
