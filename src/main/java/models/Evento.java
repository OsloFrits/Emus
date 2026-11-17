package models;

import interfaces.Temporizador;
import service.JsonSave;
import service.TemporizadorService;

import java.util.concurrent.ScheduledExecutorService;

public abstract class Evento implements Temporizador {
    private String nome="", descricao="";
    private int pontuacao=0, id;
    private static int proximoId=1;
    private ScheduledExecutorService scheduler;

    public Evento(String nome, String descricao, int pontuacao, TemporizadorService temporizadorService) {
        this.nome = nome;
        this.descricao = descricao;
        this.pontuacao = pontuacao;
        this.id = proximoId++;
        this.scheduler = temporizadorService.getScheduler();
    }

    public int getId() {
        return id;
    }

    protected ScheduledExecutorService getScheduler() {
        return scheduler;
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
