package models;

public class Usuario {
    private String nome;
    private int id, pontuacaoTotal;
    private static int proximoId=1;
    private Calendario agenda;

    public Usuario(String nome, int pontuacaoTotal) {
        this.nome = nome;
        this.id = proximoId++;
        this.pontuacaoTotal = pontuacaoTotal;
        this.agenda = new Calendario();
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    private void setPontuacaoTotal(int pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public Calendario getAgenda() {
        return agenda;
    }

    private void setAgenda(Calendario agenda) {
        this.agenda = agenda;
    }
}
