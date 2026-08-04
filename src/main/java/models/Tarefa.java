package models;

import interfaces.Temporizador;
import service.JsonSave;

import java.time.Duration;
import java.time.LocalDateTime;

public class Tarefa extends Evento implements Temporizador {
    private LocalDateTime dataEHoraLimite;
    private Boolean estaAtiva, estaConcluida;


    public Tarefa(String nome, String descricao, int pontuacao, LocalDateTime dataEHoraLimite){
        super(nome, descricao, pontuacao);
        this.dataEHoraLimite = dataEHoraLimite;
        this.estaAtiva = true;
        this.estaConcluida = false;
    }

    @Override
    public void iniciarTemporizador() {

    }

    @Override
    public void pausaTemporizador() {

    }

    @Override
    public void pararTemporizador() {
        this.estaAtiva = false;
        JsonSave.salvar(this, "Tarefa" + super.getId());
    }

    @Override
    public Duration getTempoRestante() {
        return null;
    }

    public void adiarTarefa(Duration tempoAdiamento){
        this.dataEHoraLimite.plus(tempoAdiamento);
    }

    public void setEstaAtiva(Boolean estaAtiva) { //Usar para cancelamento de tarefa
        this.estaAtiva = estaAtiva;
    }

    public Boolean getEstaAtiva() {
        return estaAtiva;
    }

    public LocalDateTime getDataEHoraLimite() {
        return dataEHoraLimite;
    }

    public void setDataEHoraLimite(LocalDateTime dataEHoraLimite) {
        this.dataEHoraLimite = dataEHoraLimite;
    }

    public Boolean getEstaConcluida() {
        return estaConcluida;
    }

    public void setEstaConcluida(Boolean estaConcluida) {
        this.estaConcluida = estaConcluida;
    }
}
