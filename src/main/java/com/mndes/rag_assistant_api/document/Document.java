package com.mndes.rag_assistant_api.document;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String name;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private LocalDateTime uploadDate;

    private String owner;

    private String filePath;

}
