package service;

import interfaces.Notificador;
import models.Alarme;
import models.Evento;
import models.Pomodoro;
import models.Tarefa;

import java.time.Duration;
import java.time.LocalDateTime;

public class EventoService {

    CalendarioService calendarioService;

    public Alarme criaAlarme(String nome, String descricao, LocalDateTime dataEHora, int pontuacao,
                             TemporizadorService temporizadorService, Notificador notificador){
        Alarme alarme = new Alarme(nome, descricao, dataEHora, pontuacao, temporizadorService, notificador);
        calendarioService.guardarEvento(alarme);
        return alarme;
    }
    public Tarefa criaTarefa(String nome, String descricao, LocalDateTime dataEHoraLimite, int pontuacao,
                             TemporizadorService temporizadorService, Notificador notificador){
        Tarefa tarefa = new Tarefa(nome, descricao, dataEHoraLimite, pontuacao, temporizadorService, notificador);
        calendarioService.guardarEvento(tarefa);
        return tarefa;
    }
    public Pomodoro criaPomodoro(String nome, String descricao, Duration tempoDeFoco, Duration tempoDeDescanso,
                                 int pontuacao, TemporizadorService temporizadorService){
        Pomodoro pomodoro = new Pomodoro(nome, descricao, tempoDeFoco, tempoDeDescanso, pontuacao, temporizadorService);
        calendarioService.guardarEvento(pomodoro);
        return pomodoro;
    }
}
