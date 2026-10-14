import enums.EstadoPomodoro;
import interfaces.Notificador;
import models.Alarme;
import models.Calendario;
import models.EventoAgendavel;
import models.Pomodoro;
import models.Tarefa;
import service.CalendarioService;
import service.TemporizadorService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static int passou = 0, falhou = 0;

    // Notificador de teste: imprime no console e guarda quem foi notificado para os testes conferirem.
    // CopyOnWriteArrayList porque quem adiciona é a thread do scheduler e quem lê é a main.
    private static final List<EventoAgendavel> notificados = new CopyOnWriteArrayList<>();
    private static final Notificador notificador = evento -> {
        notificados.add(evento);
        System.out.println("  [NOTIFICAÇÃO] Prazo de '" + evento.getNome() + "' atingido. Adiar ou concluir?");
    };

    interface Teste {
        void executar() throws Exception;
    }

    public static void main(String[] args) {
        TemporizadorService temporizadorService = new TemporizadorService();

        testar("Pomodoro: ciclo foco -> descanso -> finalizado", () -> testarCicloPomodoro(temporizadorService));
        testar("Pomodoro: pausa congela e retoma o tempo", () -> testarPausaPomodoro(temporizadorService));
        testar("Pomodoro: proteções de iniciar/parar", () -> testarProtecoesPomodoro(temporizadorService));
        testar("Alarme: validações do construtor", () -> testarValidacoesAlarme(temporizadorService));
        testar("Alarme: notifica no prazo", () -> testarDisparoAlarme(temporizadorService));
        testar("Alarme: adiar reagenda a notificação", () -> testarAdiarAlarme(temporizadorService));
        testar("Tarefa: validações do construtor", () -> testarValidacoesTarefa(temporizadorService));
        testar("Tarefa: adiar e concluir", () -> testarAdiarEConcluirTarefa(temporizadorService));
        testar("Tarefa: notifica e adiar reagenda", () -> testarNotificarEAdiarTarefa(temporizadorService));
        testar("Calendario + CalendarioService", () -> testarCalendario(temporizadorService));

        System.out.println("\n===== Resultado: " + passou + " passaram, " + falhou + " falharam =====");
        // Sem isso as threads do scheduler mantêm o programa rodando
        temporizadorService.getScheduler().shutdownNow();
    }

    // ---------- Pomodoro ----------

    private static void testarCicloPomodoro(TemporizadorService temporizadorService) throws Exception {
        Pomodoro pomodoro = new Pomodoro("Estudo", "Ciclo curto", 10,
                Duration.ofSeconds(2), Duration.ofSeconds(2), temporizadorService);
        Files.deleteIfExists(arquivoHistorico("Pomodoro", pomodoro.getId()));

        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Parado, "estado inicial é Parado");

        pomodoro.iniciarTemporizador();
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Executando, "após iniciar está Executando");

        Thread.sleep(2500);
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Descansando, "após o foco está Descansando");

        pomodoro.iniciarTemporizador();
        Thread.sleep(2500);
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Finalizado, "após o descanso está Finalizado");
        verificar(Files.exists(arquivoHistorico("Pomodoro", pomodoro.getId())), "JSON do pomodoro foi salvo");
    }

    private static void testarPausaPomodoro(TemporizadorService temporizadorService) throws InterruptedException {
        Pomodoro pomodoro = new Pomodoro("Pausa", "Teste de pausa", 5,
                Duration.ofSeconds(5), Duration.ofSeconds(5), temporizadorService);

        pomodoro.iniciarTemporizador();
        Thread.sleep(1500);
        pomodoro.pausaTemporizador();
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Pausado, "após pausar está Pausado");

        Duration tempoNaPausa = pomodoro.getTempoRestante();
        Thread.sleep(1500);
        verificar(pomodoro.getTempoRestante().equals(tempoNaPausa), "tempo restante não muda enquanto pausado");

        pomodoro.iniciarTemporizador();
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Executando, "ao retomar volta para Executando");
        Thread.sleep(1500);
        verificar(pomodoro.getTempoRestante().equals(tempoNaPausa.minusSeconds(1)),
                "ao retomar continua de onde parou (sem resetar)");
        pomodoro.pausaTemporizador();
    }

    private static void testarProtecoesPomodoro(TemporizadorService temporizadorService) throws InterruptedException {
        Pomodoro pomodoro = new Pomodoro("Proteções", "", 0,
                Duration.ofSeconds(10), Duration.ofSeconds(10), temporizadorService);

        pomodoro.iniciarTemporizador();
        pomodoro.iniciarTemporizador();
        Thread.sleep(2500);
        verificar(pomodoro.getTempoRestante().equals(Duration.ofSeconds(8)), "iniciar duas vezes não acelera o tempo");

        pomodoro.pararTemporizador();
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Descansando, "parar no foco vai para Descansando");
        Thread.sleep(1500);
        verificar(pomodoro.getTempoRestante().equals(Duration.ofSeconds(10)), "parar cancela o timer do foco");
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Descansando, "timer antigo não pula para Finalizado");

        pomodoro.iniciarTemporizador();
        pomodoro.pararTemporizador();
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Finalizado, "parar no descanso vai para Finalizado");

        pomodoro.iniciarTemporizador();
        Thread.sleep(1500);
        verificar(pomodoro.getEstadoPomodoro() == EstadoPomodoro.Finalizado
                && pomodoro.getTempoRestante().isZero(), "iniciar depois de Finalizado não faz nada");
    }

    // ---------- Alarme ----------

    private static void testarValidacoesAlarme(TemporizadorService temporizadorService) {
        verificarLanca(() -> new Alarme(LocalDateTime.now().minusMinutes(1), "Passado", "", 0, temporizadorService, notificador),
                "alarme no passado lança exceção");
        verificarLanca(() -> new Alarme(LocalDateTime.now().plusWeeks(3), "Longe", "", 0, temporizadorService, notificador),
                "alarme para mais de 2 semanas lança exceção");

        Alarme alarme = new Alarme(LocalDateTime.now().plusHours(1), "Ok", "", 0, temporizadorService, notificador);
        verificar(alarme.getTempoRestante().isZero(), "alarme não iniciado tem tempo restante zero");

        LocalDateTime dataOriginal = alarme.getDataEHora();
        alarme.adiarEvento(Duration.ofMinutes(10));
        verificar(alarme.getDataEHora().equals(dataOriginal.plusMinutes(10)), "adiarEvento soma o tempo na data");
    }

    private static void testarDisparoAlarme(TemporizadorService temporizadorService) throws Exception {
        Alarme alarme = new Alarme(LocalDateTime.now().plusSeconds(2), "Acordar", "Alarme curto", 3, temporizadorService, notificador);

        alarme.iniciarTemporizador();
        verificar(!alarme.getTempoRestante().isZero(), "alarme ativo tem tempo restante > 0");

        Thread.sleep(1500);
        verificar(!notificados.contains(alarme), "não notifica antes da hora");

        Thread.sleep(1000);
        verificar(notificados.contains(alarme), "notifica quando o prazo é atingido");
        verificar(alarme.getTempoRestante().isZero(), "após disparar o tempo restante é zero");
    }

    private static void testarAdiarAlarme(TemporizadorService temporizadorService) throws Exception {
        Alarme alarme = new Alarme(LocalDateTime.now().plusSeconds(1), "Soneca", "", 0, temporizadorService, notificador);

        alarme.iniciarTemporizador();
        alarme.adiarEvento(Duration.ofSeconds(2));

        Thread.sleep(1500);
        verificar(!notificados.contains(alarme), "não notifica no horário antigo");

        Thread.sleep(2000);
        verificar(notificados.contains(alarme), "notifica no horário novo");
    }

    // ---------- Tarefa ----------

    private static void testarValidacoesTarefa(TemporizadorService temporizadorService) {
        verificarLanca(() -> new Tarefa("Passado", "", 0, LocalDateTime.now().minusMinutes(1), temporizadorService, notificador),
                "tarefa no passado lança exceção");
        verificarLanca(() -> new Tarefa("Longe", "", 0, LocalDateTime.now().plusMonths(3), temporizadorService, notificador),
                "tarefa para mais de 2 meses lança exceção");

        Tarefa tarefa = new Tarefa("Ok", "", 0, LocalDateTime.now().plusDays(1), temporizadorService, notificador);
        verificar(tarefa.getEstaAtiva(), "tarefa nova está ativa");
        verificar(!tarefa.getEstaConcluida(), "tarefa nova não está concluída");
    }

    private static void testarAdiarEConcluirTarefa(TemporizadorService temporizadorService) throws Exception {
        Tarefa tarefa = new Tarefa("Relatório", "Entregar relatório", 8,
                LocalDateTime.now().plusDays(1), temporizadorService, notificador);
        Files.deleteIfExists(arquivoHistorico("Tarefa", tarefa.getId()));

        LocalDateTime dataOriginal = tarefa.getDataEHora();
        tarefa.adiarEvento(Duration.ofHours(2));
        verificar(tarefa.getDataEHora().equals(dataOriginal.plusHours(2)), "adiarEvento soma o tempo no prazo");
        verificar(!tarefa.getTempoRestante().isZero(), "tarefa em aberto tem tempo restante > 0");

        tarefa.pararTemporizador();
        verificar(tarefa.getEstaConcluida(), "após parar está concluída");
        verificar(!tarefa.getEstaAtiva(), "após parar não está ativa");
        verificar(tarefa.getTempoRestante().isZero(), "tarefa concluída tem tempo restante zero");
        verificar(Files.exists(arquivoHistorico("Tarefa", tarefa.getId())), "JSON da tarefa foi salvo");
    }

    private static void testarNotificarEAdiarTarefa(TemporizadorService temporizadorService) throws Exception {
        Tarefa tarefa = new Tarefa("Prova", "", 0, LocalDateTime.now().plusSeconds(1), temporizadorService, notificador);

        Thread.sleep(1500);
        verificar(notificados.contains(tarefa), "tarefa notifica sozinha (inicia no construtor)");

        // Simula o usuário escolhendo "adiar" depois da notificação
        notificados.clear();
        tarefa.adiarEvento(Duration.ofSeconds(1));
        Thread.sleep(1500);
        verificar(notificados.contains(tarefa), "depois de adiar, notifica de novo no novo prazo");
    }

    // ---------- Calendario ----------

    private static void testarCalendario(TemporizadorService temporizadorService) {
        Calendario calendario = new Calendario();
        CalendarioService calendarioService = new CalendarioService(calendario);

        Tarefa tarefa = new Tarefa("T", "", 0, LocalDateTime.now().plusDays(1), temporizadorService, notificador);
        Alarme alarme = new Alarme(LocalDateTime.now().plusDays(1), "A", "", 0, temporizadorService, notificador);
        Pomodoro pomodoro = new Pomodoro("P", "", 0, Duration.ofMinutes(25), Duration.ofMinutes(5), temporizadorService);

        calendario.guardarEvento(tarefa);
        calendario.guardarEvento(alarme);
        calendario.guardarEvento(pomodoro);

        verificar(calendario.getAgenda().size() == 3, "agenda guarda 3 eventos");
        verificar(calendarioService.getEvento(alarme.getId()) == alarme, "getEvento encontra pelo id");
        verificar(calendarioService.getTarefas().equals(List.of(tarefa)), "getTarefas retorna só tarefas");
        verificar(calendarioService.getAlarmes().equals(List.of(alarme)), "getAlarmes retorna só alarmes");

        calendarioService.deleteEvento(pomodoro.getId());
        verificar(calendario.getAgenda().size() == 2, "deleteEvento remove da agenda");
        verificarLanca(() -> calendarioService.deleteEvento(9999), "deleteEvento de id inexistente lança exceção");
    }

    // ---------- Utilitários ----------

    private static void testar(String nome, Teste teste) {
        System.out.println("\n>>> " + nome);
        try {
            teste.executar();
        } catch (Exception | Error e) {
            falhou++;
            System.out.println("  [ERRO] exceção inesperada: " + e);
        }
    }

    private static void verificar(boolean condicao, String descricao) {
        if (condicao) {
            passou++;
            System.out.println("  [OK]    " + descricao);
        } else {
            falhou++;
            System.out.println("  [FALHA] " + descricao);
        }
    }

    private static void verificarLanca(Runnable acao, String descricao) {
        try {
            acao.run();
            verificar(false, descricao);
        } catch (IllegalArgumentException e) {
            verificar(true, descricao);
        }
    }

    private static Path arquivoHistorico(String tipo, int id) {
        return Path.of("historico", tipo + id + ".json");
    }
}
