package pt.ul.fc.di.css.javafxexample.dto;

public class ArbitroDto extends UtilizadorDto {

    private int anosDeExperiencia;
    
    public ArbitroDto() {
        super();
    }

    public ArbitroDto (String nome, String email, String password, boolean temCertificado, int anosDeExperiencia) {
        super(nome, email, password, temCertificado);
        this.anosDeExperiencia = anosDeExperiencia;
    }

    public int getAnosDeExperiencia() {
        return anosDeExperiencia;
    }

    public void setAnosDeExperiencia(int anosDeExperiencia) {
        this.anosDeExperiencia = anosDeExperiencia;
    }

}
