package com.mndes.rag_assistant_api.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class DocumentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DocumentEventPublisher (RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this::handleConfirm);
        this.rabbitTemplate.setReturnsCallback(this::handleReturn);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDocumentUploaded (DocumentUploadedEvent event) {
        CorrelationData correlationData = new CorrelationData(event.eventId().toString());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event,
                correlationData
        );
        log.info("Event {} sent for document {}", event.eventId(), event.documentId());
    }

    private void handleConfirm (CorrelationData correlationData, boolean ack, String cause) {
        String eventId = correlationData != null ? correlationData.getId() : "unknown";
        if (ack) {
            log.info("RabbitMQ confirmed event {}", eventId);
        } else  {
            log.error("RabbitMQ REJECTED event {}: {}", eventId, cause);
        }
    }

    private void handleReturn (ReturnedMessage returned) {
        log.error("Message could not be routed: exchange={}, routingKey={}, reason={}",
                returned.getExchange(), returned.getRoutingKey(), returned.getReplyText());
    }

}
