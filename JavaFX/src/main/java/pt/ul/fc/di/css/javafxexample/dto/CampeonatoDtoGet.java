package pt.ul.fc.di.css.javafxexample.dto;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampeonatoDtoGet {

    private Long id;
    private String nome;
    private String modalidade;
    private List<Long> equipaIds;
    private List<Long> jogoIds;
    private int numeroJogos;
    private int numeroJogosFeitos;  
    private Map<String, Integer> pontuacoes; // chave: nome da equipa

    public CampeonatoDtoGet() {}

    public CampeonatoDtoGet(Long id, String nome, String modalidade, List<Long> equipaIds, List<Long> jogoIds, int numeroJogos, int numeroJogosFeitos,Map<String, Integer> pontuacoes) {
        this.id = id;
        this.nome = nome;
        this.modalidade = modalidade;
        this.equipaIds = equipaIds;
        this.jogoIds = jogoIds;
        this.numeroJogos = numeroJogos;
        this.numeroJogosFeitos = numeroJogosFeitos;
        this.pontuacoes = pontuacoes;
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

    public int getNumeroJogos() {
        return numeroJogos;
    }

    public void setNumeroJogos(int numeroJogos) {
        this.numeroJogos = numeroJogos;
    }

    public int getNumeroJogosFeitos() {
        return numeroJogosFeitos;
    }

    public void setNumeroJogosFeitos(int numeroJogosFeitos) {
        this.numeroJogosFeitos = numeroJogosFeitos;
    }

    public Map<String, Integer> getPontuacoes() {
        return pontuacoes;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", 
            nome, 
            modalidade, 
            numeroJogosFeitos, 
            numeroJogos);
    }

}
