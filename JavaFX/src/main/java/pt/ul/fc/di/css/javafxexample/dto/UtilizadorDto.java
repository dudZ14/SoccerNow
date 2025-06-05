package pt.ul.fc.di.css.javafxexample.dto;

public abstract class UtilizadorDto {

    private Long id;

    private String nome;

    private String email;

    private String password;

    private boolean temCertificado;

    protected UtilizadorDto() {
        super();
    }

    protected UtilizadorDto(String nome, String email, String password, boolean temCertificado) {
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

    public boolean getTemCertificado() {
        return temCertificado;
    }

    public void setTemCertificado(boolean temCertificado) {
        this.temCertificado = temCertificado;
    }
}

