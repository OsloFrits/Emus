package interfaces;

import models.EventoAgendavel;

//Quem implementa decide COMO avisar o usuário (console, popup, front...).
//O model só chama esse método quando o prazo chega e não espera resposta.
//A escolha do usuário volta pelos métodos do próprio evento: adiarEvento() ou pararTemporizador().
public interface Notificador {
    void notificarPrazoAtingido(EventoAgendavel evento);
}
