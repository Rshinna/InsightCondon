package rshinna.insightcondon.categoria.infrastructure.web.dto;

import rshinna.insightcondon.categoria.domain.Categoria;

public record CategoriaResponseDTO(
    String categoriaId, String nome, String descricao, boolean global) {
  public static CategoriaResponseDTO from(Categoria categoria) {
    return new CategoriaResponseDTO(
        categoria.getId().toString(),
        categoria.getNome(),
        categoria.getDescricao(),
        categoria.isGlobal());
  }
}
