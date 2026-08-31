package models;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class EventoAgendavel extends Evento{
    
    public EventoAgendavel(String nome, String descricao, int pontuacao) {
        super(nome, descricao, pontuacao);
    }

    public abstract LocalDateTime getDataEHora();

    public abstract void prazoAtingido();

    public abstract void adiarEvento(Duration tempoAdiamento);
}
