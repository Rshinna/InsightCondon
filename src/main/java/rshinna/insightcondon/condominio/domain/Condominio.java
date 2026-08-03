package rshinna.insightcondon.condominio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "condominio")
@Getter
@NoArgsConstructor
public class Condominio {

    @Id
    @Column(name = "id")
    private UUID condominioId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 18)
    private String cnpj;

    @Column(length = 255)
    private String endereco;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Condominio(String nome, String cnpj, String endereco) {
        if(nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do condomínio é obrigatório");
        }
        this.condominioId = UUID.randomUUID();
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.createdAt = Instant.now();
    }

    public void atualizarDados(String nome, String cnpj, String endereco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do condomínio é obrigatório");
        }
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    public CondominioId getId() {
        return CondominioId.de(this.condominioId);
    }
}
