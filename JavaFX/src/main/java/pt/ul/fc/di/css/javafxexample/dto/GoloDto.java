package pt.ul.fc.di.css.javafxexample.dto;
public class GoloDto {
    private Long jogadorId;
    private int minuto;

    public GoloDto() {}

    public GoloDto(Long jogadorId, int minuto) {
        this.jogadorId = jogadorId;
        this.minuto = minuto;
    }

    public Long getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(Long jogadorId) {
        this.jogadorId = jogadorId;
    }


    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
}