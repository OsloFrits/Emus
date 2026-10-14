package models;

import interfaces.Notificador;
import service.TemporizadorService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Alarme extends EventoAgendavel {

    private LocalDateTime dataEHora;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> alarme;
    private boolean estaAtiva;

    public Alarme(LocalDateTime dataEHora, String nome, String descricao, int pontuacao,
                  TemporizadorService temporizadorService, Notificador notificador){
        super(nome, descricao, pontuacao, notificador);
        this.scheduler = temporizadorService.getScheduler();

        if(dataEHora.isAfter(LocalDateTime.now().plusWeeks(2))) {
            throw new IllegalArgumentException("O alarme não pode ser agendado para mais de 2 semanas");
        }
        if(dataEHora.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("O alarme não pode ser agendado para o passado");
        }
        this.dataEHora = dataEHora;
    }

    @Override
    public synchronized void iniciarTemporizador(){
        if(estaAtiva){
            return;
        }
        Duration atraso = Duration.between(LocalDateTime.now(), dataEHora);
        estaAtiva = true;
        alarme = scheduler.schedule(
                this::prazoAtingido, //Fazer com this:: Impede q o metodo finalizarTemporizador seja chamado antes do previsto.
                atraso.toMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public synchronized void adiarEvento(Duration tempoAdiamento){
        this.dataEHora = this.dataEHora.plus(tempoAdiamento);
        if (estaAtiva) {
            pausaTemporizador();
            iniciarTemporizador();
        }
    }

    @Override
    public synchronized void pausaTemporizador() {
        this.estaAtiva = false;
        if(alarme != null){
            alarme.cancel(false);
        }
    }
    @Override
    public synchronized void pararTemporizador(){
        this.estaAtiva = false;
        salvar();
    }

    @Override
    public synchronized LocalDateTime getDataEHora() {
        return dataEHora;
    }

    @Override
    public synchronized Duration getTempoRestante() {
        if (!estaAtiva) {
            return Duration.ZERO;
        }
        Duration restante = Duration.between(LocalDateTime.now(), dataEHora);
        return restante.isNegative() ? Duration.ZERO : restante;
    }

}
