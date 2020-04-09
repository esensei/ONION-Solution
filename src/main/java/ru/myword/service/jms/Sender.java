package ru.myword.service.jms;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author Борис Лисков
 */
public class Sender
{
    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Inject
    private JmsTemplate jmsTemplate;

    public void send(long message)
    {
        LOG.debug("sending message='{}'", message);
        jmsTemplate.convertAndSend("processing", message);
    }

}
