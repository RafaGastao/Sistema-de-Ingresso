package com.mycompany.ingressos1.controller;

import com.mycompany.ingressos1.model.Ingresso;
import com.mycompany.ingressos1.service.IngressoService;
import com.mycompany.ingressos1.service.EventoService;
import com.mycompany.ingressos1.service.QrCodeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IngressoController {

    private final IngressoService ingressoService;
    private final EventoService eventoService;
    private final QrCodeService qrCodeService;

    public IngressoController(IngressoService ingressoService, EventoService eventoService, QrCodeService qrCodeService) {
        this.ingressoService = ingressoService;
        this.eventoService = eventoService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ingressos/novo")
    public String formularioCadastro(HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        return "cadastro";
    }

    @PostMapping("/ingressos/salvar")
    public String salvarIngresso(@RequestParam String tipo,
                                 @RequestParam String eventoId,
                                 Model model,
                                 HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        try {
            String login = (String) session.getAttribute("usuarioLogado");
            String nome = (String) session.getAttribute("nomeUsuario");
            ingressoService.reservarIngresso(eventoId, tipo, login, nome);
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("evento", eventoService.buscarPorId(eventoId).orElse(null));
            model.addAttribute("erro", e.getMessage());
            return "comprar-evento";
        }

        return "redirect:/ingressos";
    }

    @GetMapping("/cliente")
    public String areaCliente(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String login = (String) session.getAttribute("usuarioLogado");
        model.addAttribute("eventos", eventoService.listar());
        model.addAttribute("ingressos", ingressoService.listarPorClienteLogin(login));
        return "cliente";
    }

    @GetMapping("/ingressos")
    public String listarIngressos(Model model, HttpSession session) {

        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String perfil = (String) session.getAttribute("perfilUsuario");
        String nomeUsuario = (String) session.getAttribute("nomeUsuario");
        String loginUsuario = (String) session.getAttribute("usuarioLogado");

        List<Ingresso> ingressos;

        if ("ADMIN".equals(perfil)) {
            ingressos = ingressoService.listarTodos();
        } else {
            ingressos = ingressoService.listarPorClienteLogin(loginUsuario);
            if (ingressos.isEmpty()) {
                ingressos = ingressoService.listarPorCliente(nomeUsuario);
            }
        }

        model.addAttribute("ingressos", ingressos);
        return "lista";
    }

    @GetMapping("/ingressos/{id}")
    public String detalhesIngresso(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Ingresso ingresso = ingressoService.buscarPorId(id).orElse(null);
        model.addAttribute("ingresso", ingresso);
        return "detalhes";
    }

    @GetMapping("/ingressos/pagar/{id}")
    public String pagarIngresso(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Ingresso ingresso = ingressoService.pagarERetornarIngresso(id).orElse(null);
        model.addAttribute("ingresso", ingresso);
        return "imprimir-ingresso";
    }

    @GetMapping("/ingressos/cancelar/{id}")
    public String cancelarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        ingressoService.cancelarIngresso(id);
        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos/utilizar/{id}")
    public String utilizarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        String mensagem = ingressoService.utilizarIngresso(id);
        session.setAttribute("mensagemValidacao", mensagem);
        return "redirect:/admin/validar";
    }

    @GetMapping("/ingressos/deletar/{id}")
    public String deletarIngresso(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        ingressoService.deletar(id);
        return "redirect:/ingressos";
    }

    @GetMapping("/ingressos/qrcode/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> qrCode(@PathVariable String id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return ResponseEntity.status(403).build();
        }

        return ingressoService.buscarPorId(id)
                .map(ingresso -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(qrCodeService.gerarQrCode(ingresso.getCodigoQr())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/validar")
    public String validarQr(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("perfilUsuario"))) {
            return "redirect:/login";
        }

        model.addAttribute("mensagem", session.getAttribute("mensagemValidacao"));
        session.removeAttribute("mensagemValidacao");
        return "validar-ingresso";
    }

    @PostMapping("/admin/validar")
    public String validarQr(@RequestParam String codigoQr, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("perfilUsuario"))) {
            return "redirect:/login";
        }

        String mensagem = ingressoService.validarPorCodigoQr(codigoQr);
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("ingresso", ingressoService.buscarPorCodigoQr(codigoQr).orElse(null));
        return "validar-ingresso";
    }
}
