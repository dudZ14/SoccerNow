package pt.ul.fc.css.soccernow.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import pt.ul.fc.css.soccernow.enumerados.Posicao;


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

    private String nomeEquipa;
    private Long id;
    private int numeroGolos;
    private int numeroCartoes;
    private int numeroJogos;
    private String posicaoPreferida;

    
    public JogadorDtoGet() {
        super();
    }

    public JogadorDtoGet(Long id, String nome, String email, String password, boolean temCertificado,
                               String nomeEquipa, int numeroGolos, int numeroCartoes, int numeroJogos,
                               Posicao posicaoPreferida) {
        super( nome, email, password, temCertificado);
        this.id = id;
        this.nomeEquipa = nomeEquipa;
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

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public int getNumeroGolos() {
        return numeroGolos;
    }

    public void setNumeroGolos(int numeroGolos) {
        this.numeroGolos = numeroGolos;
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
}
