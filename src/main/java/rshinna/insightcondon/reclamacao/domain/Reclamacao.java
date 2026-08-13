package rshinna.insightcondon.reclamacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reclamacao")
@Getter
@NoArgsConstructor
public class Reclamacao {

    @Id
    @Column(name = "id")
    private UUID reclamacaoId;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "categoria_id")
    private UUID categoriaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "condominio_id", nullable = false)
    private UUID condominioId;

    @Column(nullable = false)
    private boolean anonimo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusReclamacao status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Urgencia urgencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgencia_sugerida_ia", length = 10)
    private Urgencia urgenciaSugeridaIa;

    @Column(name = "score_prioridade", precision = 10, scale = 2)
    private BigDecimal scorePrioridade;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    public Reclamacao(String titulo, String descricao, UUID categoriaId, UUID usuarioId,
                      UUID condominioId, boolean anonimo, Urgencia urgencia) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título da reclamação é obrigatório");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição da reclamação é obrigatório");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }
        if (condominioId == null) {
            throw new IllegalArgumentException("Condomínio é obrigatório");
        }

        this.reclamacaoId = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoriaId = categoriaId;
        this.usuarioId = usuarioId;
        this.condominioId = condominioId;
        this.anonimo = anonimo;
        this.status = StatusReclamacao.ABERTA;
        this.urgencia = urgencia != null ? urgencia : Urgencia.MEDIA;
        this.scorePrioridade = BigDecimal.ZERO;

        Instant agora = Instant.now();
        this.createdAt = agora;
        this.updatedAt = agora;
    }

    public void alterarStatus(StatusReclamacao novoStatus) {
        if (this.status == StatusReclamacao.ARQUIVADA) {
            throw new IllegalStateException("Não é possível alterar o status de uma reclamação arquivada");
        }
        if (this.status == StatusReclamacao.RESOLVIDA && novoStatus == StatusReclamacao.ABERTA) {
            throw new IllegalStateException("Não é possível reabrir uma reclamação resolvida diretamente");
        }

        this.status = novoStatus;
        this.updatedAt = Instant.now();
    }

    public void atualizarScorePrioridade(BigDecimal novoScore) {
        this.scorePrioridade = novoScore;
        this.updatedAt = Instant.now();
    }

    public void aplicarSugestaoIa(UUID categoriaSugerida, Urgencia urgenciaSugeridaIa) {
        this.urgenciaSugeridaIa = urgenciaSugeridaIa;
        if (this.categoriaId == null && categoriaSugerida != null) {
            this.categoriaId = categoriaSugerida;
        }
        this.updatedAt = Instant.now();
    }

    public boolean deveExibirAutor(){
        return !this.anonimo;
    }

    public ReclamacaoId getId(){
        return ReclamacaoId.de(this.reclamacaoId);
    }
}
