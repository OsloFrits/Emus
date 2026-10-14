package models;

import enums.EstadoPomodoro;
import service.JsonSave;
import service.TemporizadorService;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Pomodoro extends Evento{
    private Duration tempoDeFoco, tempoDeDescanso;
    private volatile Duration tempoRestante; //volatile: é alterado pela thread do scheduler e lido pela main
    private volatile EstadoPomodoro estadoPomodoro;
    private EstadoPomodoro estadoAntesDaPausa;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> pomodoro;

    public Pomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco, Duration tempoDeDescanso, TemporizadorService temporizadorService) {
        super(nome, descricao, pontuacao);
        this.scheduler = temporizadorService.getScheduler();
        this.tempoDeFoco = tempoDeFoco;
        this.tempoDeDescanso = tempoDeDescanso;
        this.tempoRestante = tempoDeFoco;
        this.estadoPomodoro = EstadoPomodoro.Parado;
    }

    @Override
    public synchronized void iniciarTemporizador() {
        if (estadoPomodoro == EstadoPomodoro.Finalizado || estaContando()) {
            return;
        }

        if (estadoPomodoro == EstadoPomodoro.Parado) {
            tempoRestante = tempoDeFoco;
            estadoPomodoro = EstadoPomodoro.Executando;
        } else if (estadoPomodoro == EstadoPomodoro.Pausado) {
            estadoPomodoro = estadoAntesDaPausa; //Retoma de onde parou, sem resetar o tempoRestante
        }

        pomodoro = scheduler.scheduleAtFixedRate(() -> {
            tempoRestante = tempoRestante.minusSeconds(1);
            System.out.println(estadoPomodoro + " | tempoRestante: " + tempoRestante);
            if (tempoRestante.isZero() || tempoRestante.isNegative()) {
                pararTemporizador();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void pausaTemporizador() {
        if (!estaContando()) {
            return;
        }
        estadoAntesDaPausa = estadoPomodoro;
        estadoPomodoro = EstadoPomodoro.Pausado;
        pomodoro.cancel(false);
    }

    @Override
    public synchronized void pararTemporizador() {
        if (pomodoro != null) {
            pomodoro.cancel(false);
        }

        if (estadoPomodoro == EstadoPomodoro.Executando) {
            estadoPomodoro = EstadoPomodoro.Descansando;
            tempoRestante = tempoDeDescanso;
        } else if (estadoPomodoro == EstadoPomodoro.Descansando) {
            estadoPomodoro = EstadoPomodoro.Finalizado;
            tempoRestante = Duration.ZERO;
        }
        salvar();
    }

    private boolean estaContando() {
        return pomodoro != null && !pomodoro.isDone();
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
