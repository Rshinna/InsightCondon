package rshinna.insightcondon.reclamacao.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.categoria.domain.CategoriaId;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.reclamacao.application.ReclamacaoService;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.ReclamacaoId;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.AlterarStatusRequestDTO;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ReclamacaoRequestDTO;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ReclamacaoResponseDTO;
import rshinna.insightcondon.shared.infrastructure.security.ContextoAutenticacao;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;
import rshinna.insightcondon.usuario.application.UsuarioService;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reclamacoes")
@RequiredArgsConstructor
public class ReclamacaoController {

    private final ReclamacaoService reclamacaoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ReclamacaoResponseDTO> registrar(
            @Valid @RequestBody ReclamacaoRequestDTO dto) {
        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();

        UUID categoriaId = dto.categoriaId() != null ? CategoriaId.de(dto.categoriaId()).value() : null;

        Reclamacao reclamacao =
                reclamacaoService.registrar(
                        dto.titulo(),
                        dto.descricao(),
                        categoriaId,
                        UsuarioId.de(autenticado.usuarioId()),
                        CondominioId.de(autenticado.condominioId()),
                        dto.anonimo(),
                        null);

        Usuario autor = usuarioService.buscarPorId(UsuarioId.de(autenticado.usuarioId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReclamacaoResponseDTO.from(reclamacao, autor.getNome()));
    }

    @GetMapping
    public ResponseEntity<List<ReclamacaoResponseDTO>> listar() {
        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();

        List<Reclamacao> reclamacoes = autenticado.perfil().equals("MORADOR")
                ? reclamacaoService.listarParaMorador(UsuarioId.de(autenticado.usuarioId()))
                : reclamacaoService.listarParaSindicoOuAdmin(CondominioId.de(autenticado.condominioId()));

        List<ReclamacaoResponseDTO> response = reclamacoes.stream()
                .map(this::montarResponseComAutor)
                .toList();

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamacaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();
        Reclamacao reclamacao = reclamacaoService.buscarPorId(ReclamacaoId.de(id));

        boolean ehMorador = autenticado.perfil().equals("MORADOR");
        boolean ehAutor = reclamacao.getUsuarioId().equals(autenticado.usuarioId());
        boolean mesmoCondominio = reclamacao.getCondominioId().equals(autenticado.condominioId());

        if (!mesmoCondominio || (ehMorador && !ehAutor)) {
            throw new AccessDeniedException("Você não tem permissão para acessar esta reclamação");
        }
        return ResponseEntity.ok(montarResponseComAutor(reclamacao));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SINDICO', 'ADMIN')")
    public ResponseEntity<ReclamacaoResponseDTO> alterarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AlterarStatusRequestDTO dto) {

        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();
        Reclamacao reclamacao = reclamacaoService.buscarPorId(ReclamacaoId.de(id));

        if(!reclamacao.getCondominioId().equals(autenticado.condominioId())) {
            throw new AccessDeniedException("Você não tem permissão para alterar esta reclamação");
        }

        Reclamacao atualizada = reclamacaoService.alterarStatus(
                ReclamacaoId.de(id), StatusReclamacao.valueOf(dto.status())
        );

        return ResponseEntity.ok(montarResponseComAutor(atualizada));
    }

    private ReclamacaoResponseDTO montarResponseComAutor(Reclamacao reclamacao) {
        String nomeAutor = null;
        if (reclamacao.deveExibirAutor()) {
            Usuario autor = usuarioService.buscarPorId(UsuarioId.de(reclamacao.getUsuarioId()));
            nomeAutor = autor.getNome();
        }
        return ReclamacaoResponseDTO.from(reclamacao, nomeAutor);
    }
}
