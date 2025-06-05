package pt.ul.fc.css.soccernow.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ArbitrosNoJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "arbitros_no_jogo_arbitros",
        joinColumns = @JoinColumn(name = "arbitros_no_jogo_id"),
        inverseJoinColumns = @JoinColumn(name = "arbitro_id")
    )
    private List<Arbitro> arbitros; // Lista de árbitros

    @OneToOne
    @JoinColumn(name = "arbitro_principal_id", nullable = false)
    private Arbitro arbitroPrincipal; // Árbitro principal

    @OneToOne(mappedBy = "arbitrosNoJogo", cascade = CascadeType.ALL)
    private Jogo jogo;

    @Version
    private int version; 

    public ArbitrosNoJogo() {}

    public ArbitrosNoJogo(List<Arbitro> arbitros, Arbitro arbitroPrincipal) {
        this.arbitros = arbitros;
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Arbitro> getArbitros() {
        return arbitros;
    }

    public void setArbitros(List<Arbitro> arbitros) {
        this.arbitros = arbitros;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
}