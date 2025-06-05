package pt.ul.fc.di.css.javafxexample.dto;
public class ConquistaDtoGet {

    private Long campeonatoId;
    private String nomeCampeonato;
    private int posicao;

    public ConquistaDtoGet() {
    }

    public ConquistaDtoGet(Long campeonatoId, String nomeCampeonato, int posicao) {
        this.campeonatoId = campeonatoId;
        this.nomeCampeonato = nomeCampeonato;
        this.posicao = posicao;
    }

    public Long getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Long campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getNomeCampeonato() {
        return nomeCampeonato;
    }

    public void setNomeCampeonato(String nomeCampeonato) {
        this.nomeCampeonato = nomeCampeonato;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
