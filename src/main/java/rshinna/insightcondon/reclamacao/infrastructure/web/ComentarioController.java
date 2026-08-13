package rshinna.insightcondon.reclamacao.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.reclamacao.application.ComentarioService;
import rshinna.insightcondon.reclamacao.application.ReclamacaoService;
import rshinna.insightcondon.reclamacao.domain.ComentarioReclamacao;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.ReclamacaoId;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ComentarioRequestDTO;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ComentarioResponseDTO;
import rshinna.insightcondon.shared.infrastructure.security.ContextoAutenticacao;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;
import rshinna.insightcondon.usuario.application.UsuarioService;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reclamacoes/{reclamacaoId}/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final ReclamacaoService reclamacaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SINDICO', 'ADMIN')")
    public ResponseEntity<ComentarioResponseDTO> adicionar(
            @PathVariable UUID reclamacaoId,
            @Valid @RequestBody ComentarioRequestDTO dto) {

        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();
        Reclamacao reclamacao = reclamacaoService.buscarPorId(ReclamacaoId.de(reclamacaoId));
        reclamacaoService.validarAcesso(reclamacao, autenticado);

        ComentarioReclamacao comentario = comentarioService.adicionar(
                ReclamacaoId.de(reclamacaoId), UsuarioId.de(autenticado.usuarioId()), dto.texto());

        Usuario autor = usuarioService.buscarPorId(UsuarioId.de(autenticado.usuarioId()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ComentarioResponseDTO.from(comentario, autor.getNome()));
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listar(@PathVariable UUID reclamacaoId) {
        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();
        Reclamacao reclamacao = reclamacaoService.buscarPorId(ReclamacaoId.de(reclamacaoId));
        reclamacaoService.validarAcesso(reclamacao, autenticado);

        List<ComentarioResponseDTO> response = comentarioService.listarPorReclamacao(ReclamacaoId.de(reclamacaoId))
                .stream()
                .map(comentario -> {
                    Usuario autor = usuarioService.buscarPorId(UsuarioId.de(comentario.getUsuarioId()));
                    return ComentarioResponseDTO.from(comentario, autor.getNome());
                })
                .toList();

        return ResponseEntity.ok(response);
    }
}
