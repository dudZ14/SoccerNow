package pt.ul.fc.css.soccernow.dto;


public class CartaoJogadorDto {
    private Long jogadorId; // Apenas o ID do jogador

    private String tipoCartao;

    public CartaoJogadorDto() {}

    public CartaoJogadorDto(Long jogadorId, String tipoCartao) {
        this.jogadorId = jogadorId;
        this.tipoCartao = tipoCartao;
    }

    public Long getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(Long jogadorId) {
        this.jogadorId = jogadorId;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}