package com.petvital.petvital_backend.feature.document;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.petvital.petvital_backend.feature.document.dto.DocumentResponseDto;
import com.petvital.petvital_backend.feature.document.entity.Document;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Uploads a document (multipart/form-data) and links it to an
     * existing examination.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("examinationId") Integer examinationId,
            Authentication authentication) {

        DocumentResponseDto response =
                documentService.uploadDocument(file, examinationId);

        return ResponseEntity.ok(response);
    }

    /**
     * Returns every document uploaded for an examination.
     */
    @GetMapping("/examination/{examinationId}")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByExamination(
            @PathVariable Integer examinationId) {

        List<DocumentResponseDto> documents =
                documentService.getDocumentsByExamination(examinationId);

        return ResponseEntity.ok(documents);
    }

    /**
     * Streams the file content stored in the database back to the client.
     */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Integer documentId) {

        Document document = documentService.getDocumentEntity(documentId);

        byte[] fileData = document.getFileData();
        if (fileData == null || fileData.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = document.getContentType() != null
                ? document.getContentType() : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + document.getOriginalFilename() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH,
                        String.valueOf(fileData.length))
                .body(fileData);
    }
}
