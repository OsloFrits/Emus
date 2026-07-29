import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Alarme extends Evento implements Temporizador{

    private LocalDateTime dataEHora;

    public Alarme(LocalDateTime dataEHora, String nome, String descricao, int pontuacao){
        super(nome, descricao, pontuacao);
        this.dataEHora = dataEHora;
    }

    public Duration alterarTempo(int hrs, int mins, int segs){ //validação das entradas vai ser por fora
        Duration duration = Duration.ZERO; //pode ser refatorado para deixar melhor e mais legivel o codigo
        duration = duration.plusHours(hrs).plusMinutes(mins).plusSeconds(segs);
        return duration;
    }

    public void iniciarTemporizador(){

    }

    @Override
    public void pausaTemporizador() {

    }

    public void finalizarTemporizador(){

    }

    public LocalTime getHoraAtual(){
        return LocalTime.now();
    }
    public LocalDate getDataAtual(){
        LocalDate data = LocalDate.now();
        return data;
    }

    public LocalDateTime getDataEHora() {
        return dataEHora;
    }

    public void setDataEHora(LocalDateTime dataEHora) {
        this.dataEHora = dataEHora;
    }
}
