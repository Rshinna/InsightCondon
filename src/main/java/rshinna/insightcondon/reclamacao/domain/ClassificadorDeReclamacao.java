package rshinna.insightcondon.reclamacao.domain;

public interface ClassificadorDeReclamacao {
    ClassificacaoIA classificar(String titulo, String descricao);
}
