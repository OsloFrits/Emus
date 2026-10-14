package service;

import interfaces.Notificador;
import models.Alarme;
import models.Pomodoro;
import models.Tarefa;

import java.time.Duration;
import java.time.LocalDateTime;

public class EventoService {

    public Alarme criaAlarme(LocalDateTime dataEHora, String nome, String descricao, int pontuacao,
                             TemporizadorService temporizadorService, Notificador notificador){
        return new Alarme(dataEHora, nome, descricao, pontuacao, temporizadorService, notificador);
    }
    public Tarefa criaTarefa(String nome, String descricao, int pontuacao, LocalDateTime dataEHoraLimite,
                             TemporizadorService temporizadorService, Notificador notificador){
        return new Tarefa(nome, descricao, pontuacao, dataEHoraLimite, temporizadorService, notificador);
    }
    public Pomodoro criaPomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco,
                                 Duration tempoDeDescanso, TemporizadorService temporizadorService){
        return new Pomodoro(nome, descricao, pontuacao, tempoDeFoco, tempoDeDescanso, temporizadorService);
    }
}
