    package pt.ul.fc.di.css.javafxexample.dto;
    import java.util.List;

    public class JogoDto {
        private LocalDto local; 
        private Long equipeCasaId;
        private Long equipeVisitanteId;
        private List<JogadorNoJogoDto> jogadoresNoJogo; 
        private ArbitrosNoJogoDto arbitrosNoJogo; 

        public JogoDto() {}

        public JogoDto(LocalDto local, Long equipeCasaId, Long equipeVisitanteId, List<JogadorNoJogoDto> jogadoresNoJogo, ArbitrosNoJogoDto arbitrosNoJogo) {
            this.local = local;
            this.equipeCasaId = equipeCasaId;
            this.equipeVisitanteId = equipeVisitanteId;
            this.jogadoresNoJogo = jogadoresNoJogo;
            this.arbitrosNoJogo = arbitrosNoJogo;
        }

        public LocalDto getLocal() {
            return local;
        }

        public void setLocal(LocalDto local) {
            this.local = local;
        }

        public Long getEquipaCasaId() {
            return equipeCasaId;
        }

        public void setEquipaCasaId(Long equipeCasaId) {
            this.equipeCasaId = equipeCasaId;
        }

        public Long getEquipaVisitanteId() {
            return equipeVisitanteId;
        }

        public void setEquipaVisitanteId(Long equipeVisitanteId) {
            this.equipeVisitanteId = equipeVisitanteId;
        }

        public List<JogadorNoJogoDto> getJogadoresNoJogo() {
            return jogadoresNoJogo;
        }

        public void setJogadoresNoJogo(List<JogadorNoJogoDto> jogadoresNoJogo) {
            this.jogadoresNoJogo = jogadoresNoJogo;
        }

        public ArbitrosNoJogoDto getArbitrosNoJogo() {
            return arbitrosNoJogo;
        }

        public void setArbitrosNoJogo(ArbitrosNoJogoDto arbitrosNoJogo) {
            this.arbitrosNoJogo = arbitrosNoJogo;
        }
    }