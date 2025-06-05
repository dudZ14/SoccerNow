package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Equipa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "equipas")
    private List<Jogador> jogadores = new ArrayList<>();

    @OneToMany(mappedBy = "equipa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JogadorNoJogo> jogadoresNoJogo = new ArrayList<>();

    
    @ManyToMany
    @JoinTable(
        name = "equipa_jogos",
        joinColumns = @JoinColumn(name = "equipa_id"),
        inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> historico = new ArrayList<>();

    @OneToMany(mappedBy = "equipa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conquista> conquistas = new ArrayList<>();


    @Version
    private int version; 

    private int numVitorias;

    public Equipa() {}

    public Equipa(String nome, List<Jogador> jogadores) {
        this.nome = nome;
        this.jogadores = jogadores;
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

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void addJogador(Jogador jogador) {
        if (!jogadores.contains(jogador)) {
            jogadores.add(jogador);
            jogador.getEquipas().add(this);
        }
    }
    
    public void removeJogador(Jogador jogador) {
        if (jogadores.contains(jogador)) {
            jogadores.remove(jogador);
            jogador.getEquipas().remove(this);
        }
    }
    
    public void clear() {
        for (Jogador jogador : new ArrayList<>(jogadores)) {
            removeJogador(jogador);
        }
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public List<JogadorNoJogo> getJogadoresNoJogo() {
        return jogadoresNoJogo;
    }

    public void setJogadoresNoJogo(List<JogadorNoJogo> jogadoresNoJogo) {
        this.jogadoresNoJogo = jogadoresNoJogo;
    }
        
    public List<Jogo> getHistorico() {
        return historico;
    }

    public void setHistorico(List<Jogo> historico) {
        this.historico = historico;
    }

    public void addJogoHistorico(Jogo jogo) {
        this.historico.add(jogo);
    }

    public List<Conquista> getConquistas() {
    return conquistas;
    }

    public void setConquistas(List<Conquista> conquistas) {
        this.conquistas = conquistas;
    }

    public void adicionarConquista(Conquista conquista) {
        conquistas.add(conquista);
        conquista.setEquipa(this);
    }

    public void setNumVitorias(int numVitorias) {
        this.numVitorias = numVitorias;
    }

    public int getNumVitorias() {
        return this.numVitorias;
    }

    public void incrementarNumVitorias() {
        this.numVitorias++;
    }

}