package com.webserver.evrentalsystem.model.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ResolveIncidentRequest {
    @NotNull
    private Long incidentId;

    @NotNull
    private String status;

    private String resolutionNotes;
}
