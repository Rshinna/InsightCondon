package rshinna.insightcondon.categoria.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "categoria")
@Getter
@NoArgsConstructor
public class Categoria {

    @Id
    @Column(name = "id")
    private UUID categoriaId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(name = "condominio_id")
    private UUID condominioId;

    public Categoria(String nome, String descricao, UUID condominioId) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }

        this.categoriaId = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.condominioId = condominioId;
    }

    public boolean isGlobal() {
        return this.condominioId == null;
    }

    public CategoriaId getId() {
        return CategoriaId.de(this.categoriaId);
    }
}
