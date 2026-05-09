package com.mycompany.ingressos1.model;

public class Cliente extends Usuario {

    public Cliente() {
        super();
    }

    public Cliente(String nome, String login, String senha) {
        super(nome, login, senha, PerfilUsuario.USUARIO);
    }
}
