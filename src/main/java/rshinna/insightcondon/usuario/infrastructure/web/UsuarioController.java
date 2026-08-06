package rshinna.insightcondon.usuario.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.usuario.application.UsuarioService;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.domain.UsuarioId;
import rshinna.insightcondon.usuario.infrastructure.web.dto.UsuarioRequestDTO;
import rshinna.insightcondon.usuario.infrastructure.web.dto.UsuarioResponseDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = usuarioService.registrar(
                dto.nome(),
                dto.email(),
                dto.senha(),
                dto.telefone(),
                dto.perfil(),
                dto.unidade(),
                CondominioId.de(dto.condominioId())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.from(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        Usuario usuario = usuarioService.buscarPorId(UsuarioId.de(id));
        return ResponseEntity.ok(UsuarioResponseDTO.from(usuario));
    }
}