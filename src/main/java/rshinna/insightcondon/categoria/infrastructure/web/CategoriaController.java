package rshinna.insightcondon.categoria.infrastructure.web;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rshinna.insightcondon.categoria.application.CategoriaService;
import rshinna.insightcondon.categoria.domain.Categoria;
import rshinna.insightcondon.categoria.infrastructure.web.dto.CategoriaRequestDTO;
import rshinna.insightcondon.categoria.infrastructure.web.dto.CategoriaResponseDTO;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.shared.infrastructure.security.ContextoAutenticacao;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

  private final CategoriaService categoriaService;

  @PostMapping
  @PreAuthorize("hasAnyRole('SINDICO', 'ADMIN')")
  public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {

    UsuarioAutenticado usuario = ContextoAutenticacao.usuarioLogado();

    Categoria categoria =
        categoriaService.criar(
            dto.nome(), dto.descricao(), CondominioId.de(usuario.condominioId()));

    return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.from(categoria));
  }

  @GetMapping
  public ResponseEntity<List<CategoriaResponseDTO>> listar() {

    UsuarioAutenticado usuario = ContextoAutenticacao.usuarioLogado();

    List<CategoriaResponseDTO> response =
        categoriaService
            .listarDisponiveisParaCondominio(CondominioId.de(usuario.condominioId()))
            .stream()
            .map(CategoriaResponseDTO::from)
            .toList();

    return ResponseEntity.ok(response);
  }
}
