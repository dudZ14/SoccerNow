package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.entities.Jogador;
import pt.ul.fc.css.soccernow.enumerados.Posicao;


@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    List<Jogador> findByNome(@Param("nome") String nome);

    @Query("SELECT j " +
       "FROM Jogador j " +
       "JOIN j.cartoes c " +
       "WHERE c.tipoCartao = 'VERMELHO' " +
       "GROUP BY j " +
       "ORDER BY COUNT(c) DESC")
    List<Jogador> findJogadoresComMaisCartoesVermelhos();

    @Query("SELECT COALESCE(SUM((SELECT COUNT(g) FROM Golo g WHERE g.jogador = j)) * 1.0 / COUNT(j), 0) " +
    "FROM Jogador j " +
    "WHERE j.nome = :nomeJogador")
    double calcularMediaGolosPorJogo(@Param("nomeJogador") String nomeJogador);

    @Query("SELECT j FROM Jogador j WHERE SIZE(j.cartoes) = :quantidade")
    List<Jogador> findByNumeroDeCartoes(@Param("quantidade") int quantidade);

    @Query("SELECT j FROM Jogador j WHERE SIZE(j.golos) = :quantidade")
    List<Jogador> findByNumeroDeGolos(@Param("quantidade") int quantidade);

    List<Jogador> findByNumJogos(int numJogos);


    @Query("""
        SELECT j
        FROM Jogador j
        WHERE j.posicaoPreferida = :posicao
    """)
    List<Jogador> findJogadoresPorPosicaoPreferida(@Param("posicao") Posicao posicao);
}