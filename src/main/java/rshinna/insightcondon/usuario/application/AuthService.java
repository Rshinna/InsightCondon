package rshinna.insightcondon.usuario.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.condominio.application.CondominioService;
import rshinna.insightcondon.condominio.domain.Condominio;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.exception.RegraDeNegocioException;
import rshinna.insightcondon.shared.infrastructure.JwtService;
import rshinna.insightcondon.usuario.domain.Usuario;
import rshinna.insightcondon.usuario.infrastructure.web.dto.LoginResponseDTO;
import rshinna.insightcondon.usuario.infrastructure.web.dto.VerificarEmailResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioService usuarioService;
    private final CondominioService condominioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public VerificarEmailResponseDTO verificarContasPorEmail(String email) {
        List<Usuario> contas = usuarioService.buscarContasPorEmail(email);

        if (contas.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma conta encontrada com este e-mail");
        }

        List<VerificarEmailResponseDTO.CondominioResumoDTO> condominios = contas.stream()
                .map(usuario -> {
                    Condominio condominio = condominioService.buscarPorId(
                            CondominioId.de(usuario.getCondominioId()));
                    return new VerificarEmailResponseDTO.CondominioResumoDTO(
                            condominio.getId().toString(),
                            condominio.getNome()
                    );
                })
                .toList();

        return new VerificarEmailResponseDTO(contas.size() > 1, condominios);
    }

    public LoginResponseDTO login(String email, String senha, CondominioId condominioId) {
        Usuario usuario = usuarioService.buscarPorEmailECondominio(email, condominioId);

        if (!passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            throw new RegraDeNegocioException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(
                usuario.getId().value(),
                usuario.getEmail(),
                usuario.getCondominioId(),
                usuario.getPerfil().name()
        );

        return new LoginResponseDTO(
                token,
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getPerfil().name(),
                usuario.getCondominioId().toString()
        );
    }
}