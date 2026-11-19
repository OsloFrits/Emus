package service;

import interfaces.Notificador;
import models.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class EventoService {

    CalendarioService calendarioService;

    public Alarme criaAlarme(Usuario usuario, String nome, String descricao, LocalDateTime dataEHora, int pontuacao,
                             TemporizadorService temporizadorService, Notificador notificador){
        Alarme alarme = new Alarme(nome, descricao, dataEHora, pontuacao, temporizadorService, notificador);
        calendarioService.guardarEvento(usuario.getAgenda(), alarme);
        return alarme;
    }
    public Tarefa criaTarefa(Usuario usuario, String nome, String descricao, LocalDateTime dataEHoraLimite, int pontuacao,
                             TemporizadorService temporizadorService, Notificador notificador){
        Tarefa tarefa = new Tarefa(nome, descricao, dataEHoraLimite, pontuacao, temporizadorService, notificador);
        calendarioService.guardarEvento(usuario.getAgenda(), tarefa);
        return tarefa;
    }
    public Pomodoro criaPomodoro(Usuario usuario, String nome, String descricao, Duration tempoDeFoco, Duration tempoDeDescanso,
                                 int pontuacao, TemporizadorService temporizadorService){
        Pomodoro pomodoro = new Pomodoro(nome, descricao, tempoDeFoco, tempoDeDescanso, pontuacao, temporizadorService);
        calendarioService.guardarEvento(usuario.getAgenda(), pomodoro);
        return pomodoro;
    }
}
