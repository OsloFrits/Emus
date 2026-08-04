import models.Pomodoro;
import models.Alarme;
import models.Tarefa;
import service.TemporizadorService;

import java.time.Duration;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        Duration tempoFoco, tempoDescanso;
        TemporizadorService temporizadorService = new TemporizadorService();

        tempoFoco = Duration.ofSeconds(10);
        tempoDescanso = Duration.ofSeconds(10);

        Pomodoro pomodoro = new Pomodoro("joans", "é os joans", 10, tempoFoco, tempoDescanso, temporizadorService);

        pomodoro.iniciarTemporizador();

        System.out.println(pomodoro.getTempoRestante());

        Thread.sleep(3000);

        pomodoro.pausaTemporizador();

        System.out.println("\n");

        pomodoro.iniciarTemporizador();

        System.out.println(pomodoro.getTempoRestante());

    }
}