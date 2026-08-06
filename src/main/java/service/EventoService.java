package service;

import models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventoService {
    private Calendario calendario;
    private Map<Integer, Evento> agenda;
    private Alarme alarme;
    private Tarefa tarefa;
    EventoAgendavel agendavel;

    public EventoService(Calendario calendario) {
        this.calendario = calendario;
        this.agenda = calendario.getAgenda();
    }

    public Evento getEvento(int id){
        return agenda.get(id);
    }

    public List<Evento> getEventoPelaData(LocalDateTime dataEHora){
        return agenda.values()
                .stream()
                .filter(evento -> evento instanceof EventoAgendavel agendavel)
                .filter(evento -> agendavel.getDataEHora().equals(dataEHora))
                .toList();
    }
    public List<Tarefa> getTarefas(){
        return agenda.values()
                .stream()
                .filter(evento -> evento instanceof Tarefa)
                .map(evento -> (Tarefa) evento)
                .toList();
    }
    public List<Alarme> getAlarmes(){
        return agenda.values()
                .stream()
                .filter(evento -> evento instanceof Alarme)
                    .map(evento -> (Alarme) evento)
                .toList();
    }
}
