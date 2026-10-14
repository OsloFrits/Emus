package models;

import interfaces.Notificador;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class EventoAgendavel extends Evento{
    private final Notificador notificador;

    public EventoAgendavel(String nome, String descricao, int pontuacao, Notificador notificador) {
        super(nome, descricao, pontuacao);
        this.notificador = notificador;
    }

    public abstract LocalDateTime getDataEHora();

    public abstract void adiarEvento(Duration tempoAdiamento);

    //Roda na thread do scheduler: só avisa e retorna, nunca espera a resposta do usuário aqui.
    public void prazoAtingido() {
        notificador.notificarPrazoAtingido(this);
    }
}
