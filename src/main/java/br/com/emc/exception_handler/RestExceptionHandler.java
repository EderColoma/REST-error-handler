package br.com.emc.exception_handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.emc.exception_handler.exception.NoResultsException;
import br.com.emc.exception_handler.exception.UnsupportedFileExtensionException;

/**
 * Gerencia as exceções ocorrias nas chamadas REST.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Personaliza a resposta para {@link Exception} do tipo {@link MethodArgumentNotValidException}.
     *
     * @param ex A exceção ocorrida.
     * @param headers O cabeçalho http que será utilizado na mensagem de retorno.
     * @param status O código http da exceção.
     * @param request A requisição que gerou a exceção.
     * @return {@code ResponseEntity} resposta personalizada para a exceção.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        final ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Erro de validação");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Gerencia exceções do tipo {@link ConstraintViolationException}.
     *
     * @param ex A {@link ConstraintViolationException} ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        final ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Erro de validação");
        return buildResponseEntity(apiError);
    }

    /**
     * Gerencia exceções do tipo {@link NoResultsException}.
     *
     * @param ex A {@link NoResultsException} ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @ExceptionHandler(NoResultsException.class)
    protected ResponseEntity<Object> handleEntityNotFound(NoResultsException ex) {
        final ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Gerencia exceções do tipo {@link UnsupportedFileExtensionException}.
     *
     * @param ex A {@link UnsupportedFileExtensionException} ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @ExceptionHandler(UnsupportedFileExtensionException.class)
    protected ResponseEntity<Object> handlehandleEntityNotFound(UnsupportedFileExtensionException ex) {
        final ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Gerencia exceções do tipo {@link HttpMessageNotWritableException}.
     *
     * @param ex A {@link HttpMessageNotWritableException} ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        final String error = "Erro ao escrever a saída JSON";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Gerencia exceções do tipo {@link EntityNotFoundException}.
     *
     * @param ex A {@link EntityNotFoundException} ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Gerencia exceções genéricas.
     *
     * @param ex A exceção ocorrida.
     * @return {@link ApiError} com as informações da exceção.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        final ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("O parâmetro '%s' com valor '%s' não pode ser convertido para o tipo '%s'",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Cria uma {@link ResponseEntity } com o corpo e código http informados, e sem cabeçalho.
     *
     * @param apiError {@link ApiError} com as informações da exceção.
     * @return {@link ResponseEntity } com o corpo e código http obtidos do {@link ApiError} informado, e sem cabeçalho.
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    public static class Error {

        private final String msgToUser;
        private final String msgToDesenv;

        public Error(String msgToUser, String msgToDesenv) {
            this.msgToUser = msgToUser;
            this.msgToDesenv = msgToDesenv;
        }

        public String getMsgToUser() {
            return msgToUser;
        }

        public String getMsgToDesenv() {
            return msgToDesenv;
        }

    }

}