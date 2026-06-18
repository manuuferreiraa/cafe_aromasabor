package sp.senai.br.cafearomae.CafeAromaESabor.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sp.senai.br.cafearomae.CafeAromaESabor.model.Movimentacao;
import sp.senai.br.cafearomae.CafeAromaESabor.model.Produto;
import sp.senai.br.cafearomae.CafeAromaESabor.repository.MovimentacaoRepository;
import sp.senai.br.cafearomae.CafeAromaESabor.repository.ProdutoRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/movimentacao") // https://localhost/movimentacao
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // ==================== LISTAGEM / FORMULÁRIO PRINCIPAL ====================

    // GET /movimentacao -> exibe a tela de estoque com a tabela e o formulário
    @GetMapping
    public String estoque(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        // Lista todos os produtos para o select do formulário
        List<Produto> produtos = produtoRepository.findAll();

        // Lista todas as movimentações ordenadas por data
        List<Movimentacao> movimentacoes = movimentacaoRepository.findAll();

        model.addAttribute("movimentacao", new Movimentacao());
        model.addAttribute("produtos", produtos);
        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("tiposMovimentacao", Movimentacao.TipoMovimentacao.values());
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "movimentacao/estoque"; // templates/movimentacao/estoque.html
    }

    // ==================== REGISTRAR MOVIMENTAÇÃO ====================

    // POST /movimentacao/registrar -> salva a movimentação e atualiza o estoque
    @PostMapping("/registrar")
    public String registrar(@Valid Movimentacao movimentacao,
                            BindingResult result,
                            @RequestParam Long produtoId,
                            Model model,
                            HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        if (result.hasErrors()) {
            return prepararTelaEstoque(model, session);
        }

        // Busca o produto no banco
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produtoId));

        // Atualiza a quantidade do produto conforme o tipo de movimentação
        if (movimentacao.getTipo() == Movimentacao.TipoMovimentacao.ENTRADA) {
            produto.setQuantidade(produto.getQuantidade() + movimentacao.getQuantidade());
        } else {
            // Impede quantidade negativa
            int novaQtd = produto.getQuantidade() - movimentacao.getQuantidade();
            if (novaQtd < 0) {
                model.addAttribute("erro", "Quantidade insuficiente em estoque!");
                return prepararTelaEstoque(model, session);
            }
            produto.setQuantidade(novaQtd);
        }

        // Define a data atual se não foi informada
        if (movimentacao.getData() == null) {
            movimentacao.setData(LocalDate.now());
        }

        movimentacao.setProduto(produto);

        // Salva produto atualizado e a movimentação
        produtoRepository.save(produto);
        movimentacaoRepository.save(movimentacao);

        return "redirect:/movimentacao";
    }

    // ==================== EXCLUIR MOVIMENTAÇÃO ====================

    // GET /movimentacao/excluir/{id} -> exclui uma movimentação pelo id
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        movimentacaoRepository.deleteById(id);
        return "redirect:/movimentacao";
    }

    // ==================== MÉTODO AUXILIAR ====================

    // Recarrega a tela de estoque com os dados necessários (usado em caso de erro)
    private String prepararTelaEstoque(Model model, HttpSession session) {
        model.addAttribute("movimentacao", new Movimentacao());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("movimentacoes", movimentacaoRepository.findAll());
        model.addAttribute("tiposMovimentacao", Movimentacao.TipoMovimentacao.values());
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
        return "movimentacao/estoque";
    }
}
