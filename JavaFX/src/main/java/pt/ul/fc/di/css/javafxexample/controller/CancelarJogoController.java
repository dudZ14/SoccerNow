    package pt.ul.fc.di.css.javafxexample.controller;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.control.*;
    import javafx.stage.Stage;
    import pt.ul.fc.di.css.javafxexample.api.ApiClient;
    import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;
    import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet;
    import pt.ul.fc.di.css.javafxexample.model.DataModel;

    import java.util.List;
    import java.util.stream.Collectors;

    public class CancelarJogoController implements ControllerWithModel {
        private Stage stage;
        private DataModel model;
        private CampeonatoDtoGet campeonato;

        @FXML private ComboBox<JogoDtoGet> comboJogos;

        @FXML private Label labelTitulo;

        @Override
        public void initModel(Stage stage, DataModel model) {
            this.stage = stage;
            this.model = model;
        }

        public void initData(Stage stage, DataModel model, CampeonatoDtoGet campeonato) {
            this.initModel(stage, model);
            this.campeonato = campeonato;
            labelTitulo.setText(campeonato.getNome());
            carregarJogosPorJogar();
        }

        private void carregarJogosPorJogar() {
            try {
                List<JogoDtoGet> todosJogos = ApiClient.getAllJogos();

                List<JogoDtoGet> jogosDoCampeonato = todosJogos.stream()
                    .filter(j -> campeonato.getJogoIds().contains(j.getId()))
                    .filter(j -> j.getEstado() == JogoDtoGet.EstadoJogo.POR_JOGAR)
                    .collect(Collectors.toList());

                comboJogos.getItems().setAll(jogosDoCampeonato);
            } catch (Exception e) {
                Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
            }
        }



        @FXML
        void cancelarJogoSelecionado() {
            JogoDtoGet jogo = comboJogos.getSelectionModel().getSelectedItem();
            if (jogo == null) {
                Util.mostrarAvisoTemporario("❌ Selecione um jogo para cancelar", stage, "red");
                return;
            }

            try {
                ApiClient.cancelarJogo(campeonato.getId(), jogo.getId());
                comboJogos.getItems().remove(jogo);
                Util.mostrarAvisoTemporario("✅ Jogo cancelado com sucesso", stage, "green");
            } catch (Exception e) {
                Util.mostrarAvisoTemporario("❌ " + e.getMessage(), stage, "red");
            }
        }


        @FXML
        void back(ActionEvent event) {
            Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_campeonatos.fxml", "SoccerNow", model, 600, 370);
        }

        @Override
        public Stage getStage() { return stage; }
        @Override
        public void setStage(Stage stage) { this.stage = stage; }
    }
