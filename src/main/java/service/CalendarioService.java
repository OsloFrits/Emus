package service;

import interfaces.Notificador;
import models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CalendarioService {
    private Calendario calendario;
    EventoAgendavel agendavel;

    public CalendarioService(Calendario calendario) {
        this.calendario = calendario;
    }

    public void guardarEvento(Evento evento){
        calendario.getAgenda().put(evento.getId(), evento);
    }

    public Evento getEvento(int id){
        return calendario.getAgenda().get(id);
    }

    public List<Evento> getEventos(){
        return calendario.getAgenda().values().stream().toList();
    }

    public List<Evento> getEventosPelaData(LocalDateTime dataEHora){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof EventoAgendavel agendavel)
                .filter(evento -> agendavel.getDataEHora().equals(dataEHora))
                .toList();
    }
    public List<Tarefa> getTarefas(){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof Tarefa)
                .map(evento -> (Tarefa) evento)
                .toList();
    }
    public List<Alarme> getAlarmes(){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof Alarme)
                .map(evento -> (Alarme) evento)
                .toList();
    }


    public void deleteEvento(int id){
        if(calendario.getAgenda().containsKey(id)) {
            Evento evento = calendario.getAgenda().get(id);
            evento.salvar();
            calendario.getAgenda().remove(id);
        }else {
            throw new IllegalArgumentException("Evento de id:" + id + " não encontrado");
        }
    }
}
