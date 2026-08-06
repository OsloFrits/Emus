package models;

import service.JsonSave;
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
    private boolean ativo;

    public Alarme(LocalDateTime dataEHora, String nome, String descricao, int pontuacao, TemporizadorService temporizadorService){
        super(nome, descricao, pontuacao);
        this.dataEHora = dataEHora;
        this.scheduler = temporizadorService.getScheduler();

        if(dataEHora.isAfter(LocalDateTime.now().plusWeeks(2))) {
            throw new IllegalArgumentException("O alarme não pode ser agendado para mais de 2 semanas");
        }
        if(dataEHora.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("O alarme não pode ser agendado para o passado");
        }
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
        alarme = scheduler.schedule(
                this::pararTemporizador, //Fazer com this:: Impede q o metodo finalizarTemporizador seja chamado antes do previsto.
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

    public void pararTemporizador(){
        this.ativo = false;
        tocarAlarme();
        JsonSave.salvar(this, "Alarme" + super.getId());
    }

    public void tocarAlarme(){

    }

    @Override
    public LocalDateTime getDataEHora() {
        return dataEHora;
    }

}
