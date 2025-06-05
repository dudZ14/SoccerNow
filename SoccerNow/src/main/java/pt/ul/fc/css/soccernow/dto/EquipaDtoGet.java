package pt.ul.fc.css.soccernow.dto;
import java.util.List;

public class EquipaDtoGet {

    private Long id;
    private String nome;
    private List<JogadorDtoGet> jogadores;
    private List<Long> historicoJogosIds;
    private List<ConquistaDtoGet> conquistas;
    private int numeroVitorias;
    private List<String> historicoFormatado;

    public EquipaDtoGet() {
    }

    public EquipaDtoGet(Long id, String nome, List<JogadorDtoGet> jogadores, List<Long> historicoJogosIds, List<ConquistaDtoGet> conquistas, int numeroVitorias) {
        this.id = id;
        this.nome = nome;
        this.jogadores = jogadores;
        this.historicoJogosIds = historicoJogosIds;
        this.conquistas = conquistas;
        this.numeroVitorias = numeroVitorias;
    }



    // Getters e Setters

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

    public int getNumeroVitorias() {
        return numeroVitorias;
    }

    public void setNumeroVitorias(int numeroVitorias) {
        this.numeroVitorias = numeroVitorias;
    }



    public List<JogadorDtoGet> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<JogadorDtoGet> jogadores) {
        this.jogadores = jogadores;
    }

    public List<Long> getHistoricoJogosIds() {
        return historicoJogosIds;
    }

    public void setHistoricoJogosIds(List<Long> historicoJogosIds) {
        this.historicoJogosIds = historicoJogosIds;
    }

    public List<ConquistaDtoGet> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<ConquistaDtoGet> conquistas) {
        this.conquistas = conquistas;
    }

    public List<String> getHistoricoFormatado() {
        return historicoFormatado;
    }

    public void setHistoricoFormatado(List<String> historicoFormatado) {
        this.historicoFormatado = historicoFormatado;
    }


}
