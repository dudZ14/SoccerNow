
package pt.ul.fc.di.css.javafxexample.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


// para aparecer nesta ordem quando se da get 
@JsonPropertyOrder({
    "id",
    "nome",
    "email",
    "password",
    "temCertificado",
    "posicaoPreferida",
    "nomeEquipa",
    "numeroGolos",
    "numeroCartoes",
    "numeroJogos"
})
public class JogadorDtoGet extends UtilizadorDto {
    public enum Posicao {
        GUARDA_REDES,
        DEFESA,
        MEDIO,
        AVANCADO
    }

    private Long id;
    private int numeroGolos;
    private int numeroCartoes;
    private int numeroJogos;
    private String posicaoPreferida;
    private String nomeEquipa;
    
    public JogadorDtoGet() {
        super();
    }

    public JogadorDtoGet(Long id, String nome, String email, String password, int numeroGolos, int numeroCartoes, int numeroJogos,
                               Posicao posicaoPreferida) {
        super( nome, email, password, false);
        this.id = id;
        this.numeroGolos = numeroGolos;
        this.numeroCartoes = numeroCartoes;
        this.numeroJogos = numeroJogos;
        this.posicaoPreferida = posicaoPreferida != null ? posicaoPreferida.name() : null;
    }

     public String getPosicaoPreferida() {
        return posicaoPreferida;
    }

    public void setPosicaoPreferida(String posicaoPreferida) {
        this.posicaoPreferida = posicaoPreferida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroGolos() {
        return numeroGolos;
    }

    public void setNumeroGolos(int numeroGolos) {
        this.numeroGolos = numeroGolos;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public int getNumeroCartoes() {
        return numeroCartoes;
    }

    public void setNumeroCartoes(int numeroCartoes) {
        this.numeroCartoes = numeroCartoes;
    }
    
    public int getNumeroJogos() {
        return numeroJogos;
    }

    public void setNumeroJogos(int numeroJogos) {
        this.numeroJogos = numeroJogos;
    }

    @Override
    public String toString() {
        return String.format(
            "%s (%s) - %d golos em %d jogos",
            getNome(),
            getPosicaoPreferida(),
            getNumeroGolos(),
            getNumeroJogos()
        );
    }


}
