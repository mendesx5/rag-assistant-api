package com.mndes.rag_assistant_api.document;

import com.mndes.rag_assistant_api.messaging.DocumentUploadedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void shouldSaveDocumentAsProcessingAndPublishEvent() {
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> {
            Document document = invocation.getArgument(0);
            document.setUuid(UUID.randomUUID());
            return document;
        });
        MockMultipartFile pdf = new MockMultipartFile(
                "file", "manual.pdf", "application/pdf", "fake pdf content".getBytes());

        assertNotNull(documentService.uploadDocument(pdf, "gabriel"));

        ArgumentCaptor<Document> saved = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(saved.capture());
        assertEquals(DocumentStatus.PROCESSING, saved.getValue().getStatus());
        assertEquals("gabriel", saved.getValue().getOwner());

        ArgumentCaptor<DocumentUploadedEvent> event = ArgumentCaptor.forClass(DocumentUploadedEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertEquals(saved.getValue().getUuid(), event.getValue().documentId());
        assertEquals(saved.getValue().getFilePath(), event.getValue().filePath());
    }

    @Test
    void shouldRejectNonPdfAndNotSaveNorPublish() {
        MockMultipartFile text = new MockMultipartFile(
                "file", "notes.txt", "text/plain", "hello".getBytes());

        assertThrows(IllegalArgumentException.class,
                () -> documentService.uploadDocument(text, "gabriel"));

        verifyNoInteractions(documentRepository, eventPublisher);
    }
}