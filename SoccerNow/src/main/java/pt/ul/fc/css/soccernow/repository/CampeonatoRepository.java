package pt.ul.fc.css.soccernow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.soccernow.entities.Campeonato;
import pt.ul.fc.css.soccernow.entities.Equipa;

import java.util.List;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {

    // Filtrar por nome (contém, case insensitive)
    List<Campeonato> findByNomeIgnoreCaseContaining(String nome);

    // Filtrar por equipa participante
    @Query("SELECT c FROM Campeonato c JOIN c.equipas e WHERE e = :equipa")
    List<Campeonato> findByEquipa(@Param("equipa") Equipa equipa);

    // Filtrar por número mínimo de jogos realizados (jogos ENCERRADOS)
    @Query("SELECT c FROM Campeonato c WHERE " +
           "(SELECT COUNT(j) FROM Jogo j WHERE j.campeonato = c AND j.estado = pt.ul.fc.css.soccernow.enumerados.EstadoJogo.ENCERRADO) = :minJogosRealizados")
    List<Campeonato> findByJogosRealizados(@Param("minJogosRealizados") long minJogosRealizados);

    // Filtrar por número mínimo de jogos a realizar (jogos POR_JOGAR ou OUTROS estados não ENCERRADOS)
    @Query("SELECT c FROM Campeonato c WHERE " +
           "(SELECT COUNT(j) FROM Jogo j WHERE j.campeonato = c AND j.estado <> pt.ul.fc.css.soccernow.enumerados.EstadoJogo.ENCERRADO) = :minJogosPorRealizar")
    List<Campeonato> findByJogosPorRealizar(@Param("minJogosPorRealizar") long minJogosPorRealizar);
}
