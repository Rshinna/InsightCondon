package rshinna.insightcondon.condominio.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rshinna.insightcondon.condominio.domain.Condominio;
import rshinna.insightcondon.condominio.domain.CondominioId;
import rshinna.insightcondon.condominio.infrastructure.CondominioRepository;
import rshinna.insightcondon.shared.exception.RecursoNaoEncontradoException;
import rshinna.insightcondon.shared.exception.RegraDeNegocioException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CondominioService {

    private final CondominioRepository condominioRepository;

    public Condominio criar(String nome, String cnpj, String endereco){
        if(cnpj != null && !cnpj.isBlank() && condominioRepository.existsByCnpj(cnpj)){
            throw new RegraDeNegocioException("Já existe um condomínio cadastrado com este CNPJ:");
        }

        Condominio condominio = new Condominio(nome, cnpj, endereco);
        return condominioRepository.save(condominio);
    }

    @Transactional(readOnly = true)
    public Condominio buscarPorId(CondominioId condominioId){
        return condominioRepository.findById(condominioId.value())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Condomínio não encontrado: " + condominioId));
    }

    @Transactional(readOnly = true)
    public List<Condominio> listarTodos() {
        return condominioRepository.findAll();
    }

    public Condominio atualizar(CondominioId condominioId, String nome, String cnpj, String endereco){
        Condominio condominio = buscarPorId(condominioId);
        condominio.atualizarDados(nome, cnpj, endereco);
        return condominio;
    }
}
