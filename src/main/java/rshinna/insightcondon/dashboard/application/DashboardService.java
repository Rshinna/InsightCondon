package rshinna.insightcondon.dashboard.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rshinna.insightcondon.categoria.application.CategoriaService;
import rshinna.insightcondon.categoria.domain.Categoria;
import rshinna.insightcondon.categoria.domain.CategoriaId;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.dashboard.infrastructure.web.dto.CategoriaContagemDTO;
import rshinna.insightcondon.dashboard.infrastructure.web.dto.DashboardResponseDTO;
import rshinna.insightcondon.reclamacao.application.ReclamacaoService;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.StatusReclamacao;
import rshinna.insightcondon.reclamacao.infrastructure.web.dto.ReclamacaoResponseDTO;
import rshinna.insightcondon.usuario.application.UsuarioService;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.domain.UsuarioId;

import java.time.Duration;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ReclamacaoService reclamacaoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    public DashboardResponseDTO gerar(CondominioId condominioId) {
        List<Reclamacao> reclamacoes = reclamacaoService.listarParaSindicoOuAdmin(condominioId);

        Map<String, Long> porStatus = contarPorStatus(reclamacoes);
        List<CategoriaContagemDTO> porCategoria = contarPorCategoria(reclamacoes);
        List<ReclamacaoResponseDTO> topPrioridades = topReclamacoesAtivas(reclamacoes, 5);
        Double tempoMedioResolucaoHoras = calcularTempoMedioResolucao(reclamacoes);

        return new DashboardResponseDTO(
                reclamacoes.size(), porStatus, porCategoria, topPrioridades, tempoMedioResolucaoHoras);
    }

    private Map<String, Long> contarPorStatus(List<Reclamacao> reclamacoes) {
        Map<String, Long> contagem = new LinkedHashMap<>();
        for (StatusReclamacao status : StatusReclamacao.values()) {
            contagem.put(status.name(), 0L);
        }
        Map<String, Long> reais = reclamacoes.stream()
                .collect(Collectors.groupingBy(r -> r.getStatus().name(), Collectors.counting()));
        contagem.putAll(reais);
        return contagem;
    }

    private List<CategoriaContagemDTO> contarPorCategoria(List<Reclamacao> reclamacoes) {
        Map<UUID, Long> contagemPorCategoriaId = reclamacoes.stream()
                .filter(r -> r.getCategoriaId() != null)
                .collect(Collectors.groupingBy(Reclamacao::getCategoriaId, Collectors.counting()));
        return contagemPorCategoriaId.entrySet().stream()
                .map(entry -> {
                    Categoria categoria = categoriaService.buscarPorId(CategoriaId.de(entry.getKey()));
                    return new CategoriaContagemDTO(
                            categoria.getId().toString(),
                            categoria.getNome(),
                            entry.getValue()
                    );
                })
                .sorted(Comparator.comparingLong(CategoriaContagemDTO::quantidade).reversed())
                .toList();
    }

    private List<ReclamacaoResponseDTO> topReclamacoesAtivas(List<Reclamacao> reclamacoes, int limite) {
        return reclamacoes.stream()
                .filter(r -> r.getStatus() == StatusReclamacao.ABERTA
                        || r.getStatus() == StatusReclamacao.EM_ANDAMENTO)
                .sorted(Comparator.comparing(Reclamacao::getScorePrioridade).reversed())
                .limit(limite)
                .map(this::montarResponseComAutor)
                .toList();
    }

    private Double calcularTempoMedioResolucao(List<Reclamacao> reclamacoes) {
        List<Reclamacao> resolvidas = reclamacoes.stream()
                .filter(r -> r.getStatus() == StatusReclamacao.RESOLVIDA && r.getResolvedAt() != null)
                .toList();

        if (resolvidas.isEmpty()) {
            return null;
        }

        double mediaHoras = resolvidas.stream()
                .mapToLong(r -> Duration.between(r.getCreatedAt(), r.getResolvedAt()).toMinutes())
                .average()
                .orElse(0) / 60.0;

        return Math.round(mediaHoras * 100.0) / 100.0;
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
