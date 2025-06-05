package pt.ul.fc.css.soccernow.dto;


public class JogadorNoJogoDto {

    private Long jogadorId; 

    private String posicao;

    public JogadorNoJogoDto() {}

    public JogadorNoJogoDto(Long jogadorId, String posicao) {
        this.jogadorId = jogadorId;
        this.posicao = posicao;
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
}