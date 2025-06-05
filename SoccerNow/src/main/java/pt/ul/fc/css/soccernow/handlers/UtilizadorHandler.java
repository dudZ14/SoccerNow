package pt.ul.fc.css.soccernow.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.entities.Utilizador;
import pt.ul.fc.css.soccernow.repository.ArbitroRepository;
import pt.ul.fc.css.soccernow.repository.JogadorRepository;

@Service
public class UtilizadorHandler {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private JogadorRepository jogadorRepository;

    public List<Utilizador> getAllUtilizadores() {
        List<Utilizador> utilizadores = new ArrayList<>();
        utilizadores.addAll(arbitroRepository.findAll());
        utilizadores.addAll(jogadorRepository.findAll());
        return utilizadores;
    }
}

