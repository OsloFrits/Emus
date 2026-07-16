import java.time.Duration;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int hrs=0, mins=0, segs=0;
        Alarme alarme = Alarme.criarAlarme();

        LocalTime agora = alarme.getHoraAtual();


//        hrs = input.nextInt();
//        mins = input.nextInt();
//        segs = input.nextInt();
//        Duration duration = alarme.addTempo(hrs, mins, segs);
//
//        System.out.printf(agora.toString() + " Depois " + agora.plus(duration).toString());

        System.out.printf(alarme.getDataAtual().toString());
    }
}