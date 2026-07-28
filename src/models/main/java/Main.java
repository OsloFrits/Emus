import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        Duration tempoFoco, tempoDescanso;

        tempoFoco = Duration.ofSeconds(10);
        tempoDescanso = Duration.ofSeconds(10);

        Pomodoro pomodoro = new Pomodoro("joans", "é os joans", 10, tempoFoco, tempoDescanso);

        pomodoro.iniciarTemporizador();

        while (true) {
           if(pomodoro.getEstadoPomodoro() != EstadoPomodoro.Finalizado) {
               System.out.printf("Tempo restante: ", pomodoro.getTempoRestante().toString());
               Thread.sleep(1000);
           }else{
               break;
           }
        }

    }
}