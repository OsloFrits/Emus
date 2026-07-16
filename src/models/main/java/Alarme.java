import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Alarme {
    private static Alarme instance = null; //Permite apenas criar um obj alarme, provalvelmente iremos trocar isso
    //private Alarme alarme = new Alarme(); *Erro, coloquei criar obj direto fora de um metodo, fico se criando de forma infinita.

    public static Alarme criarAlarme(){
        if (instance == null){
            instance = new Alarme();
        }
        return Alarme.instance;
    }

    public Duration addTempo(int hrs, int mins, int segs){ //validação das entradas vai ser por fora
        Duration duration = Duration.ZERO; //pode ser refatorado para deixar melhor e mais legivel o codigo
        duration = duration.plusHours(hrs).plusMinutes(mins).plusSeconds(segs);
        return duration;
    }

    public LocalTime getHoraAtual(){
        return LocalTime.now();
    }
    public LocalDate getDataAtual(){
        LocalDate data = LocalDate.now();
        return data;
    }
    public String toString() {
        return "";
    } //Precisamos criar um metodo toString ou so damos override??
}
