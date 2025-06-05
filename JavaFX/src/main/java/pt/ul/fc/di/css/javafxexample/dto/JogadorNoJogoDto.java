package pt.ul.fc.di.css.javafxexample.dto;

public class JogadorNoJogoDto {

    private Long jogadorId; 

    private String posicao;

    private String nomeJogador;

    private String nomeEquipa;

    public JogadorNoJogoDto() {}
    
    public JogadorNoJogoDto(Long jogadorId, String posicao, String nomeJogador) {
        this.jogadorId = jogadorId;
        this.posicao = posicao;
        this.nomeJogador = nomeJogador;
    }
    
    public Long getJogadorId() {
        return jogadorId;
    }
    
    public void setJogadorId(Long jogadorId) {
        this.jogadorId = jogadorId;
    }
    
    public String getPosicao() {
        return posicao;
    }
    
    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }
    
    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }
    
    public String getNomeEquipa() {
        return nomeEquipa;
    }
    
    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    @Override
    public String toString() {
        return nomeJogador != null ? nomeJogador + " (" + posicao + ")" : "Jogador #"  + " (" + posicao + ")";
    }

}