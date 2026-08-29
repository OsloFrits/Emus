package models;

import java.util.HashMap;
import java.util.Map;

public class Calendario {
    public Map<Integer, Evento> agenda = new HashMap<>();

    public void guardarEvento(Evento evento){
        agenda.put(evento.getId(), evento);
    }

    public Map<Integer, Evento> getAgenda() {
        return agenda;
    }

    public void setAgenda(Map<Integer, Evento> agenda) {
        this.agenda = agenda;
    }
}
