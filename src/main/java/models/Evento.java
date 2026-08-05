package models;

import interfaces.Temporizador;

public abstract class Evento implements Temporizador {
    private String nome="", descricao="";
    private int pontuacao=0, id;
    private static int proximoId=1;

    public Evento(String nome, String descricao, int pontuacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.pontuacao = pontuacao;
        this.id = proximoId++;
    }

    public int getId() {
        return id;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
