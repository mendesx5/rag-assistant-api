package com.mndes.rag_assistant_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

public class RabbitMQConfig {

    public static final String DOCUMENT_EXCHANGE = "document.exchange";

    public static final String DOCUMENT_QUEUE = "document.processing.queue";

    public static final String DOCUMENT_ROUTING_KEY = "document.uploaded";

    @Bean
    public TopicExchange documentExchange() {
        return new TopicExchange(DOCUMENT_EXCHANGE, true , false);
    }

    @Bean
    public Queue documentProcessingQueue() {
        return new Queue(DOCUMENT_QUEUE, true);
    }

    @Bean
    public Binding documentProcessingBinding(
            TopicExchange documentExchange,
            Queue documentProcessingQueue
    ) {
        return BindingBuilder
                .bind(documentProcessingQueue)
                .to(documentExchange)
                .with(DOCUMENT_ROUTING_KEY);
    }

}
