package models;

import interfaces.Temporizador;
import service.JsonSave;
import service.TemporizadorService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Tarefa extends Evento implements Temporizador {
    private LocalDateTime dataEHoraLimite;
    private Boolean estaAtiva, estaConcluida;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> tarefa;


    public Tarefa(String nome, String descricao, int pontuacao, LocalDateTime dataEHoraLimite, TemporizadorService temporizadorService){
        super(nome, descricao, pontuacao);
        this.scheduler = temporizadorService.getScheduler();
        this.dataEHoraLimite = dataEHoraLimite;
        this.estaAtiva = true;
        this.estaConcluida = false;
    }

    @Override
    public void iniciarTemporizador() {
        Duration atraso = Duration.between(LocalDateTime.now(), dataEHoraLimite);
        tarefa = scheduler.schedule(
            this::prazoAtingido,
            atraso.getSeconds(),
            TimeUnit.SECONDS
        );
    }

    public void prazoAtingido(){
        System.out.println("Tarefa de ID:" + getId() + " teve o prazo atingido");
    }

    @Override
    public void pausaTemporizador() {
        estaAtiva = false;
        if(tarefa != null){
            tarefa.cancel(false);
        }
    }

    @Override
    public void pararTemporizador() {
        this.estaAtiva = false;
        this.estaConcluida = true;
        JsonSave.salvar(this, "Tarefa" + super.getId());
    }

    @Override
    public Duration getTempoRestante() {
        return null;
    }

    public void adiarTarefa(Duration tempoAdiamento){
        this.dataEHoraLimite =  this.dataEHoraLimite.plus(tempoAdiamento);
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
