package sp.senai.br.cafearomae.CafeAromaESabor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sp.senai.br.cafearomae.CafeAromaESabor.model.Movimentacao;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Busca todas as movimentações de um produto específico
    List<Movimentacao> findByProdutoIdOrderByDataDesc(Long produtoId);

    // Busca movimentações por tipo (ENTRADA ou SAIDA)
    List<Movimentacao> findByTipo(Movimentacao.TipoMovimentacao tipo);
}
