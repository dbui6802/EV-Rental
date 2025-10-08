package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.Document;
import com.webserver.evrentalsystem.entity.DocumentType;
import com.webserver.evrentalsystem.model.dto.entitydto.DocumentDto;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-04T20:16:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class DocumentMapperImpl implements DocumentMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public DocumentDto toDocumentDto(Document document) {
        if ( document == null ) {
            return null;
        }

        DocumentDto documentDto = new DocumentDto();

        String value = documentTypeValue( document );
        if ( value != null ) {
            documentDto.setType( Enum.valueOf( DocumentType.class, value ) );
        }
        documentDto.setId( document.getId() );
        documentDto.setUser( userMapper.toUserDto( document.getUser() ) );
        documentDto.setDocumentNumber( document.getDocumentNumber() );
        documentDto.setDocumentUrl( document.getDocumentUrl() );
        documentDto.setVerified( document.getVerified() );
        documentDto.setVerifiedBy( userMapper.toUserDto( document.getVerifiedBy() ) );
        documentDto.setCreatedAt( document.getCreatedAt() );

        return documentDto;
    }

    private String documentTypeValue(Document document) {
        if ( document == null ) {
            return null;
        }
        DocumentType type = document.getType();
        if ( type == null ) {
            return null;
        }
        String value = type.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
