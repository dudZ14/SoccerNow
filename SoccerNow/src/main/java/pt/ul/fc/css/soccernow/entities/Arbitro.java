package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Arbitro")
public class Arbitro extends Utilizador {

    private int anosDeExperiencia;
    private int numCartoesMostrados;

    public Arbitro() {
        super();
    }

    public Arbitro(String nome, String email, String password, boolean temCertificado, int anosDeExperiencia) {
        super(nome, email, password, temCertificado);
        this.anosDeExperiencia = anosDeExperiencia;
    }

    public int getAnosDeExperiencia() {
        return anosDeExperiencia;
    }

    public void setAnosDeExperiencia(int anosDeExperiencia) {
        this.anosDeExperiencia = anosDeExperiencia;
    }

     public int getNumCartoesMostrados() {
        return numCartoesMostrados;
    }

    public void setNumCartoesMostrados(int numCartoesMostrados) {
        this.numCartoesMostrados = numCartoesMostrados;
    }

    public void incrementarNumCartoesMostrados(int quantidade) {
        this.numCartoesMostrados += quantidade;
    }
}