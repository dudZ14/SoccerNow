package pt.ul.fc.di.css.javafxexample.model;

import javafx.beans.property.*;

public class Arbitro extends Utilizador {

    private final IntegerProperty anosDeExperiencia = new SimpleIntegerProperty();
    private int numeroJogos;
    private int numeroCartoesMostrados;

    

    public Arbitro() {
        super();
    }

    public Arbitro(String nome, String email, String password, boolean temCertificado, int anosDeExperiencia) {
        setNome(nome);
        setEmail(email);
        setPassword(password);
        setTemCertificado(temCertificado);
        setAnosDeExperiencia(anosDeExperiencia);
    }


    public IntegerProperty anosDeExperienciaProperty() {
        return anosDeExperiencia;
    }

    public int getAnosDeExperiencia() {
        return anosDeExperiencia.get();
    }

    public void setAnosDeExperiencia(int anosDeExperiencia) {
        this.anosDeExperiencia.set(anosDeExperiencia);
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
        return String.format("%s | %d anos de experiência | %s certificado",
                getNome(), getAnosDeExperiencia(), getTemCertificado() ? "com" : "sem");
    }
}
