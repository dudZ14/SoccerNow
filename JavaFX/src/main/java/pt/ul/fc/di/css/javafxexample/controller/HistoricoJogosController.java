package pt.ul.fc.di.css.javafxexample.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.EstatisticaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet.EstadoJogo;
import pt.ul.fc.di.css.javafxexample.model.DataModel;
import pt.ul.fc.di.css.javafxexample.model.Equipa;
import pt.ul.fc.di.css.javafxexample.model.Jogo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoricoJogosController {

    @FXML private TableView<Jogo> tableJogos;
    @FXML private TableColumn<Jogo, String> colEquipaCasa;
    @FXML private TableColumn<Jogo, String> colEquipaVisitante;
    @FXML private TableColumn<Jogo, String> colLocal;
    @FXML private TableColumn<Jogo, String> colData;
    @FXML private TableColumn<Jogo, String> colHora;
    @FXML private TableColumn<Jogo, String> colResultado;
    @FXML private Label labelTitulo;

    private Stage stage;
    private DataModel model;
    private Equipa equipaSelecionada;
    private Map<Long, String> idParaNomeEquipa;
    private List<Jogo> allJogos;
    private Map<Long, String> idParaResultado;  // Mapa para cache dos resultados

    public void setEquipa(Equipa equipa) {
        this.equipaSelecionada = equipa;
        labelTitulo.setText("Histórico de jogos do " + equipa.getNome());
    }

    private Jogo getJogoById(Long id) {
        if (allJogos == null) {
            System.out.println("Lista de jogos ainda não carregada");
            return null;
        }
        return allJogos.stream()
                .filter(j -> j.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void atualizarTabela() {
        if (equipaSelecionada == null) {
            tableJogos.getItems().clear();
            return;
        }


        List<Jogo> jogos = equipaSelecionada.getHistoricoJogosIds()
                .stream()
                .map(this::getJogoById)
                .filter(j -> j != null)
                .toList();

        tableJogos.getItems().setAll(jogos);
    }

    private Jogo convertDtoToModel(JogoDtoGet dto) {
        Jogo jogo = new Jogo();
        jogo.setId(dto.getId());
        jogo.setLocal(dto.getLocal());
        jogo.setEquipaCasaId(dto.getEquipaCasaId());
        jogo.setEquipaVisitanteId(dto.getEquipaVisitanteId());

        // Converte lista de jogadores para ObservableList
        jogo.setJogadoresNoJogo(FXCollections.observableArrayList(dto.getJogadoresNoJogo()));

        jogo.setArbitrosNoJogo(dto.getArbitrosNoJogo());

        // Converte estado do DTO para o do modelo
        if (dto.getEstado() != null) {
            jogo.setEstado(EstadoJogo.valueOf(dto.getEstado().name()));
        } else {
            jogo.setEstado(null);
        }

        return jogo;
    }

    public void initModel(Stage stage, DataModel model) throws IOException, InterruptedException {
        this.stage = stage;
        this.model = model;

        List<JogoDtoGet> jogosDto = ApiClient.getAllJogos();

        this.allJogos = jogosDto.stream().map(this::convertDtoToModel).collect(Collectors.toList());

        this.idParaNomeEquipa = model.getEquipaList()
                .stream()
                .collect(Collectors.toMap(Equipa::getId, Equipa::getNome));

        this.idParaResultado = new java.util.HashMap<>();
        List<Long> historicoIds = equipaSelecionada.getHistoricoJogosIds();

        for (Long jogoId : historicoIds) {
            try {
                EstatisticaDtoGet resultado = ApiClient.getResultado(jogoId);
                String res = (resultado != null && resultado.getResultado() != null) ? resultado.getResultado() : "N/A";
                idParaResultado.put(jogoId, res);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                idParaResultado.put(jogoId, "Erro ao carregar");
            }
        }


        colEquipaCasa.setCellValueFactory(cellData -> {
            long idCasa = cellData.getValue().getEquipaCasaId();
            return new javafx.beans.property.SimpleStringProperty(
                    idParaNomeEquipa.getOrDefault(idCasa, "Desconhecida"));
        });

        colEquipaVisitante.setCellValueFactory(cellData -> {
            long idVisitante = cellData.getValue().getEquipaVisitanteId();
            return new javafx.beans.property.SimpleStringProperty(
                    idParaNomeEquipa.getOrDefault(idVisitante, "Desconhecida"));
        });

        colLocal.setCellValueFactory(cellData -> {
            String local = cellData.getValue().getLocal() != null ? cellData.getValue().getLocal().getLocal() : "Desconhecido";
            return new javafx.beans.property.SimpleStringProperty(local);
        });

        colData.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getData());
        });

        colHora.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoras());
        });

        colResultado.setCellValueFactory(cellData -> {
            String res = idParaResultado.get(cellData.getValue().getId());
            return new javafx.beans.property.SimpleStringProperty(res != null ? res : "N/A");
        });

        tableJogos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableJogos.setFixedCellSize(25);
        tableJogos.prefHeightProperty().bind(
                javafx.beans.binding.Bindings.size(tableJogos.getItems()).multiply(25).add(25)
        );

        centralizarColuna(colEquipaCasa);
        centralizarColuna(colEquipaVisitante);
        centralizarColuna(colLocal);
        centralizarColuna(colData);
        centralizarColuna(colHora);
        centralizarColuna(colResultado);

        if (equipaSelecionada != null) {
            atualizarTabela();
        }


    }

    // Função auxiliar para centralizar texto nas células
    private <T> void centralizarColuna(TableColumn<T, String> coluna) {
        coluna.setCellFactory(col -> {
            TableCell<T, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/list_equipas.fxml", "SoccerNow", model, 700, 370);
    }
}
