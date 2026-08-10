package rshinna.insightcondon.shared.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class ContextoAutenticacao {

  private ContextoAutenticacao() {}

  public static UsuarioAutenticado usuarioLogado() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(principal instanceof UsuarioAutenticado usuarioAutenticado)) {
      throw new IllegalStateException("Nenhum usuário autenticado no contexto atual");
    }

    return usuarioAutenticado;
  }
}
