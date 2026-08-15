package rshinna.insightcondon.reclamacao.infrastructure.ia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import rshinna.insightcondon.reclamacao.domain.ClassificacaoIA;
import rshinna.insightcondon.reclamacao.domain.ClassificadorDeReclamacao;
import rshinna.insightcondon.reclamacao.domain.Urgencia;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GeminiClassificadorAdapter implements ClassificadorDeReclamacao {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public GeminiClassificadorAdapter(
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    @Override
    public ClassificacaoIA classificar(String titulo, String descricao) {
        try {
            String prompt = montarPrompt(titulo, descricao);
            String respostaBruta = chamarGemini(prompt);
            Urgencia urgencia = interpretarResposta(respostaBruta);
            return new ClassificacaoIA(urgencia);
        } catch (Exception e) {
            log.warn("Falha ao classificar reclamação via IA, usando fallback. Motivo: {}", e.getMessage());
            return new ClassificacaoIA(null);
        }
    }

    private String montarPrompt(String titulo, String descricao) {
        return """
                Você é um assistente que classifica reclamações de condomínio por urgência.
                
                Analise o título e a descrição abaixo e responda APENAS com uma destas palavras,
                sem explicação adicional: BAIXA, MEDIA, ALTA ou CRITICA.
                
                Critérios:
                - CRITICA: risco à segurança ou saúde imediato (ex: incêndio, vazamento de gás, desabamento)
                - ALTA: problema que afeta vários moradores ou piora rapidamente (ex: infiltração grande, elevador quebrado)
                - MEDIA: problema real mas não urgente (ex: barulho, manutenção pontual)
                - BAIXA: incômodo menor, sem urgência (ex: sugestão, reclamação estética)
                
                Título: %s
                Descrição: %s
                
                Responda apenas com a palavra da classificação.
                """.formatted(titulo, descricao);
    }

    @SuppressWarnings("unchecked")
    private String chamarGemini(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("/models/{model}:generateContent?key={apiKey}", model, apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Object> candidates = (List<Object>) response.get("candidates");
        Map<String, Object> primeiroCandidato = (Map<String, Object>) candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) primeiroCandidato.get("content");
        List<Object> parts = (List<Object>) content.get("parts");
        Map<String, Object> primeiraPart = (Map<String, Object>) parts.get(0);

        return ((String) primeiraPart.get("text")).trim();
    }

    private Urgencia interpretarResposta(String resposta) {
        String limpo = resposta.toUpperCase().replaceAll("[^A-Z]", "");
        try {
            return Urgencia.valueOf(limpo);
        } catch (IllegalArgumentException e) {
            log.warn("Resposta da IA não reconhecida como urgência válida: '{}'", resposta);
            return null;
        }
    }
}