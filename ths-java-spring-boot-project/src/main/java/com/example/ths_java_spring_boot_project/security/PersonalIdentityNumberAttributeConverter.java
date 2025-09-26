package com.example.ths_java_spring_boot_project.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

@Component
@Converter
public class PersonalIdentityNumberAttributeConverter implements AttributeConverter<String, String> {

    private final StringEncryptor encryptor;

    public PersonalIdentityNumberAttributeConverter(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : encryptor.decrypt(dbData);
    }
}
