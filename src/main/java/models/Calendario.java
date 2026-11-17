package models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Calendario {
    public Map<Integer, Evento> agenda = new ConcurrentHashMap<>();

    public Map<Integer, Evento> getAgenda() {
        return agenda;
    }

    public void setAgenda(Map<Integer, Evento> agenda) {
        this.agenda = agenda;
    }
}
