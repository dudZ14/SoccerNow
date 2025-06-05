package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto.Posicao;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Equipa;
import pt.ul.fc.di.css.javafxexample.model.Jogador;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


import java.util.List;

public class ListEquipaController implements ControllerWithModel {
    private Stage stage;
    private DataModel model;

    @FXML private ListView<Equipa> listEquipas;

    @FXML private Label labelNome;
    @FXML private Label labelNumJogadores;
    @FXML private Label labelNumJogos;
    @FXML private Label labelNumVitorias;
    @FXML private Label labelNumConquistas;

    @FXML private HBox boxNome;
    @FXML private HBox boxNumJogadores;
    @FXML private HBox boxNumJogos;
    @FXML private HBox boxNumVitorias;
    @FXML private HBox boxNumConquistas;


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
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/equipas.fxml", "SoccerNow", model, 600, 400);
    }
    
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
        hideDetails();
        setEquipaList();
        setClickListener();
    }

    @FXML
    void removerEquipa() {
        Equipa selected = listEquipas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione uma equipa primeiro", stage, "orange");
            return;
        }

        try {
            ApiClient.deleteEquipa(selected.getId()); 
            model.getEquipaList().remove(selected);
            listEquipas.getSelectionModel().clearSelection();
            Util.mostrarAvisoTemporario("✅ Equipa removida com sucesso",stage,"green");
            hideDetails();
        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(),stage,"red");

        }
    }

    @FXML
    void editarEquipa() {
        Equipa selected = listEquipas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione uma equipa primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/atualizar_equipa.fxml"));
            Parent root = loader.load();

            AtualizarEquipaController controller = loader.getController();
            controller.initModel(stage, model, selected);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Editar Equipa");
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ " + e.getMessage(),stage,"red");
        }
    }


    private void setEquipaList() {
        try {
            List<EquipaDtoGet> dtoList = ApiClient.getAllEquipas();

            List<Equipa> equipas = dtoList.stream().map(dto -> {
                List<Jogador> jogadores = dto.getJogadores().stream().map(j -> {
                    Jogador jogador = new Jogador(j.getNome(), j.getEmail(), j.getPassword(), Posicao.valueOf(j.getPosicaoPreferida()));
                    jogador.setId(j.getId());
                    jogador.setNumeroGolos(j.getNumeroGolos());
                    jogador.setNumeroJogos(j.getNumeroJogos());
                    jogador.setNumeroCartoes(j.getNumeroCartoes());
                    return jogador;
                }).toList();

                List<String> conquistas = dto.getConquistas().stream()
                        .map(c -> String.format("%d - %dº lugar", c.getCampeonatoId(), c.getPosicao()))
                        .toList();
                        
                        Equipa equipa = new Equipa(dto.getNome(), jogadores, dto.getHistoricoJogosIds(), conquistas);
                        equipa.setId(dto.getId());
                        equipa.setNumeroVitorias(dto.getNumeroVitorias());
                        return equipa;
                    }).toList();
                    
                    model.setEquipaList(equipas);
                    listEquipas.setItems(model.getEquipaList());
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
    private void hideDetails() {
        boxNome.setVisible(false);
        boxNumJogadores.setVisible(false);
        boxNumJogos.setVisible(false);
        boxNumVitorias.setVisible(false);
        boxNumConquistas.setVisible(false);
    }

    private void showDetails() {
        boxNome.setVisible(true);
        boxNumJogadores.setVisible(true);
        boxNumJogos.setVisible(true);
        boxNumVitorias.setVisible(true);
        boxNumConquistas.setVisible(true);
    }


    private void setClickListener() {
        listEquipas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                hideDetails();
            } else {
                labelNome.setText(newSelection.getNome());
                labelNumJogadores.setText(String.valueOf(newSelection.getNumeroJogadores()));
                labelNumJogos.setText(String.valueOf(newSelection.getNumeroJogos()));
                labelNumConquistas.setText(String.valueOf(newSelection.getNumeroConquistas()));
                labelNumVitorias.setText(String.valueOf(newSelection.getNumeroVitorias()));
                showDetails();
            }
        });
    }

    @FXML
    void mostrarJogadores() {
        Equipa selected = listEquipas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione uma equipa primeiro", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/jogadores_equipa.fxml"));
            Parent root = loader.load();

            JogadoresEquipaController controller = loader.getController();
            controller.initModel(stage, model);
            controller.setEquipa(selected);
            controller.setJogadores(selected.getJogadores());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Jogadores da Equipa " + selected.getNome());
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("Erro ao abrir lista de jogadores: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarHistorico() {
        Equipa selected = listEquipas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione uma equipa primeiro", stage, "orange");
            return;
        }

        if (selected.getHistoricoJogosIds().size() == 0) {
            Util.mostrarAvisoTemporario("A equipa ainda não tem histórico", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/historico_jogos.fxml"));
            Parent root = loader.load();

            HistoricoJogosController controller = loader.getController();
            controller.setEquipa(selected);  
            controller.initModel(stage, model);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Histórico da equipa " + selected.getNome());
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("Erro ao abrir histórico de jogos: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarConquistas() {
        Equipa selected = listEquipas.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Util.mostrarAvisoTemporario("Selecione uma equipa primeiro", stage, "orange");
            return;
        }

        if (selected.getConquistas().size() == 0) {
            Util.mostrarAvisoTemporario("A equipa ainda não tem conquistas", stage, "orange");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/conquistas.fxml"));
            Parent root = loader.load();

            ConquistasController controller = loader.getController();
            controller.initModel(stage, model);
            controller.setEquipa(selected);  

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/pt/ul/fc/di/css/javafxexample/view/init.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Conquistas da equipa " + selected.getNome());
            stage.show();

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("Erro ao abrir conquistas de jogos: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }



}
