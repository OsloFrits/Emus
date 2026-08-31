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

        tempoFoco = Duration.ofSeconds(9);
        tempoDescanso = Duration.ofSeconds(9);

        Pomodoro pomodoro = new Pomodoro("joans", "é os joans", 10, tempoFoco, tempoDescanso, temporizadorService);

        pomodoro.iniciarTemporizador();

        System.out.println(pomodoro.getTempoRestante());

        //pomodoro.pararTemporizador();

        //pomodoro.iniciarTemporizador();

        System.out.println(pomodoro.getTempoRestante());

        //System.out.println(pomodoro.getTempoRestante());

    }
}