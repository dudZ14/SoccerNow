package pt.ul.fc.di.css.javafxexample.dto;

public class JogadorDto extends UtilizadorDto {
    public enum Posicao {
        GUARDA_REDES,
        DEFESA,
        MEDIO,
        AVANCADO
    }

    private String posicaoPreferida;

    public JogadorDto() {
        super();
    }

    public JogadorDto(String nome, String email, String password, Posicao posicaoPreferida) {
        super(nome, email, password, false);
        this.posicaoPreferida = posicaoPreferida != null ? posicaoPreferida.name() : null;
    }

    public String getPosicaoPreferida() {
        return posicaoPreferida;
    }

    public void setPosicaoPreferida(String posicaoPreferida) {
        this.posicaoPreferida = posicaoPreferida;
    }
}
