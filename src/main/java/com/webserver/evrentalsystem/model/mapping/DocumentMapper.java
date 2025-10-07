package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DocumentMapper {
    @Mapping(source = "type.value", target = "type") // enum DocumentType -> string
    DocumentDto toDocumentDto(Document document);
}
