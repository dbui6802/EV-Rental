package com.webserver.evrentalsystem.jpaconverter;

import com.webserver.evrentalsystem.entity.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodJpaConverter implements AttributeConverter<PaymentMethod, String> {
    @Override
    public String convertToDatabaseColumn(PaymentMethod method) {
        return method == null ? null : method.getValue();
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String value) {
        return value == null ? null : PaymentMethod.fromValue(value);
    }
}
