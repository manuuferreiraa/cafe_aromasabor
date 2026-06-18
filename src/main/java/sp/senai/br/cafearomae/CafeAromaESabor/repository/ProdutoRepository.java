package sp.senai.br.cafearomae.CafeAromaESabor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sp.senai.br.cafearomae.CafeAromaESabor.model.Produto;

import java.time.LocalDate;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca produto pelo nome (contendo o texto, ignorando maiúsculas/minúsculas)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Produtos com quantidade zero ou nula (em falta)
    @Query("SELECT p FROM Produto p WHERE p.quantidade = 0 OR p.quantidade IS NULL")
    List<Produto> findProdutosEmFalta();

    // Produtos com validade nos próximos 30 dias
    @Query("SELECT p FROM Produto p WHERE p.validade BETWEEN :hoje AND :limite")
    List<Produto> findProdutosComValidadeProxima(LocalDate hoje, LocalDate limite);
}
