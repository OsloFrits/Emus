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
        this.scheduler = temporizadorService.getScheduler();

        if(dataEHora.isAfter(LocalDateTime.now().plusWeeks(2))) {
            throw new IllegalArgumentException("O alarme não pode ser agendado para mais de 2 semanas");
        }
        if(dataEHora.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("O alarme não pode ser agendado para o passado");
        }
        this.dataEHora = dataEHora;
    }

    public void adiarEvento(Duration tempoAdiamento){
        this.dataEHora = this.dataEHora.plus(tempoAdiamento);
        if (ativo) {
            pausaTemporizador();
            iniciarTemporizador();
        }
    }

    public void prazoAtingido(){
        //futuramente aplicar notiicaçãoservice para controlar melhor isso
        System.out.println("Alarme de ID:" + getId() + " teve o prazo atingido");
        /*sout gostaria de add alarme??
                se sim
                    adiar()
                se nao
                    pararTemporizador()*/
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
        salvar();
    }

    @Override
    public LocalDateTime getDataEHora() {
        return dataEHora;
    }

}
