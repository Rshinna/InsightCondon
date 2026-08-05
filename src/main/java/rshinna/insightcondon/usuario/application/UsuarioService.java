package rshinna.insightcondon.usuario.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.exception.RegraDeNegocioException;
import rshinna.insightcondon.usuario.domain.Perfil;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.domain.UsuarioId;
import rshinna.insightcondon.usuario.infrastructure.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario registrar(String nome, String email, String senha, String telefone,
                             Perfil perfil, String unidade, CondominioId condominioId) {

        String emailNormalizado = email.toLowerCase().trim();

        if (usuarioRepository.existsByEmailAndCondominioId(emailNormalizado, condominioId.value())) {
            throw new RegraDeNegocioException("Já existe uma conta com este e-mail neste condomínio");
        }

        String senhaHash = passwordEncoder.encode(senha);

        Usuario usuario = new Usuario(nome, emailNormalizado, senhaHash, telefone,
                perfil, unidade, condominioId.value());

        return usuarioRepository.save(usuario);

    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarContasPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase().trim());
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmailECondominio(String email, CondominioId condominioId) {
        return usuarioRepository.findByEmailAndCondominioId(email.toLowerCase().trim(), condominioId.value())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(UsuarioId usuarioId) {
        return usuarioRepository.findById(usuarioId.value())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}
