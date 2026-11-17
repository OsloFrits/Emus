package models;

import interfaces.Notificador;
import service.TemporizadorService;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class EventoAgendavel extends Evento{
    private final Notificador notificador;

    public EventoAgendavel(String nome, String descricao, int pontuacao, TemporizadorService temporizadorService, Notificador notificador) {
        super(nome, descricao, pontuacao, temporizadorService);
        this.notificador = notificador;
    }

    public abstract LocalDateTime getDataEHora();

    public abstract void adiarEvento(Duration tempoAdiamento);

    //Roda na thread do scheduler: só avisa e retorna, nunca espera a resposta do usuário aqui.
    public void prazoAtingido() {
        notificador.notificarPrazoAtingido(this);
    }
}
