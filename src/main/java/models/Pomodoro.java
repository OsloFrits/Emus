package models;

import enums.EstadoPomodoro;
import service.JsonSave;
import service.TemporizadorService;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Pomodoro extends Evento{
    private Duration tempoDeFoco, tempoDeDescanso, tempoRestante;
    private EstadoPomodoro estadoPomodoro;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> pomodoro;

    public Pomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco, Duration tempoDeDescanso, TemporizadorService temporizadorService) {
        super(nome, descricao, pontuacao);
        this.scheduler = temporizadorService.getScheduler();
        this.tempoDeFoco = tempoDeFoco;
        this.tempoDeDescanso = tempoDeDescanso;
        this.estadoPomodoro = EstadoPomodoro.Parado;
    }

    @Override
    public void iniciarTemporizador() {
        if (estadoPomodoro == EstadoPomodoro.Parado) {
            tempoRestante = tempoDeFoco;
            estadoPomodoro = EstadoPomodoro.Executando;
        } else if (estadoPomodoro == EstadoPomodoro.Descansando) {
            tempoRestante = tempoDeDescanso;
        }

        pomodoro = scheduler.scheduleAtFixedRate(() -> {
            tempoRestante = tempoRestante.minusSeconds(1);
            System.out.println(estadoPomodoro + " | tempoRestante: " + tempoRestante);
            if (tempoRestante.isZero() || tempoRestante.isNegative()) {
                pomodoro.cancel(false);
                pararTemporizador();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void pausaTemporizador() {
        estadoPomodoro = EstadoPomodoro.Pausado;
        if (pomodoro != null) {
            pomodoro.cancel(false);
        }
    }

    @Override
    public void pararTemporizador() {
        if (estadoPomodoro == EstadoPomodoro.Executando) {
            estadoPomodoro = EstadoPomodoro.Descansando;
        } else if (estadoPomodoro == EstadoPomodoro.Descansando) {
            estadoPomodoro = EstadoPomodoro.Finalizado;
        }
        salvar();
    }

    public EstadoPomodoro getEstadoPomodoro() {
        return estadoPomodoro;
    }

    @Override
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
