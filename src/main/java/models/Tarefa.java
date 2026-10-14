package models;

import interfaces.Notificador;
import service.TemporizadorService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Tarefa extends EventoAgendavel {
    private LocalDateTime dataEHoraLimite;
    private boolean estaAtiva=false, estaConcluida=false;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> tarefa;



    public Tarefa(String nome, String descricao, int pontuacao, LocalDateTime dataEHoraLimite,
                  TemporizadorService temporizadorService, Notificador notificador){
        super(nome, descricao, pontuacao, notificador);
        this.scheduler = temporizadorService.getScheduler();

        if(dataEHoraLimite.isAfter(LocalDateTime.now().plusMonths(2))) {
            throw new IllegalArgumentException("A tarefa não pode ser criada para depois de 2 meses");
        }
        if(dataEHoraLimite.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("A tarefa não pode ser agendado para o passado");
        }
        this.dataEHoraLimite = dataEHoraLimite;

        iniciarTemporizador();
        this.estaConcluida = false;
    }

    @Override
    public synchronized void iniciarTemporizador() {
        if (estaAtiva) {
            return;
        }
        estaAtiva = true;
        Duration atraso = Duration.between(LocalDateTime.now(), dataEHoraLimite);
        tarefa = scheduler.schedule(
            this::prazoAtingido,
            atraso.toMillis(),
            TimeUnit.MILLISECONDS
        );
    }

    public synchronized void adiarEvento(Duration tempoAdiamento){
        this.dataEHoraLimite =  this.dataEHoraLimite.plus(tempoAdiamento);
        if (estaAtiva) {
            pausaTemporizador();
            iniciarTemporizador();
        }else{
            iniciarTemporizador();
        }
    }

    @Override
    public synchronized void pausaTemporizador() {
        estaAtiva = false;
        if(tarefa != null){
            tarefa.cancel(false);
        }
    }

    @Override
    public synchronized void pararTemporizador() {
        this.estaAtiva = false;
        this.estaConcluida = true;
        salvar();
    }

    @Override
    public synchronized LocalDateTime getDataEHora() {
        return dataEHoraLimite;
    }

    @Override
    public synchronized Duration getTempoRestante() {
        if (estaConcluida) {
            return Duration.ZERO;
        }
        Duration restante = Duration.between(LocalDateTime.now(), dataEHoraLimite);
        return restante.isNegative() ? Duration.ZERO : restante;
    }

    public void setDataEHoraLimite(LocalDateTime dataEHoraLimite) {
        this.dataEHoraLimite = dataEHoraLimite;
    }

    public void setEstaAtiva(boolean estaAtiva) { //Usar para cancelamento de tarefa
        this.estaAtiva = estaAtiva;
    }

    public Boolean getEstaAtiva() {
        return estaAtiva;
    }

    public Boolean getEstaConcluida() {
        return estaConcluida;
    }

    public void setEstaConcluida(boolean estaConcluida) {
        this.estaConcluida = estaConcluida;
    }
}
