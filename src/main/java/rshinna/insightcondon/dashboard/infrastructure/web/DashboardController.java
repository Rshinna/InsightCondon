package rshinna.insightcondon.dashboard.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.dashboard.application.DashboardService;
import rshinna.insightcondon.dashboard.infrastructure.web.dto.DashboardResponseDTO;
import rshinna.insightcondon.shared.infrastructure.security.ContextoAutenticacao;
import rshinna.insightcondon.shared.infrastructure.security.UsuarioAutenticado;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SINDICO', 'ADMIN')")
    public ResponseEntity<DashboardResponseDTO> gerar() {
        UsuarioAutenticado autenticado = ContextoAutenticacao.usuarioLogado();
        DashboardResponseDTO dashboard = dashboardService.gerar(CondominioId.de(autenticado.condominioId()));
        return ResponseEntity.ok(dashboard);
    }
}
