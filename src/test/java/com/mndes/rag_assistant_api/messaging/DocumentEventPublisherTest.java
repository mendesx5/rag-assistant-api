package com.mndes.rag_assistant_api.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocumentEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldRegisterConfirmAndReturnCallbacksOnCreation() {
        new DocumentEventPublisher(rabbitTemplate);

        verify(rabbitTemplate).setConfirmCallback(any());
        verify(rabbitTemplate).setReturnsCallback(any());
    }

    @Test
    void shouldSendEventToExchangeUsingEventIdAsCorrelation() {
        DocumentEventPublisher publisher = new DocumentEventPublisher(rabbitTemplate);
        DocumentUploadedEvent event = new DocumentUploadedEvent(
                UUID.randomUUID(), UUID.randomUUID(), "/tmp/manual.pdf", Instant.now().toString());

        publisher.onDocumentUploaded(event);

        ArgumentCaptor<CorrelationData> correlation = ArgumentCaptor.forClass(CorrelationData.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.ROUTING_KEY),
                eq(event),
                correlation.capture());
        assertEquals(event.eventId().toString(), correlation.getValue().getId());
    }
}