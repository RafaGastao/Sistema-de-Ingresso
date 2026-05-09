package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Evento;
import com.mycompany.ingressos1.service.EventoService;
import com.mycompany.ingressos1.service.IngressoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EventoController {

    private final EventoService eventoService;
    private final IngressoService ingressoService;

    public EventoController(EventoService eventoService, IngressoService ingressoService) {
        this.eventoService = eventoService;
        this.ingressoService = ingressoService;
    }

    @GetMapping("/admin")
    public String painelAdmin(Model model, HttpSession session) {
        if (!usuarioAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("eventos", eventoService.listar());
        return "admin";
    }

    @GetMapping("/admin/novo")
    public String novoEvento(HttpSession session) {
        if (!usuarioAdmin(session)) {
            return "redirect:/login";
        }
        return "cadastrar-evento";
    }

    @PostMapping("/admin/salvar")
    public String salvarEvento(@RequestParam String nome,
                               @RequestParam String descricao,
                               @RequestParam String data,
                               @RequestParam String horario,
                               @RequestParam String local,
                               @RequestParam int quantidadeIngressosDisponiveis,
                               @RequestParam double valorBase,
                               HttpSession session) {
        if (!usuarioAdmin(session)) {
            return "redirect:/login";
        }

        eventoService.criar(nome, descricao, data, horario, local, quantidadeIngressosDisponiveis, valorBase);

        return "redirect:/admin";
    }

    @GetMapping("/eventos")
    public String listarEventosParaUsuario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        model.addAttribute("eventos", eventoService.listar());
        return "eventos";
    }

    @GetMapping("/eventos/comprar/{id}")
    public String comprarEvento(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        Evento evento = eventoService.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "comprar-evento";
    }

    @GetMapping("/admin/eventos/{id}/ingressos")
    public String ingressosPorEvento(@PathVariable String id, Model model, HttpSession session) {
        if (!usuarioAdmin(session)) {
            return "redirect:/login";
        }

        Evento evento = eventoService.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        model.addAttribute("ingressos", ingressoService.listarPorEvento(id));
        model.addAttribute("reservados", ingressoService.contarReservadosPorEvento(id));
        model.addAttribute("utilizados", ingressoService.contarUtilizadosPorEvento(id));
        return "admin-ingressos";
    }

    private boolean usuarioAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("perfilUsuario"));
    }
}
