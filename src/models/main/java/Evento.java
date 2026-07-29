abstract class Evento {
    private String nome, descricao;
    private int pontuacao, id;
    public Evento(String nome, String descricao, int pontuacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.pontuacao = pontuacao;
    }
}
