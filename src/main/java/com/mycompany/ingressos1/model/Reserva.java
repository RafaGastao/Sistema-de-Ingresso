package com.mycompany.ingressos1.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservas")
public class Reserva {

    @Id
    private String id;

    private String eventoId;
    private String clienteLogin;
    private String ingressoId;
    private EstadoIngresso status;
    private LocalDateTime dataReserva;

    public Reserva() {
    }

    public Reserva(String eventoId, String clienteLogin, String ingressoId) {
        this.eventoId = eventoId;
        this.clienteLogin = clienteLogin;
        this.ingressoId = ingressoId;
        this.status = EstadoIngresso.RESERVADO;
        this.dataReserva = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getClienteLogin() {
        return clienteLogin;
    }

    public void setClienteLogin(String clienteLogin) {
        this.clienteLogin = clienteLogin;
    }

    public String getIngressoId() {
        return ingressoId;
    }

    public void setIngressoId(String ingressoId) {
        this.ingressoId = ingressoId;
    }

    public EstadoIngresso getStatus() {
        return status;
    }

    public void setStatus(EstadoIngresso status) {
        this.status = status;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }
}
