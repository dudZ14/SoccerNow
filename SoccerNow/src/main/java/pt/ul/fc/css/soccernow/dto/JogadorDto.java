package pt.ul.fc.css.soccernow.dto;

import pt.ul.fc.css.soccernow.enumerados.Posicao;

public class JogadorDto extends UtilizadorDto {


    private String posicaoPreferida;

    public JogadorDto() {
        super();
    }

    public JogadorDto(String nome, String email, String password, boolean temCertificado, Posicao posicaoPreferida) {
        super(nome, email, password, temCertificado);
        this.posicaoPreferida = posicaoPreferida != null ? posicaoPreferida.name() : null;
    }

    public String getPosicaoPreferida() {
        return posicaoPreferida;
    }

    public void setPosicaoPreferida(String posicaoPreferida) {
        this.posicaoPreferida = posicaoPreferida;
    }
}
