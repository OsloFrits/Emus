import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Pomodoro extends Evento implements Temporizador{
    private Duration tempoDeFoco, tempoDeDescanso, tempoRestante;
    private EstadoPomodoro estadoPomodoro;
    private int id;
    private static int proximoId = 1;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Pomodoro(String nome, String descricao, int pontuacao, Duration tempoDeFoco, Duration tempoDeDescanso) {
        super(nome, descricao, pontuacao);
        this.id = proximoId++;
        this.tempoDeFoco = tempoDeFoco;
        this.tempoDeDescanso = tempoDeDescanso;
        this.estadoPomodoro = EstadoPomodoro.Parado;
    }

    @Override
    public void iniciarTemporizador() {
        if(estadoPomodoro == EstadoPomodoro.Parado) {
            tempoRestante = tempoDeFoco;
        }else if(estadoPomodoro == EstadoPomodoro.Descansando) {
            tempoRestante = tempoDeDescanso;
        }
            scheduler.scheduleAtFixedRate(() -> {
                tempoRestante = tempoRestante.minusSeconds(1);

                System.out.println(tempoRestante); //Apenas para ver se esta funcionando

                if (tempoRestante.isZero() || tempoRestante.isNegative()) {
                    scheduler.shutdown();
                    finalizarTemporizador();
                }
            }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void pausaTemporizador() {

    }

    @Override
    public void finalizarTemporizador() {
        this.estadoPomodoro = EstadoPomodoro.Finalizado;
        JsonSave.salvar(this, "Pomodoro"+id);
    }
    public EstadoPomodoro getEstadoPomodoro() {
        return estadoPomodoro;
    }

    public Duration getTempoRestante() {
        return tempoRestante;
    }

    public Duration getTempoDeFoco() {
        return tempoDeFoco;
    }

    public void setTempoDeFoco(Duration tempoDeFoco) {
        this.tempoDeFoco = tempoDeFoco;
    }

    public Duration getTempoDeDescanso() {
        return tempoDeDescanso;
    }

    public void setTempoDeDescanso(Duration tempoDeDescanso) {
        this.tempoDeDescanso = tempoDeDescanso;
    }

}
