package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class EquipaDto {

    private String nome;
    private List<Long> jogadoresIds; // Lista de nomes dos jogadores

    public EquipaDto() {
    }

    public EquipaDto(String nome, List<Long> jogadoresIds) {
        this.nome = nome;
        this.jogadoresIds = jogadoresIds;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getJogadoresIds() {
        return jogadoresIds;
    }

    public void setJogadoresIds(List<Long> jogadoresIds) {
        this.jogadoresIds = jogadoresIds;
    }
}