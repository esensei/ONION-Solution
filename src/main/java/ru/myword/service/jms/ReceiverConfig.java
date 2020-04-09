package ru.myword.service.jms;

import javax.inject.Inject;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import ru.myword.service.services.ProcessingInspection;

/**
 *
 * @author Борис Лисков
 */
@Configuration
@EnableJms
public class ReceiverConfig
{

    @Value("${artemis.brokerUrl}")
    private String brokerUrl;

    private final ProcessingInspection processingInspection;

    @Inject
    public ReceiverConfig(ProcessingInspection processingInspection)
    {
        this.processingInspection = processingInspection;
    }

    @Bean
    public ActiveMQConnectionFactory receiverActiveMQConnectionFactory()
    {
        return new ActiveMQConnectionFactory(brokerUrl);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory()
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(receiverActiveMQConnectionFactory());
        factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public Receiver receiver()
    {
        return new Receiver(processingInspection);
    }
}
