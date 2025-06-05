package pt.ul.fc.css.soccernow.dto;


public abstract class UtilizadorDto {

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

