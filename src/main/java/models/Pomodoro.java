package models;

import enums.EstadoPomodoro;
import interfaces.Temporizador;
import service.JsonSave;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Pomodoro extends Evento implements Temporizador {
    private Duration tempoDeFoco, tempoDeDescanso, tempoRestante;
    private EstadoPomodoro estadoPomodoro;
    private  ScheduledExecutorService scheduler;

    public Pomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco, Duration tempoDeDescanso) {
        super(nome, descricao, pontuacao);
        criarSchedule();
        this.tempoDeFoco = tempoDeFoco;
        this.tempoDeDescanso = tempoDeDescanso;
        this.estadoPomodoro = EstadoPomodoro.Parado;
    }

    @Override
    public void iniciarTemporizador() {
        if(estadoPomodoro == EstadoPomodoro.Parado) {
            tempoRestante = tempoDeFoco;
        }else if(estadoPomodoro == EstadoPomodoro.Descansando) {
            tempoRestante = tempoDeDescanso;
        }
        if(scheduler.isTerminated() || scheduler.isShutdown()) {
            criarSchedule();
        }
            scheduler.scheduleAtFixedRate(() -> {
                tempoRestante = tempoRestante.minusSeconds(1);

                //System.out.println(tempoRestante); //Apenas para ver se esta funcionando

                if (tempoRestante.isZero() || tempoRestante.isNegative()) {
                    scheduler.shutdown();
                    finalizarTemporizador();
                }
            }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void pausaTemporizador() {
        estadoPomodoro = EstadoPomodoro.Pausado;
        scheduler.shutdownNow();
    }

    @Override
    public void finalizarTemporizador() {
        estadoPomodoro = EstadoPomodoro.Finalizado;
        JsonSave.salvar(this, "Pomodoro" + super.getId());
    }

    private void criarSchedule(){
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public EstadoPomodoro getEstadoPomodoro() {
        return estadoPomodoro;
    }

    public Duration getTempoRestante() {
        return tempoRestante;
    }

    public Duration getTempoDeFoco() {
        return tempoDeFoco;
    }

    public void setTempoDeFoco(Duration tempoDeFoco) {
        this.tempoDeFoco = tempoDeFoco;
    }

    public Duration getTempoDeDescanso() {
        return tempoDeDescanso;
    }

    public void setTempoDeDescanso(Duration tempoDeDescanso) {
        this.tempoDeDescanso = tempoDeDescanso;
    }

}
