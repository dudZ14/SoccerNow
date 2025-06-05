package pt.ul.fc.di.css.javafxexample.model;

import javafx.beans.property.*;

public abstract class Utilizador {

    protected final LongProperty id = new SimpleLongProperty();
    protected final StringProperty nome = new SimpleStringProperty();
    protected final StringProperty email = new SimpleStringProperty();
    protected final StringProperty password = new SimpleStringProperty();
    protected final BooleanProperty temCertificado = new SimpleBooleanProperty();
    protected final IntegerProperty numJogos = new SimpleIntegerProperty();

    public LongProperty idProperty() {
        return id;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public BooleanProperty temCertificadoProperty() {
        return temCertificado;
    }

    public boolean getTemCertificado() {
        return temCertificado.get();
    }

    public void setTemCertificado(boolean temCertificado) {
        this.temCertificado.set(temCertificado);
    }

    public IntegerProperty numJogosProperty() {
        return numJogos;
    }

    public int getNumJogos() {
        return numJogos.get();
    }

    public void setNumJogos(int numJogos) {
        this.numJogos.set(numJogos);
    }

    public void incrementarNumJogos() {
        this.numJogos.set(this.numJogos.get() + 1);
    }
}
