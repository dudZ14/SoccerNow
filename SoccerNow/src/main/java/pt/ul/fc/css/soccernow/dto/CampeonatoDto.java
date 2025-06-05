package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class CampeonatoDto {

    private String nome;
    private String modalidade;
    private List<Long> equipaIds; 
    private List<Long> jogoIds;   

    public CampeonatoDto() {}

    public CampeonatoDto(String nome, String modalidade, List<Long> equipaIds, List<Long> jogoIds) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.equipaIds = equipaIds;
        this.jogoIds = jogoIds;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModalidade() {
        return modalidade;
    }
    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public List<Long> getEquipaIds() {
        return equipaIds;
    }
    public void setEquipaIds(List<Long> equipaIds) {
        this.equipaIds = equipaIds;
    }

    public List<Long> getJogoIds() {
        return jogoIds;
    }
    public void setJogoIds(List<Long> jogoIds) {
        this.jogoIds = jogoIds;
    }
}
