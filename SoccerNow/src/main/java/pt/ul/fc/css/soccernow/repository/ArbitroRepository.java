package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.entities.Arbitro;

@Repository
public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    @Query("SELECT a FROM Arbitro a " +
       "WHERE a.numJogos = (SELECT MAX(ar.numJogos) FROM Arbitro ar WHERE ar.numJogos > 0) " +
       "AND a.numJogos > 0")
    List<Arbitro> findArbitrosComMaisJogos();

    List<Arbitro> findByNome(String nome);

    @Query("""
        SELECT a FROM Arbitro a
        WHERE (
            SELECT COUNT(j)
            FROM Jogo j
            JOIN j.arbitrosNoJogo anj
            JOIN anj.arbitros ar
            WHERE j.estado = 'ENCERRADO' AND ar.id = a.id
        ) = :min
        """)
    List<Arbitro> findArbitrosComNumeroDeJogosEncerrados(@Param("min") int min);


    @Query("""
        SELECT a FROM Arbitro a
        WHERE (
            SELECT COUNT(c)
            FROM Jogo j
            JOIN j.estatistica.cartoes c
            WHERE j.arbitrosNoJogo.arbitroPrincipal = a
        ) = :minCartoes
        """)
    List<Arbitro> findArbitrosComNumeroDeCartoesMostrados(@Param("minCartoes") int minCartoes);



}