package com.mndes.rag_assistant_api.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mndes.rag_assistant_api.document.Document;

import java.time.Instant;
import java.util.UUID;

public record DocumentUploadedEvent (
        @JsonProperty("event_id") UUID  eventId,
        @JsonProperty("document_id") UUID documentId,
        @JsonProperty("file_path") String filePath,
        @JsonProperty("timestamp") String timestamp
) {
    public static DocumentUploadedEvent from(Document document) {
        return new DocumentUploadedEvent(
                UUID.randomUUID(),
                document.getUuid(),
                document.getFilePath(),
                Instant.now().toString()
        );
    }
}