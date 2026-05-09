package com.mycompany.ingressos1.service;

import com.mycompany.ingressos1.model.*;
import com.mycompany.ingressos1.repository.EventoRepository;
import com.mycompany.ingressos1.repository.IngressoRepository;
import com.mycompany.ingressos1.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final EventoRepository eventoRepository;
    private final ReservaRepository reservaRepository;

    public IngressoService(IngressoRepository ingressoRepository, EventoRepository eventoRepository,
                           ReservaRepository reservaRepository) {
        this.ingressoRepository = ingressoRepository;
        this.eventoRepository = eventoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Ingresso criarIngresso(String tipo, String nomeEvento, String nomeCliente,
                                  String dataEvento, double valorBase) {

        Ingresso ingresso;

        if (tipo.equalsIgnoreCase("VIP")) {
            ingresso = new IngressoVIP(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else if (tipo.equalsIgnoreCase("MEIA")) {
            ingresso = new IngressoMeia(nomeEvento, nomeCliente, dataEvento, valorBase);
        } else {
            ingresso = new IngressoNormal(nomeEvento, nomeCliente, dataEvento, valorBase);
        }

        double valorFinal = ingresso.calcularValor();
        ingresso.setValorFinal(valorFinal);

        return ingressoRepository.save(ingresso);
    }

    public Ingresso reservarIngresso(String eventoId, String tipo, String clienteLogin, String nomeCliente) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento nao encontrado."));

        boolean jaReservado = ingressoRepository.existsByEventoIdAndClienteLoginAndEstadoNot(
                eventoId, clienteLogin, EstadoIngresso.CANCELADO);

        if (jaReservado) {
            throw new IllegalStateException("Voce ja possui uma reserva ativa para este evento.");
        }

        if (!evento.possuiIngressosDisponiveis()) {
            throw new IllegalStateException("Nao ha ingressos disponiveis para este evento.");
        }

        Ingresso ingresso = criarPorTipo(tipo, evento, clienteLogin, nomeCliente);
        ingresso.setValorFinal(ingresso.calcularValor());

        Ingresso ingressoSalvo = ingressoRepository.save(ingresso);

        Reserva reserva = new Reserva(evento.getId(), clienteLogin, ingressoSalvo.getId());
        reservaRepository.save(reserva);

        evento.diminuirDisponibilidade();
        eventoRepository.save(evento);

        return ingressoSalvo;
    }

    public List<Ingresso> listarTodos() {
        return ingressoRepository.findAll();
    }

    public List<Ingresso> listarPorCliente(String nomeCliente) {
        return ingressoRepository.findByNomeCliente(nomeCliente);
    }

    public List<Ingresso> listarPorClienteLogin(String clienteLogin) {
        return ingressoRepository.findByClienteLogin(clienteLogin);
    }

    public List<Ingresso> listarPorEvento(String eventoId) {
        return ingressoRepository.findByEventoId(eventoId);
    }

    public Optional<Ingresso> buscarPorId(String id) {
        return ingressoRepository.findById(id);
    }

    public Optional<Ingresso> pagarERetornarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
            atualizarStatusReserva(ingresso.getId(), EstadoIngresso.PAGO);
            return Optional.of(ingresso);
        }

        return Optional.empty();
    }

    public void pagarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            ingresso.setEstado(EstadoIngresso.PAGO);
            ingressoRepository.save(ingresso);
            atualizarStatusReserva(ingresso.getId(), EstadoIngresso.PAGO);
        }
    }

    public void cancelarIngresso(String id) {
        Optional<Ingresso> ingressoOptional = ingressoRepository.findById(id);

        if (ingressoOptional.isPresent()) {
            Ingresso ingresso = ingressoOptional.get();
            devolverIngressoAoEventoSeNecessario(ingresso);
            ingresso.setEstado(EstadoIngresso.CANCELADO);
            ingressoRepository.save(ingresso);
            atualizarStatusReserva(ingresso.getId(), EstadoIngresso.CANCELADO);
        }
    }

    public String utilizarIngresso(String id) {
        return ingressoRepository.findById(id)
                .map(this::validarUso)
                .orElse("Ingresso nao encontrado.");
    }

    public String validarPorCodigoQr(String codigoQr) {
        return ingressoRepository.findByCodigoQr(codigoQr)
                .map(this::validarUso)
                .orElse("QR Code invalido ou ingresso nao encontrado.");
    }

    public long contarReservadosPorEvento(String eventoId) {
        return ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.RESERVADO)
                + ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.CONFIRMADO)
                + ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.PAGO);
    }

    public long contarUtilizadosPorEvento(String eventoId) {
        return ingressoRepository.countByEventoIdAndEstado(eventoId, EstadoIngresso.UTILIZADO);
    }

    public Optional<Ingresso> buscarPorCodigoQr(String codigoQr) {
        return ingressoRepository.findByCodigoQr(codigoQr);
    }

    private Ingresso criarPorTipo(String tipo, Evento evento, String clienteLogin, String nomeCliente) {
        String dataEvento = evento.getData();
        if (evento.getHorario() != null && !evento.getHorario().isBlank()) {
            dataEvento += " " + evento.getHorario();
        }

        if (tipo.equalsIgnoreCase("VIP")) {
            return new IngressoVIP(evento.getId(), clienteLogin, evento.getNome(), nomeCliente,
                    dataEvento, evento.getLocal(), evento.getValorBase());
        }
        if (tipo.equalsIgnoreCase("MEIA")) {
            return new IngressoMeia(evento.getId(), clienteLogin, evento.getNome(), nomeCliente,
                    dataEvento, evento.getLocal(), evento.getValorBase());
        }
        return new IngressoNormal(evento.getId(), clienteLogin, evento.getNome(), nomeCliente,
                dataEvento, evento.getLocal(), evento.getValorBase());
    }

    private String validarUso(Ingresso ingresso) {
        if (ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
            return "Ingresso valido, mas ja utilizado anteriormente.";
        }
        if (ingresso.getEstado() == EstadoIngresso.CANCELADO) {
            return "Ingresso cancelado. Entrada nao permitida.";
        }
        if (ingresso.getEstado() == EstadoIngresso.RESERVADO) {
            return "Ingresso apenas reservado. Confirme o ingresso antes da entrada.";
        }

        ingresso.setEstado(EstadoIngresso.UTILIZADO);
        ingressoRepository.save(ingresso);
        atualizarStatusReserva(ingresso.getId(), EstadoIngresso.UTILIZADO);
        return "Ingresso valido. Entrada liberada e ingresso marcado como utilizado.";
    }

    private void atualizarStatusReserva(String ingressoId, EstadoIngresso status) {
        reservaRepository.findByIngressoId(ingressoId).ifPresent(reserva -> {
            reserva.setStatus(status);
            reservaRepository.save(reserva);
        });
    }

    private void devolverIngressoAoEventoSeNecessario(Ingresso ingresso) {
        if (ingresso.getEventoId() == null || ingresso.getEstado() == EstadoIngresso.CANCELADO
                || ingresso.getEstado() == EstadoIngresso.UTILIZADO) {
            return;
        }

        eventoRepository.findById(ingresso.getEventoId()).ifPresent(evento -> {
            evento.aumentarDisponibilidade();
            eventoRepository.save(evento);
        });
    }

    public void deletar(String id) {
        ingressoRepository.deleteById(id);
    }
}
