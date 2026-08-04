package interfaces;

import java.time.Duration;

public interface Temporizador {
    void iniciarTemporizador();
    void pausaTemporizador();
    void pararTemporizador();
    Duration getTempoRestante();
}
