package models;

import interfaces.Temporizador;
import service.JsonSave;

import java.time.Duration;

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

    protected Evento() {
    }

    public int getId() {
        return id;
    }

    @Override
    public void iniciarTemporizador() {

    }

    @Override
    public void pausaTemporizador() {

    }

    @Override
    public void pararTemporizador() {

    }

    @Override
    public Duration getTempoRestante() {
        return null;
    }

    public void salvar(){
        JsonSave.salvar(this, this.getClass().getSimpleName() + id);
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
