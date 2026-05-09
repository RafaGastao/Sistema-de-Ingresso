/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingressos1.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author RafaelG
 */
@Document(collection = "eventos")
public class Evento {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private String data;
    private String horario;
    private String local;
    private int quantidadeIngressosDisponiveis;
    private double valorBase;

    public Evento() {}

    public Evento(String nome, String data, String local, double valorBase) {
        this.nome = nome;
        this.data = data;
        this.local = local;
        this.valorBase = valorBase;
    }

    public Evento(String nome, String descricao, String data, String horario, String local,
                  int quantidadeIngressosDisponiveis, double valorBase) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.quantidadeIngressosDisponiveis = quantidadeIngressosDisponiveis;
        this.valorBase = valorBase;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getQuantidadeIngressosDisponiveis() {
        return quantidadeIngressosDisponiveis;
    }

    public void setQuantidadeIngressosDisponiveis(int quantidadeIngressosDisponiveis) {
        this.quantidadeIngressosDisponiveis = quantidadeIngressosDisponiveis;
    }

    public boolean possuiIngressosDisponiveis() {
        return quantidadeIngressosDisponiveis > 0;
    }

    public void diminuirDisponibilidade() {
        if (!possuiIngressosDisponiveis()) {
            throw new IllegalStateException("Nao ha ingressos disponiveis para este evento.");
        }
        quantidadeIngressosDisponiveis--;
    }

    public void aumentarDisponibilidade() {
        quantidadeIngressosDisponiveis++;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }
}
