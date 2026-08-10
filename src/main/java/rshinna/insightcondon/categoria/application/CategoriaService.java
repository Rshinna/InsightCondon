package rshinna.insightcondon.categoria.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.categoria.domain.Categoria;
import rshinna.insightcondon.categoria.domain.CategoriaId;
import rshinna.insightcondon.categoria.infrastructure.CategoriaRepository;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

  private final CategoriaRepository categoriaRepository;

  public Categoria criar(String nome, String descricao, CondominioId condominioId) {
    Categoria categoria = new Categoria(nome, descricao, condominioId.value());
    return categoriaRepository.save(categoria);
  }

  @Transactional(readOnly = true)
  public List<Categoria> listarDisponiveisParaCondominio(CondominioId condominioId) {
    return categoriaRepository.findGlobaisECondominio(condominioId.value());
  }

  @Transactional(readOnly = true)
  public Categoria buscarPorId(CategoriaId categoriaId) {
    return categoriaRepository
        .findById(categoriaId.value())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
  }
}
