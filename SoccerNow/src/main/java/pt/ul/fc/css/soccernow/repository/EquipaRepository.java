package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.entities.Equipa;
import pt.ul.fc.css.soccernow.enumerados.Posicao;

@Repository
public interface EquipaRepository extends JpaRepository<Equipa, Long> {

        @Query("SELECT e FROM Equipa e WHERE SIZE(e.jogadores) < 5")
        List<Equipa> findEquipasComMenosDeCincoJogadores();

        @Query("SELECT e FROM Equipa e " +
                        "WHERE (SELECT COUNT(c) FROM Jogador j JOIN j.cartoes c WHERE e MEMBER OF j.equipas) = " +
                        "(SELECT MAX(cartoesPorEquipa) FROM (SELECT COUNT(c) AS cartoesPorEquipa " +
                        "FROM Jogador j JOIN j.cartoes c GROUP BY j) subquery)")
        List<Equipa> findEquipasComMaisCartoes();

        @Query("SELECT e FROM Equipa e " +
                        "WHERE (SELECT COUNT(j) FROM Jogo j WHERE j.estatistica.equipaVencedora = e) = " +
                        "(SELECT MAX(vitorias) FROM (SELECT COUNT(j2) AS vitorias FROM Jogo j2 GROUP BY j2.estatistica.equipaVencedora))")
        List<Equipa> findEquipasComMaisVitorias();

        Optional<Equipa> findById(Long id);

        List<Equipa> findByNome(String nome);

        @Query("""
        SELECT e
        FROM Equipa e
        WHERE SIZE(e.jogadores) = :num
        """)
        List<Equipa> findByNumeroDeJogadores(@Param("num") int num);

        //é preciso left join para se meter vitorias=0 aparecer as equipas que nunca jogaram
        @Query("""
        SELECT e
        FROM Equipa e
        LEFT JOIN e.historico j
        LEFT JOIN j.estatistica est
        WHERE est.equipaVencedora = e
        OR est.equipaVencedora IS NULL
        GROUP BY e
        HAVING COUNT(CASE WHEN est.equipaVencedora = e THEN 1 END) = :vitorias
        """)
        List<Equipa> findByNumeroDeVitorias(@Param("vitorias") int vitorias);


        //é preciso left join e case para se meter empates=0 aparecer as equipas que nunca jogaram
        @Query("""
        SELECT e
        FROM Equipa e
        LEFT JOIN e.historico j
        LEFT JOIN j.estatistica est
        GROUP BY e
        HAVING COUNT(
                CASE
                WHEN j.estado = 'ENCERRADO'
                AND est.equipaVencedora IS NULL
                AND (j.equipaCasa = e OR j.equipaFora = e)
                THEN 1
                END
        ) = :empates
        """)
        List<Equipa> findByNumeroDeEmpates(@Param("empates") int empates);


        ////é preciso left join e casepara se meter derrotas=0 aparecer as equipas que nunca jogaram
        @Query("""
        SELECT e
        FROM Equipa e
        LEFT JOIN e.historico j
        LEFT JOIN j.estatistica est
        GROUP BY e
        HAVING COUNT(
                CASE
                WHEN j.estado = 'ENCERRADO'
                AND est.equipaVencedora IS NOT NULL
                AND est.equipaVencedora <> e
                AND (j.equipaCasa = e OR j.equipaFora = e)
                THEN 1
                END
        ) = :derrotas
        """)
        List<Equipa> findByNumeroDeDerrotas(@Param("derrotas") int derrotas);



        @Query("""
        SELECT e
        FROM Equipa e
        WHERE SIZE(e.conquistas) = :minConquistas
        """)
        List<Equipa> findByNumeroDeConquistas(@Param("minConquistas") int minConquistas);


        @Query("""
        SELECT e
        FROM Equipa e
        WHERE NOT EXISTS (
                SELECT j FROM Jogador j
                WHERE j MEMBER OF e.jogadores
                AND j.posicaoPreferida = :posicao
        )
        """)
        List<Equipa> findEquipasSemJogadorNaPosicao(@Param("posicao") Posicao posicao);


}