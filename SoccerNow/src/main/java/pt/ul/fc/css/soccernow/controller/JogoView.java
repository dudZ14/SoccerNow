package pt.ul.fc.css.soccernow.controller;

import java.util.List;

public class JogoView {
    private Long id;
    private String descricao;
    private List<JogadorView> jogadores;

    public JogoView(Long id, String descricao, List<JogadorView> jogadores) {
        this.id = id;
        this.descricao = descricao;
        this.jogadores = jogadores;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public List<JogadorView> getJogadores() { return jogadores; }
}
