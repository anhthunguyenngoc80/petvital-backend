package com.petvital.petvital_backend.feature.document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.petvital.petvital_backend.feature.document.dto.DocumentResponseDto;
import com.petvital.petvital_backend.feature.document.entity.Document;
import com.petvital.petvital_backend.feature.examination.ExaminationRepository;

@Service
public class DocumentService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "png", "jpg", "jpeg", "webp", "doc", "docx");

    private final DocumentRepository documentRepository;
    private final ExaminationRepository examinationRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            ExaminationRepository examinationRepository) {

        this.documentRepository = documentRepository;
        this.examinationRepository = examinationRepository;
    }

    /**
     * Stores the uploaded file content directly in the database (bytea)
     * together with its metadata.
     */
    @Transactional
    public DocumentResponseDto uploadDocument(
            MultipartFile file, Integer examinationId) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Uploaded file must not be empty");
        }

        if (examinationId == null
                || !examinationRepository.existsById(examinationId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Examination not found with id: " + examinationId);
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null
                        ? file.getOriginalFilename() : "file");

        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file type: ." + extension
                            + " (allowed: " + ALLOWED_EXTENSIONS + ")");
        }

        byte[] fileData;
        try (var in = file.getInputStream()) {
            fileData = in.readAllBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to read the uploaded file", e);
        }

        Document document = new Document();
        document.setExaminationId(examinationId);
        document.setFileUrl(null);
        document.setFileData(fileData);
        document.setContentType(
                file.getContentType() != null
                        ? file.getContentType() : "application/octet-stream");
        document.setOriginalFilename(originalFilename);
        document.setUploadedAt(LocalDateTime.now());

        Document saved = documentRepository.save(document);

        return toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DocumentResponseDto> getDocumentsByExamination(
            Integer examinationId) {

        return documentRepository.findAllByExaminationId(examinationId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Document getDocumentEntity(Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found with id: " + documentId));
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File must have an extension (e.g. .pdf, .jpg)");
        }
        return filename.substring(dotIndex + 1);
    }

    private DocumentResponseDto toResponseDto(Document document) {
        return new DocumentResponseDto(
                document.getDocumentId(),
                document.getExaminationId(),
                document.getOriginalFilename(),
                document.getContentType(),
                document.getUploadedAt());
    }
}
