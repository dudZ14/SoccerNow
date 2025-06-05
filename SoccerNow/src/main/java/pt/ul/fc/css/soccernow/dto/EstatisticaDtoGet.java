package pt.ul.fc.css.soccernow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "golos",
    "cartoes",
    "equipaVencedoraId",
    "resultado"
})
public class EstatisticaDtoGet extends EstatisticaDto {

    private Long equipaVencedoraId;
    private Long id;
    private String resultado;


    public EstatisticaDtoGet() {
        super();
    }

    public EstatisticaDtoGet(Long id, List<GoloDto> golos, List<CartaoJogadorDto> cartoes, Long equipaVencedoraId, String resultado) {
        super( golos, cartoes);
        this.id = id;
        this.equipaVencedoraId = equipaVencedoraId;
        this.resultado = resultado;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipaVencedoraId() {
        return equipaVencedoraId;
    }

    public void setEquipaVencedora(Long equipaVencedoraId) {
        this.equipaVencedoraId = equipaVencedoraId;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
