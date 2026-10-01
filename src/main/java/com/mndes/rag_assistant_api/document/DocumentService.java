package com.mndes.rag_assistant_api.document;

import com.mndes.rag_assistant_api.document.DTO.DocumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final String UPLOAD_DIR = "uploads";

    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file, String owner) {

        validateFile(file);

        String storedFilePath = storeFile(file);

        Document document = Document.builder()
                .name(file.getOriginalFilename())
                .status(DocumentStatus.PENDING)
                .uploadDate(LocalDateTime.now())
                .owner(owner)
                .filePath(storedFilePath)
                .build();

        Document savedDocument = documentRepository.save(document);

        return DocumentResponse.fromEntity(savedDocument);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File exceeds the 10MB limit");
        }
    }

    private String storeFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + ".pdf";
            Path targetPath = uploadPath.resolve(fileName);
            file.transferTo(targetPath);

            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store the file", e);
        }
    }



}
