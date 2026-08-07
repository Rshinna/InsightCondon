package rshinna.insightcondon.usuario.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.usuario.application.AuthService;
import rshinna.insightcondon.usuario.infrastructure.web.dto.LoginRequestDTO;
import rshinna.insightcondon.usuario.infrastructure.web.dto.LoginResponseDTO;
import rshinna.insightcondon.usuario.infrastructure.web.dto.VerificarEmailRequestDTO;
import rshinna.insightcondon.usuario.infrastructure.web.dto.VerificarEmailResponseDTO;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verificar-email")
    public ResponseEntity<VerificarEmailResponseDTO> verificarEmail(
            @Valid @RequestBody VerificarEmailRequestDTO dto) {
        return ResponseEntity.ok(authService.verificarContasPorEmail(dto.email()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(
                dto.email(), dto.senha(), CondominioId.de(dto.condominioId()));
        return ResponseEntity.ok(response);
    }
}