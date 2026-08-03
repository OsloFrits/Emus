package models;

import interfaces.Temporizador;
import service.JsonSave;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Alarme extends Evento implements Temporizador {

    private LocalDateTime dataEHora;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> alarme;
    private boolean ativo;

    public Alarme(LocalDateTime dataEHora, String nome, String descricao, int pontuacao){
        super(nome, descricao, pontuacao);
        this.dataEHora = dataEHora;
        if(dataEHora.isAfter(LocalDateTime.now().plusWeeks(2))) {
            throw new IllegalArgumentException("O alarme não pode ser agendado para mais de 2 semanas");
        }
        if(dataEHora.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("O alarme não pode ser agendado para o passado");
        }
            criarSchedule();
    }

    public LocalDateTime adiarAlarme(int hrs, int mins, int segs){
        this.dataEHora = this.dataEHora.plusHours(hrs).plusMinutes(mins).plusSeconds(segs);
        return dataEHora;
    }

    public void iniciarTemporizador(){
        if(ativo){
            return;
        }
        Duration atraso = Duration.between(LocalDateTime.now(), dataEHora);
        ativo = true;
        if(scheduler.isTerminated() || scheduler.isShutdown() || scheduler==null) {
            criarSchedule();
        }
        alarme = scheduler.schedule(
                this::finalizarTemporizador, //Fazer com this:: Impede q o metodo finalizarTemporizador seja chamado antes do previsto.
                atraso.getSeconds(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public void pausaTemporizador() {
        this.ativo = false;
        if(alarme != null){
            alarme.cancel(false);
        }
    }

    public void finalizarTemporizador(){
        this.ativo = false;
        tocarAlarme();
        JsonSave.salvar(this, "Alarme" + super.getId());
    }

    public void tocarAlarme(){

    }

    private void criarSchedule(){
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public LocalDateTime getHoraAtual(){
        return LocalDateTime.now();
    }

    public void setDataEHora(LocalDateTime dataEHora) {
        this.dataEHora = dataEHora;
    }
}
