package sp.senai.br.cafearomae.CafeAromaESabor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sp.senai.br.cafearomae.CafeAromaESabor.repository.ProdutoRepository;

import java.time.LocalDate;

@Controller
public class HomeController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // GET /home -> exibe o painel principal com resumo do estoque
    // A autenticação é gerenciada pelo Spring Security (não precisa verificar session)
    @GetMapping("/home")
    public String home(Model model) {

        // Conta produtos em falta (quantidade = 0)
        long produtosEmFalta = produtoRepository.findProdutosEmFalta().size();

        // Conta produtos com validade nos próximos 30 dias
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);
        long validadeProxima = produtoRepository.findProdutosComValidadeProxima(hoje, limite).size();

        // Conta total de itens somando a quantidade de todos os produtos
        long totalEstoque = produtoRepository.findAll()
                .stream()
                .mapToLong(p -> p.getQuantidade() != null ? p.getQuantidade() : 0)
                .sum();

        model.addAttribute("produtosEmFalta", produtosEmFalta);
        model.addAttribute("validadeProxima", validadeProxima);
        model.addAttribute("totalEstoque", totalEstoque);

        return "home"; // src/main/resources/templates/home.html
    }
}
