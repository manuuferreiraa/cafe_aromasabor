package sp.senai.br.cafearomae.CafeAromaESabor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // GET /login -> exibe o formulário de login (Spring Security processa o POST)
    @GetMapping("/login")
    public String exibirLogin() {
        return "login"; // src/main/resources/templates/login.html
    }

    // GET / -> redireciona para /login
    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }
}
