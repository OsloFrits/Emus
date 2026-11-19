package service;

import models.*;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarioService {
    private EventoAgendavel agendavel;

    public void guardarEvento(Calendario calendario, Evento evento){
        calendario.getAgenda().put(evento.getId(), evento);
    }

    public Evento getEvento(Calendario calendario, int id){
        return calendario.getAgenda().get(id);
    }

    public List<Evento> getEventos(Calendario calendario){
        return calendario.getAgenda().values().stream().toList();
    }

    public List<Evento> getEventosPelaData(Calendario calendario, LocalDateTime dataEHora){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof EventoAgendavel agendavel)
                .filter(evento -> agendavel.getDataEHora().equals(dataEHora))
                .toList();
    }
    public List<Tarefa> getTarefas(Calendario calendario){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof Tarefa)
                .map(evento -> (Tarefa) evento)
                .toList();
    }
    public List<Alarme> getAlarmes(Calendario calendario){
        return calendario.getAgenda().values()
                .stream()
                .filter(evento -> evento instanceof Alarme)
                .map(evento -> (Alarme) evento)
                .toList();
    }

    public void deleteEvento(Calendario calendario, int id){
        if(calendario.getAgenda().containsKey(id)) {
            Evento evento = calendario.getAgenda().get(id);
            evento.salvar();
            calendario.getAgenda().remove(id);
        }else {
            throw new IllegalArgumentException("Evento de id:" + id + " não encontrado");
        }
    }
}
