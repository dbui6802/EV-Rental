package com.webserver.evrentalsystem.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String error;
    private String message;

    public String toJson() throws JsonProcessingException, CharacterCodingException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonData = mapper.writeValueAsString(this).getBytes(StandardCharsets.UTF_8);

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        CharBuffer charBuffer = decoder.decode(ByteBuffer.wrap(jsonData));
        return charBuffer.toString();
    }
}
