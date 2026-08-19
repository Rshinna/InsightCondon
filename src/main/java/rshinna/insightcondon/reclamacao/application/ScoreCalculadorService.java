package rshinna.insightcondon.reclamacao.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rshinna.insightcondon.reclamacao.domain.Reclamacao;
import rshinna.insightcondon.reclamacao.domain.Urgencia;
import rshinna.insightcondon.reclamacao.infrastructure.ReclamacaoRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ScoreCalculadorService {

    private static final int JANELA_DIAS = 30;

    private static final BigDecimal PESO_URGENCIA = BigDecimal.valueOf(10);
    private static final BigDecimal PESO_FREQUENCIA = BigDecimal.valueOf(2);
    private static final BigDecimal PESO_IMPACTO = BigDecimal.valueOf(5);

    private final ReclamacaoRepository reclamacaoRepository;

    public BigDecimal calcular(Reclamacao reclamacao) {
        BigDecimal valorUrgencia = BigDecimal.valueOf(mapearUrgencia(reclamacao.getUrgencia()));
        BigDecimal componenteUrgencia = valorUrgencia.multiply(PESO_URGENCIA);

        BigDecimal componenteFrequencia = BigDecimal.ZERO;
        BigDecimal componenteImpacto = BigDecimal.ZERO;

        if (reclamacao.getCategoriaId() != null) {
            Instant desde = Instant.now().minus(JANELA_DIAS, ChronoUnit.DAYS);

            long frequencia = reclamacaoRepository.countAtivasPorCategoria(
                    reclamacao.getCategoriaId(), desde);
            long impacto = reclamacaoRepository.countUsuariosDistintosAtivosPorCategoria(
                    reclamacao.getCategoriaId(), desde);

            componenteFrequencia = BigDecimal.valueOf(frequencia).multiply(PESO_FREQUENCIA);
            componenteImpacto = BigDecimal.valueOf(impacto).multiply(PESO_IMPACTO);
        }

        return componenteUrgencia
                .add(componenteFrequencia)
                .add(componenteImpacto);

    }

    private int mapearUrgencia(Urgencia urgencia) {
        return switch (urgencia) {
            case BAIXA -> 1;
            case MEDIA -> 2;
            case ALTA -> 3;
            case CRITICA -> 4;
        };
    }
}
