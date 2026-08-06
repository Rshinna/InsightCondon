package rshinna.insightcondon.condominio.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rshinna.insightcondon.condominio.application.CondominioService;
import rshinna.insightcondon.condominio.domain.Condominio;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.condominio.infrastructure.web.dto.CondominioRequestDTO;
import rshinna.insightcondon.condominio.infrastructure.web.dto.CondominioResponseDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/condominios")
@RequiredArgsConstructor
public class CondominioController {

    private final CondominioService condominioService;

    @PostMapping
    public ResponseEntity<CondominioResponseDTO> criar(@Valid @RequestBody CondominioRequestDTO dto) {
        Condominio condominio = condominioService.criar(dto.nome(), dto.cnpj(), dto.endereco());
        return ResponseEntity.status(HttpStatus.CREATED).body(CondominioResponseDTO.from(condominio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CondominioResponseDTO> buscarPorId(@PathVariable UUID id) {
        Condominio condominio = condominioService.buscarPorId(CondominioId.de(id));
        return ResponseEntity.ok(CondominioResponseDTO.from(condominio));
    }

    @GetMapping
    public ResponseEntity<List<CondominioResponseDTO>> listarTodos() {
        List<CondominioResponseDTO> response = condominioService.listarTodos().stream()
                .map(CondominioResponseDTO::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CondominioResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CondominioRequestDTO dto) {
        Condominio condominio = condominioService.atualizar(CondominioId.de(id), dto.nome(), dto.cnpj(), dto.endereco());
        return ResponseEntity.ok(CondominioResponseDTO.from(condominio));
    }
}
