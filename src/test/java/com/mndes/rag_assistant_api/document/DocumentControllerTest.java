package com.mndes.rag_assistant_api.document;

import com.mndes.rag_assistant_api.document.DTO.DocumentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {

    @Mock
    DocumentService service;
    @InjectMocks
    DocumentController controller;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() { mockMvc = MockMvcBuilders.standaloneSetup(controller).build(); }

    @Test
    void upload_returns201_withPendingStatus() throws Exception {
        var file = new MockMultipartFile("file", "manual.pdf", "application/pdf", "%PDF-1.4".getBytes());
        var resp = new DocumentResponse(UUID.randomUUID(), "manual.pdf", DocumentStatus.PENDING, null, "gabriel");
        when(service.uploadDocument(any(), eq("gabriel"))).thenReturn(resp);

        mockMvc.perform(multipart("/api/documents").file(file).param("owner", "gabriel"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

}
