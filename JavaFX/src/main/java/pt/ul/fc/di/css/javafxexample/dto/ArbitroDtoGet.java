package pt.ul.fc.di.css.javafxexample.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// para aparecer nesta ordem quando se da get 
@JsonPropertyOrder({
    "id",
    "nome",
    "email",
    "password",
    "temCertificado",
    "numeroJogos",
    "anosDeExperiencia",
    "numeroCartoesMostrados"
})
public class ArbitroDtoGet extends UtilizadorDto {

    private int anosDeExperiencia;
    private int numeroJogos;
    private int numeroCartoesMostrados;
    private Long id;

    public ArbitroDtoGet() {
        super();
    }

    public ArbitroDtoGet(Long id, String nome, String email, String password, boolean temCertificado,
                               int anosDeExperiencia, int numeroJogos, int numeroCartoesMostrados) {
        super( nome, email, password, temCertificado);
        this.id = id;
        this.anosDeExperiencia = anosDeExperiencia;
        this.numeroJogos = numeroJogos;
        this.numeroCartoesMostrados = numeroCartoesMostrados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnosDeExperiencia() {
        return anosDeExperiencia;
    }
    
    public void setAnosDeExperiencia(int anosDeExperiencia) {
        this.anosDeExperiencia = anosDeExperiencia;
    }
    
    public int getNumeroJogos() {
        return numeroJogos;
    }

    public void setNumeroJogos(int numeroJogos) {
        this.numeroJogos = numeroJogos;
    }

    public int getNumeroCartoesMostrados() {
        return numeroCartoesMostrados;
    }

    public void setNumeroCartoesMostrados(int numeroCartoesMostrados) {
        this.numeroCartoesMostrados = numeroCartoesMostrados;
    }

    @Override
    public String toString() {
        return getNome() + (getTemCertificado() ? " (com certificado)" : " (sem certificado)");
    }

}
