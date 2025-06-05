package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(nullable = false)
    protected String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    protected boolean temCertificado;

    @Column(nullable = false)
    protected int numJogos = 0; 

    @Version
    private int version;

    protected Utilizador() {}

    protected Utilizador(String nome, String email, String password, boolean temCertificado) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.temCertificado = temCertificado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean getCertificado() {
        return temCertificado;
    }

    public void setCertificado(boolean temCertificado) {
        this.temCertificado = temCertificado;
    }

    public int getNumJogos() {
        return numJogos;
    }

    public void setNumJogos(int numJogos) {
        this.numJogos = numJogos;
    }

    public void incrementarNumJogos() {
        this.numJogos++;
    }
}