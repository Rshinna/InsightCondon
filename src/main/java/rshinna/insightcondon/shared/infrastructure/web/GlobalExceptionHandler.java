package rshinna.insightcondon.shared.infrastructure.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.exception.RegraDeNegocioException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraDeNegocio(RegraDeNegocioException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErroResponseDTO.of(409, "Regra de negócio violada", ex.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErroResponseDTO.of(404, "Recurso não encontrado", ex.getMessage()));
    }

    @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<ErroResponseDTO> handleAccessDenied(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErroResponseDTO.of(403, "Acesso negado",
                        "Você não tem permissão para executar esta ação"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErroResponseDTO.of(400, "Dados inválidos", "Erro de validação nos campos enviados", detalhes));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ErroResponseDTO.of(422, "Violação de regra de domínio", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ErroResponseDTO.of(422, "Violação da regra de condomínio", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenerico(Exception ex) {
        log.error("Erro inesperado não tratado", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroResponseDTO.of(500, "Erro interno", "Ocorreu um erro inesperado. Tente novamente."));
    }
}