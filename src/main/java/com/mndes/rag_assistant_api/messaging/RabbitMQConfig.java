package com.mndes.rag_assistant_api.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "documents.exchange";
    public static final String QUEUE = "documents.queue";
    public static final String ROUTING_KEY = "documents.uploaded";

    // Exchange: It is the “counter” where Java delivers the message
    @Bean
    public DirectExchange documentsExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    // Queue: This is where the message waits for Python
    @Bean
    public Queue documentsProcessingQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    // Rule: "Anything that reaches the exchange with the key 'documents.uploaded' goes into this queue"
    @Bean
    public Binding documentsBinding(DirectExchange documentsExchange, Queue documentsProcessingQueue) {
        return BindingBuilder.bind(documentsProcessingQueue)
                .to(documentsExchange)
                .with(ROUTING_KEY);
    }

    // Converts the Java object into JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
