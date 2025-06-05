package pt.ul.fc.css.soccernow.controller;

public class JogadorView {
    private Long id;
    private String nome;
    private String posicao;

    public JogadorView(Long id, String nome, String posicao) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getPosicao() { return posicao; }
}
