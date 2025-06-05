package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class ArbitrosNoJogoDto {

    private List<Long> arbitrosIds; // Lista de IDs dos árbitros
    private Long arbitroPrincipalId; // ID do árbitro principal

    public ArbitrosNoJogoDto() {}

    public ArbitrosNoJogoDto( List<Long> arbitrosIds, Long arbitroPrincipalId) {
        this.arbitrosIds = arbitrosIds;
        this.arbitroPrincipalId = arbitroPrincipalId;
    }


    public List<Long> getArbitrosIds() {
        return arbitrosIds;
    }

    public void setArbitrosIds(List<Long> arbitrosIds) {
        this.arbitrosIds = arbitrosIds;
    }

    public Long getArbitroPrincipalId() {
        return arbitroPrincipalId;
    }

    public void setArbitroPrincipalId(Long arbitroPrincipalId) {
        this.arbitroPrincipalId = arbitroPrincipalId;
    }
}