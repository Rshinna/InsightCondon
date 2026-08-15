package rshinna.insightcondon.reclamacao.infrastructure.web.dto;

import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;
import rshinna.insightcondon.reclamacao.domain.Urgencia;

import java.math.BigDecimal;
import java.time.Instant;

public record ReclamacaoResponseDTO(
        String reclamacaoId,
        String titulo,
        String descricao,
        String categoriaId,
        String autorNome,
        boolean anonimo,
        StatusReclamacao status,
        Urgencia urgencia,
        Urgencia urgenciaSugeridaIa,
        BigDecimal scoreProridade,
        Instant createdAt,
        Instant resolvedAt
) {
    public static ReclamacaoResponseDTO from(Reclamacao reclamacao, String nomeAutor) {
        return new ReclamacaoResponseDTO(
                reclamacao.getId().toString(),
                reclamacao.getTitulo(),
                reclamacao.getDescricao(),
                reclamacao.getCategoriaId() != null ? reclamacao.getCategoriaId().toString() : null,
                reclamacao.deveExibirAutor() ? nomeAutor : null,
                reclamacao.isAnonimo(),
                reclamacao.getStatus(),
                reclamacao.getUrgencia(),
                reclamacao.getUrgenciaSugeridaIa(),
                reclamacao.getScorePrioridade(),
                reclamacao.getCreatedAt(),
                reclamacao.getResolvedAt()
        );
    }
}
