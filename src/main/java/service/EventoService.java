package service;

import models.Alarme;
import models.Pomodoro;
import models.Tarefa;

import java.time.Duration;
import java.time.LocalDateTime;

public class EventoService {

    public Alarme criaAlarme(LocalDateTime dataEHora, String nome,
                             String descricao, int pontuacao, TemporizadorService temporizadorService){
        return new Alarme(dataEHora, nome, descricao, pontuacao, temporizadorService);
    }
    public Tarefa criaTarefa(String nome, String descricao, int pontuacao,
                             LocalDateTime dataEHoraLimite, TemporizadorService temporizadorService){
        return new Tarefa(nome, descricao, pontuacao, dataEHoraLimite, temporizadorService);
    }
    public Pomodoro criaPomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco,
                                 Duration tempoDeDescanso, TemporizadorService temporizadorService){
        return new Pomodoro(nome, descricao, pontuacao, tempoDeFoco, tempoDeDescanso, temporizadorService);
    }
}
