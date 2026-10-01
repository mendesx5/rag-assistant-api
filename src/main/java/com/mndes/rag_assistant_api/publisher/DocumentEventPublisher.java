package com.mndes.rag_assistant_api.publisher;

import com.mndes.rag_assistant_api.config.RabbitMQConfig;
import com.mndes.rag_assistant_api.event.DTO.DocumentUploadedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DocumentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(DocumentUploadedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DOCUMENT_EXCHANGE,
                RabbitMQConfig.DOCUMENT_ROUTING_KEY,
                event
        );
    }

}
