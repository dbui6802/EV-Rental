package com.webserver.evrentalsystem.controller.renter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import com.webserver.evrentalsystem.model.dto.request.DocumentUploadRequest;
import com.webserver.evrentalsystem.service.renter.DocumentRenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/renter/documents")
@Tag(name = "2.1. Renter Documents", description = "API quản lý tài liệu của Renter (CCCD, GPLX, ...)")
@SecurityRequirement(name = "bearerAuth")
public class DocumentRenterController {

    @Autowired
    private DocumentRenterService documentRenterService;

    // ================= Upload Document =================
    @Operation(
            summary = "Upload tài liệu",
            description = "Upload tài liệu với metadata (JSON) + file",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Upload thành công",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Document.class)))
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> uploadDocument(
            @Parameter(
                    description = "Metadata JSON (truyền vào như String JSON)",
                    example = "{\"type\":\"CCCD\", \"documentNumber\":\"123456789\"}"
            )
            @RequestParam("metadata") String metadataJson,

            @Parameter(description = "File tài liệu (JPG/PNG/PDF)")
            @RequestPart("file") MultipartFile file
    ) throws JsonProcessingException {
        // Parse JSON string thành object
        DocumentUploadRequest request =
                new ObjectMapper().readValue(metadataJson, DocumentUploadRequest.class);

        DocumentDto saved = documentRenterService.uploadDocument(
                request.getType(), request.getDocumentNumber(), file
        );
        return ResponseEntity.ok(saved);
    }

    // ================= Get Documents =================
    @Operation(
            summary = "Xem danh sách tài liệu",
            description = "Lấy danh sách tài liệu đã upload",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Danh sách tài liệu",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Document.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        List<DocumentDto> docs = documentRenterService.getDocuments();
        return ResponseEntity.ok(docs);
    }

    // ================= Delete Document =================
    @Operation(
            summary = "Xoá tài liệu",
            description = "Xoá tài liệu theo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Xoá thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy tài liệu", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "ID của tài liệu", required = true)
            @PathVariable("id") Long id
    ) {
        documentRenterService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }
}