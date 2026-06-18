package sp.senai.br.cafearomae.CafeAromaESabor.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sp.senai.br.cafearomae.CafeAromaESabor.model.Produto;
import sp.senai.br.cafearomae.CafeAromaESabor.repository.ProdutoRepository;

import java.util.List;

@Controller
@RequestMapping("/produto") // https://localhost/produto
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // ==================== LISTAGEM ====================

    // GET /produto -> lista todos os produtos
    @GetMapping
    public String listagem(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "produto/listagem"; // templates/produto/listagem.html
    }

    // GET /produto/buscar?nome=cafe -> busca produto por nome
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nome, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        model.addAttribute("produtos", produtos);
        model.addAttribute("nomeBusca", nome);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "produto/listagem";
    }

    // ==================== INSERIR ====================

    // GET /produto/form-inserir -> exibe o formulário de cadastro
    @GetMapping("/form-inserir")
    public String formInserir(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        model.addAttribute("produto", new Produto());
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "produto/form-inserir"; // templates/produto/form-inserir.html
    }

    // POST /produto/salvar -> salva o novo produto
    @PostMapping("/salvar")
    public String salvar(@Valid Produto produto, BindingResult result, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
            return "produto/form-inserir";
        }

        produtoRepository.save(produto);
        return "redirect:/produto"; // redireciona para a listagem após salvar
    }

    // ==================== ALTERAR ====================

    // GET /produto/form-alterar/{id} -> exibe o formulário preenchido para edição
    @GetMapping("/form-alterar/{id}")
    public String formAlterar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));

        model.addAttribute("produto", produto);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "produto/form-alterar"; // templates/produto/form-alterar.html
    }

    // POST /produto/atualizar -> salva as alterações do produto
    @PostMapping("/atualizar")
    public String atualizar(@Valid Produto produto, BindingResult result, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
            return "produto/form-alterar";
        }

        produtoRepository.save(produto);
        return "redirect:/produto";
    }

    // ==================== EXCLUIR ====================

    // GET /produto/excluir/{id} -> exclui o produto pelo id
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        produtoRepository.deleteById(id);
        return "redirect:/produto";
    }
}
