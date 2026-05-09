package com.mycompany.ingressos1.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ingressos")
public abstract class Ingresso {

    @Id
    private String id;

    private String eventoId;
    private String clienteLogin;
    private String nomeEvento;
    private String nomeCliente;
    private String dataEvento;
    private String localEvento;
    private String codigoQr;
    private double valorBase;
    private double valorFinal;
    private EstadoIngresso estado;

    public Ingresso() {
        this.estado = EstadoIngresso.RESERVADO;
    }

    public Ingresso(String nomeEvento, String nomeCliente, String dataEvento, double valorBase) {
        this.nomeEvento = nomeEvento;
        this.nomeCliente = nomeCliente;
        this.dataEvento = dataEvento;
        this.valorBase = valorBase;
        this.estado = EstadoIngresso.RESERVADO;
    }

    public Ingresso(String eventoId, String clienteLogin, String nomeEvento, String nomeCliente,
                    String dataEvento, String localEvento, double valorBase) {
        this.eventoId = eventoId;
        this.clienteLogin = clienteLogin;
        this.nomeEvento = nomeEvento;
        this.nomeCliente = nomeCliente;
        this.dataEvento = dataEvento;
        this.localEvento = localEvento;
        this.valorBase = valorBase;
        this.estado = EstadoIngresso.RESERVADO;
        gerarCodigoQr();
    }

    public abstract double calcularValor();

    public abstract String imprimirIngresso();

    public abstract String getTipoIngresso();

    public String getImprimirIngresso() {
        return imprimirIngresso();
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

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(String dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public void gerarCodigoQr() {
        String base = UUID.randomUUID() + "|" + nomeEvento + "|" + nomeCliente + "|" + LocalDateTime.now();
        this.codigoQr = gerarHash(base);
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public EstadoIngresso getEstado() {
        return estado;
    }

    public void setEstado(EstadoIngresso estado) {
        this.estado = estado;
    }

    private String gerarHash(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultado = new StringBuilder();
            for (byte b : hash) {
                resultado.append(String.format("%02x", b));
            }
            return resultado.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Nao foi possivel gerar o codigo do ingresso.", e);
        }
    }
}
