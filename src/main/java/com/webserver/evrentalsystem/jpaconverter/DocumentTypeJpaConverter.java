package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.DocumentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DocumentTypeJpaConverter implements AttributeConverter<DocumentType, String> {

    @Override
    public String convertToDatabaseColumn(DocumentType type) {
        if (type == null) {
            return null;
        }
        return type.getValue();
    }

    @Override
    public DocumentType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return DocumentType.fromValue(value);
    }
}
