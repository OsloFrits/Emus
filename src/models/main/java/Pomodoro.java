import java.time.LocalTime;

public class Pomodoro extends Evento implements Temporizador{
    LocalTime tempoDeFoco, tempoDeDescanso;
    private EstadoPomodoro estadoPomodoro;

    public Pomodoro(String nome, String descricao, int pontuacao, LocalTime tempoDeFoco, LocalTime tempoDeDescanso) {
        super(nome, descricao, pontuacao);
        this.tempoDeFoco = tempoDeFoco;
        this.tempoDeDescanso = tempoDeDescanso;
        this.estadoPomodoro = EstadoPomodoro.Parado;
    }

    @Override
    public void iniciarTemporizador() {
        this.estadoPomodoro = EstadoPomodoro.Executando;
    }

    @Override
    public void pausaTemporizador() {
        this.estadoPomodoro = EstadoPomodoro.Pausado;
    }

    @Override
    public void finalizarTemporizador() {
        this.estadoPomodoro = EstadoPomodoro.Finalizado;
    }

    public LocalTime getTempoDeFoco() {
        return tempoDeFoco;
    }

    public void setTempoDeFoco(LocalTime tempoDeFoco) {
        this.tempoDeFoco = tempoDeFoco;
    }

    public LocalTime getTempoDeDescanso() {
        return tempoDeDescanso;
    }

    public void setTempoDeDescanso(LocalTime tempoDeDescanso) {
        this.tempoDeDescanso = tempoDeDescanso;
    }

}
