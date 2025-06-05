package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.entities.Jogo;
import pt.ul.fc.css.soccernow.enumerados.EstadoJogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    
    List<Jogo> findByEstado(EstadoJogo estado);

    @Query("""
        SELECT j
        FROM Jogo j
        WHERE j.estado = 'ENCERRADO'
    """)
    List<Jogo> findJogosRealizados();

    @Query("""
        SELECT j
        FROM Jogo j
        WHERE j.estado = 'POR_JOGAR'
    """)
    List<Jogo> findJogosPorRealizar();

    @Query("""
        SELECT j
        FROM Jogo j
        WHERE SIZE(j.estatistica.golos) = :quantidade
    """)
    List<Jogo> findJogosPorQuantidadeDeGolos(@Param("quantidade") int quantidade);

    @Query("""
        SELECT j
        FROM Jogo j
        WHERE j.local.local = :nomeLocal
    """)
    List<Jogo> findJogosPorNomeLocal(@Param("nomeLocal") String nomeLocal);


    @Query("""
        SELECT j
        FROM Jogo j
        WHERE FUNCTION('date_part', 'hour', j.local.dataHoraInicio) BETWEEN 6 AND 11
        """)
    List<Jogo> findJogosTurnoManha();

    @Query("""
        SELECT j
        FROM Jogo j
        WHERE FUNCTION('date_part', 'hour', j.local.dataHoraInicio) BETWEEN 12 AND 17
        """)
    List<Jogo> findJogosTurnoTarde();


    @Query("""
        SELECT j
        FROM Jogo j
        WHERE FUNCTION('date_part', 'hour', j.local.dataHoraInicio) BETWEEN 18 AND 23
        """)
    List<Jogo> findJogosTurnoNoite();


}