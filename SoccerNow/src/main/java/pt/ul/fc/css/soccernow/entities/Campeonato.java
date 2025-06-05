package pt.ul.fc.css.soccernow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;

@Entity
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String modalidade;

    @ManyToMany
    @JoinTable(
        name = "campeonato_equipa",
        joinColumns = @JoinColumn(name = "campeonato_id"),
        inverseJoinColumns = @JoinColumn(name = "equipa_id")
    )
    private List<Equipa> equipas = new ArrayList<>();

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogo> jogos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "campeonato_pontuacoes",
        joinColumns = @JoinColumn(name = "campeonato_id"))
    @MapKeyJoinColumn(name = "equipa_id")
    @Column(name = "pontos")
    private Map<Equipa, Integer> pontuacoes = new HashMap<>();

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conquista> conquistas = new ArrayList<>();



    public Campeonato() {}

    public Campeonato(String nome, String modalidade, List<Equipa> equipas) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.equipas = equipas;
        this.pontuacoes = new HashMap<>();
        for (Equipa equipa : equipas) {
            this.pontuacoes.put(equipa, 0); // começa com 0 pontos
        }
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

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

    public boolean terminou() {
        if (jogos == null || jogos.isEmpty()) {
            return false; // Um campeonato sem jogos não pode ter terminado
        }
        return jogos.stream().allMatch(j -> j.getEstado() == EstadoJogo.ENCERRADO);
    }

    //ordenar pontuacoes por ordem decrescente, tal como na vida real
    public Map<Equipa, Integer> getPontuacoes() {
        return pontuacoes.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }


    public void setPontuacoes(Map<Equipa, Integer> pontuacoes) {
        this.pontuacoes = pontuacoes;
    }

}
