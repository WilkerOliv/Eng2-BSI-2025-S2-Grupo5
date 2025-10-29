package projeto.salf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "menu"; // renderiza menu.jsp
    }

    @GetMapping("/categorias")
    public String categorias() {
        return "categorias";
    }

    @GetMapping("/listas")
    public String listas() {
        return "listas";
    }

    @GetMapping("/necessidades")
    public String necessidades() {
        return "necessidades";
    }
}
