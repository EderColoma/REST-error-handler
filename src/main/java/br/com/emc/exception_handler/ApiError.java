package br.com.emc.exception_handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import lombok.Data;

/**
 * Armazena as informações dos erros que podem ocorrer durante as chamadas REST.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@Data
class ApiError {

    private int statusCode;
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private short errorCode;
    private List<ApiSubError> subErrors = new ArrayList<>();

    /**
     * Construtor.
     */
    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    /**
     * Construtor
     *
     * @param status Código http do erro.
     */
    ApiError(HttpStatus status) {
        this();
        this.status = status;
        statusCode = status.value();
    }

    /**
     * Construtor
     *
     * @param status Código http do erro.
     * @param ex Exceção ocorrida.
     */
    ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        statusCode = status.value();
        message = "Unexpected error";
        debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Construtor
     *
     * @param status Código http do erro.
     * @param message Mensagem explicativa do erro.
     * @param ex Exceção ocorrida.
     */
    ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        statusCode = status.value();
        this.message = message;
        debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Construtor
     *
     * @param status Código http do erro.
     * @param message Mensagem explicativa do erro.
     * @param ex Exceção ocorrida.
     */
    ApiError(HttpStatus status, String message, Throwable ex, short errorCode) {
        this();
        this.status = status;
        this.errorCode = errorCode;
        statusCode = status.value();
        this.message = message;
        debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Adiciona um sub erro à lista de sub erros da exceção principal.
     *
     * @param subError Lista dos sub erros, usada para representar múltiplos erros em uma mesma chamada.
     */
    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    /**
     * Adiciona um sub erro de validação à lista de sub erros da exceção principal.
     *
     * @param object {@link Object} que sofreu a validação.
     * @param field O campo que possui um valor inválido.
     * @param rejectedValue O valor identificado como inválido.
     * @param message Mensagem explicativa do erro.
     */
    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    /**
     * Adiciona um sub erro de validação à lista de sub erros da exceção principal.
     *
     * @param fieldError {@link FieldError} contendo as informações do erro.
     */
    private void addValidationError(FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
            fieldError.getDefaultMessage());
    }

    /**
     * Adiciona uma lista de sub erros de validação à lista de sub erros da exceção principal.
     *
     * @param fieldErrors Lista de {@link FieldError} contendo as informações do erros.
     */
    void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }
}