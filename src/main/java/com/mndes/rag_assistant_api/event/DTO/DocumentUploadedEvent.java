package com.mndes.rag_assistant_api.event.DTO;

import java.time.Instant;
import java.util.UUID;

public record DocumentUploadedEvent (

    UUID uuid,
    Long documentId,
    String filePath,
    Instant timestamp

) {}
