package com.mndes.rag_assistant_api.document.DTO;

import com.mndes.rag_assistant_api.document.Document;
import com.mndes.rag_assistant_api.document.DocumentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID uuid,
        String name,
        DocumentStatus status,
        LocalDateTime uploadDate,
        String owner

) {
    public static DocumentResponse fromEntity (Document document) {
        return new DocumentResponse(
                document.getUuid(),
                document.getName(),
                document.getStatus(),
                document.getUploadDate(),
                document.getOwner()
        );
    }
}
