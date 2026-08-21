package rshinna.insightcondon.dashboard.infrastructure.web.dto;

import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ReclamacaoResponseDTO;

import java.util.List;
import java.util.Map;

public record DashboardResponseDTO(
        long totalReclamacoes,
        Map<String, Long> porStatus,
        List<CategoriaContagemDTO> porCategoria,
        List<ReclamacaoResponseDTO> topPrioridades,
        Double tempoMedioResolucaoHoras
) {
}
