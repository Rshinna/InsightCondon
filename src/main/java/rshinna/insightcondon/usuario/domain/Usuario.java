package rshinna.insightcondon.usuario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "usuario",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_usuario_email_condominio",
                columnNames = {"email", "condominio_id"}
        ))
@Getter
@NoArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id")
    private UUID usuarioId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil;

    @Column(length = 20)
    private String unidade;

    @Column(name = "condominio_id", nullable = false)
    private UUID condominioId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Usuario(String nome, String email, String senhaHash, String telefone, Perfil perfil, String unidade, UUID condominioId) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        if (perfil == null) {
            throw new IllegalArgumentException("Perfil é obrigatório");
        }
        if (perfil == Perfil.MORADOR && (unidade == null || unidade.isBlank())) {
            throw new IllegalArgumentException("Unidade é obrigatória para moradores");
        }
        if (condominioId == null) {
            throw new IllegalArgumentException("Condomínio é obrigatório");
        }

        this.usuarioId = UUID.randomUUID();
        this.nome = nome;
        this.email = email.toLowerCase().trim();
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.perfil = perfil;
        this.unidade = unidade;
        this.condominioId = condominioId;
        this.createdAt = Instant.now();

    }

    public UsuarioId getId() {
        return UsuarioId.de(this.usuarioId);
    }
}
