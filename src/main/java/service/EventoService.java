package service;

import models.Alarme;
import models.Calendario;
import models.Evento;
import models.Tarefa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventoService {
    private Calendario calendario;
    private Map<Integer, Evento> agenda;
    private Alarme alarme;
    private Tarefa tarefa;

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
                .filter(evento -> {
                    if(evento instanceof Alarme){
                        return alarme.getDataEHora().equals(dataEHora);
                    }
                    if(evento instanceof Tarefa){
                        return tarefa.getDataEHoraLimite().equals(dataEHora);
                    }
                    return false;
                })
                .toList();
    }
    public List<Tarefa> getTarefas(){
        return agenda.values()
                .stream()
                .filter(evento -> evento instanceof Tarefa)
                .map(evento -> (Tarefa) evento)
                .toList();
    }
}
