package pt.ul.fc.di.css.javafxexample.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorNoJogoDto;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet.EstadoJogo;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Jogo;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListJogoController implements ControllerWithModel {

    private Stage stage;
    private DataModel model;

    @FXML private ListView<Jogo> listJogos;

    @FXML private VBox boxDetalhes;
    @FXML private Label labelLocal;
    @FXML private Label labelDia;
    @FXML private Label labelHoraInicio;
    @FXML private Label labelHoraFim;
    @FXML private Label labelEquipaCasa;
    @FXML private Label labelEquipaVisitante;
    @FXML private Label labelEstado;
    @FXML private HBox boxResultado;
    @FXML private Label labelResultado;
    

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogos.fxml", "SoccerNow", model, 600, 400);
    }

    private void hideDetails() {
        boxDetalhes.setVisible(false);
    }

    private void showDetails() {
        boxDetalhes.setVisible(true);
    }


    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        loadJogos();
        hideDetails();

        listJogos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalhes(newVal); // Não precisa mais de try/catch aqui
            } else {
                hideDetails();
            }
        });

    }

    private Jogo converterParaJogo(JogoDtoGet dto) {
        ObservableList<JogadorNoJogoDto> jogadores = FXCollections.observableArrayList(dto.getJogadoresNoJogo());

        try {
            List<JogadorDtoGet> todosJogadores = ApiClient.getAllJogadores();

            for (JogadorNoJogoDto j : jogadores) {
                todosJogadores.stream()
                    .filter(jog -> jog.getId().equals(j.getJogadorId()))
                    .findFirst()
                    .ifPresent(jogadorEncontrado -> {
                        j.setNomeJogador(jogadorEncontrado.getNome());
                        j.setNomeEquipa(jogadorEncontrado.getNomeEquipa()); 
                    });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Jogo(
            dto.getId(),
            dto.getLocal(),
            dto.getEquipaCasaId(),
            dto.getEquipaVisitanteId(),
            jogadores,
            dto.getArbitrosNoJogo(),
            dto.getEstado()
        );
    }


    private void loadJogos() {
        try {
            List<JogoDtoGet> jogosDto = ApiClient.getAllJogos();
            List<Jogo> jogos = jogosDto.stream()
                .map(dto -> converterParaJogo(dto)) 
                .toList();
            model.setJogoList(jogos);
            listJogos.setItems(model.getJogoList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void registarJogo() {
        Jogo selected = listJogos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um jogo primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/registrar_jogo.fxml"));
            Parent root = loader.load();

            RegistarResultadoController controller = loader.getController();
            controller.initModel(stage, model,selected);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setHeight(500);
            stage.setScene(scene);
            stage.setTitle("Registar Jogo");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Util.mostrarAvisoTemporario("❌ Erro ao abrir formulário de registo", stage, "red");
        }
    }

    @FXML
    void mostrarJogadores() {
        Jogo selected = listJogos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um jogo primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/participantes.fxml"));
            Parent root = loader.load();

            ParticipantesController controller = loader.getController();
            controller.initModel(stage, model);
            controller.setJogadores(selected.getJogadoresNoJogo());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Jogadores do Jogo");
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            Util.mostrarAvisoTemporario("❌ Erro ao abrir formulário de registo", stage, "red");
        }
    }

    @FXML
    void mostrarArbitros() {
        Jogo selected = listJogos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione um jogo primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/arbitro_jogo.fxml"));
            Parent root = loader.load();

            ArbitroJogoController controller = loader.getController();
            controller.initModel(stage, model);
            controller.setArbitros(selected.getArbitrosNoJogo());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Árbitros do Jogo");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Util.mostrarAvisoTemporario("❌ Erro ao abrir tela de árbitros", stage, "red");
        }
    }


    private String getNomeEquipaPorId(Long id, List<EquipaDtoGet> equipas) {
        if (equipas == null || equipas.isEmpty()) {
            return null;
        }

        for (EquipaDtoGet equipa : equipas) {
            if (equipa.getId() == id) {
                return equipa.getNome();
            }
        }
        return null;
    }

    private String estadoParaTexto(EstadoJogo estado) {
        if (estado == null) return "";
        return switch (estado) {
            case ENCERRADO -> "Encerrado";
            case CANCELADO -> "Cancelado";
            case POR_JOGAR -> "Agendado";
        };
    }


    private void mostrarDetalhes(Jogo jogo) {
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            labelLocal.setText(jogo.getLocal().getLocal());
            labelDia.setText(jogo.getLocal().getDataHoraInicio().format(dataFormatter));
            labelHoraInicio.setText(jogo.getLocal().getDataHoraInicio().format(horaFormatter));
            labelHoraFim.setText(jogo.getLocal().getDataHoraFim().format(horaFormatter));
            labelEstado.setText(estadoParaTexto(jogo.getEstado()));


            List<EquipaDtoGet> equipas = ApiClient.getAllEquipas();
            String nomeEquipaCasa = getNomeEquipaPorId(jogo.getEquipaCasaId(), equipas);
            String nomeEquipaVisitante = getNomeEquipaPorId(jogo.getEquipaVisitanteId(), equipas);

            labelEquipaCasa.setText(nomeEquipaCasa != null ? nomeEquipaCasa : "Desconhecido");
            labelEquipaVisitante.setText(nomeEquipaVisitante != null ? nomeEquipaVisitante : "Desconhecido");

            // Oculta inicialmente o resultado
            boxResultado.setVisible(false);

            try {
                var resultado = ApiClient.getResultado(jogo.getId());
                if (resultado != null && resultado.getResultado() != null) {
                    labelResultado.setText(resultado.getResultado());
                    boxResultado.setVisible(true);
                }
            } catch (IOException | InterruptedException ex) {
                System.out.println("Resultado ainda não disponível para o jogo ID " + jogo.getId());
                // Só loga no console, não deixa propagar
            } catch (Exception ex) {
                // Para pegar outros erros inesperados e evitar crash
                ex.printStackTrace();
                System.out.println("Erro inesperado ao buscar resultado: " + ex.getMessage());
            }

            showDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
