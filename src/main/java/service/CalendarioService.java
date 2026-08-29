package service;

import models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CalendarioService {
    private Calendario calendario;
    private Map<Integer, Evento> agenda;
    EventoAgendavel agendavel;

    public CalendarioService(Calendario calendario) {
        this.calendario = calendario;
        this.agenda = calendario.getAgenda();
    }

    public Evento getEvento(int id){
        return agenda.get(id);
    }

    public List<Evento> getEventosPelaData(LocalDateTime dataEHora){
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

    public EventoAgendavel criarEventoAgendavel(String nome, String descricao, int pontuacao){
        return null;
    }

    public void deleteEvento(int id){
        if(agenda.containsKey(id)) {
            Evento evento = agenda.get(id);
            evento.salvar();
            agenda.remove(id);
        }else {
            throw new IllegalArgumentException("Evento de id:" + id + " não encontrado");
        }
    }
}
