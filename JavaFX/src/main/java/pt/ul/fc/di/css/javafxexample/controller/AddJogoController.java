package pt.ul.fc.di.css.javafxexample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxexample.api.ApiClient;
import pt.ul.fc.di.css.javafxexample.dto.*;
import pt.ul.fc.di.css.javafxexample.model.DataModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddJogoController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private TextField fieldLocal;
    
    @FXML private DatePicker pickerDataInicio;
    @FXML private TextField fieldHoraInicio;
    @FXML private DatePicker pickerDataFim;
    @FXML private TextField fieldHoraFim;
    @FXML private ComboBox<EquipaDtoGet> comboEquipaCasa;
    @FXML private ComboBox<EquipaDtoGet> comboEquipaVisitante;
    @FXML private ComboBox<ArbitroDtoGet> comboArbitroPrincipal;
    private final List<ComboBox<JogadorDtoGet>> jogadoresCasaBoxes = new ArrayList<>();
    private final List<ComboBox<JogadorDto.Posicao>> posicoesCasaFields = new ArrayList<>();
    private final List<ComboBox<JogadorDto.Posicao>> posicoesVisitanteFields = new ArrayList<>();
    private final List<ComboBox<JogadorDtoGet>> jogadoresVisitanteBoxes = new ArrayList<>();
    @FXML private ListView<ArbitroDtoGet> listArbitrosAssistentes;

    @FXML private VBox boxJogadoresCasa;
    @FXML private VBox boxJogadoresVisitante;

    @FXML private Button btnAdd;


    @FXML
    void addJogo() {

        try {
            String local = fieldLocal.getText().trim();
            LocalDate dataInicio = pickerDataInicio.getValue();
            LocalDate dataFim = pickerDataFim.getValue();
            EquipaDtoGet equipaCasa = comboEquipaCasa.getValue();
            EquipaDtoGet equipaVisitante = comboEquipaVisitante.getValue();
            ArbitroDtoGet arbitroPrincipal = comboArbitroPrincipal.getValue();
            List<ArbitroDtoGet> assistentes = listArbitrosAssistentes.getSelectionModel().getSelectedItems();
            if (assistentes == null || assistentes.isEmpty()) {
                Util.mostrarAvisoTemporario("❌ Deve selecionar pelo menos um árbitro assistente", stage, "red");
                return;
            }

            String horaInicioText = fieldHoraInicio.getText().trim();
            String horaFimText = fieldHoraFim.getText().trim();

            if (horaInicioText.isEmpty() || horaFimText.isEmpty()) {
                Util.mostrarAvisoTemporario("❌ As horas devem estar preenchidas", stage, "red");
                return;
            }

            LocalTime horaInicio;
            LocalTime horaFim;

            try {
                horaInicio = LocalTime.parse(horaInicioText);
                horaFim = LocalTime.parse(horaFimText);
            } catch (Exception e) {
                Util.mostrarAvisoTemporario("❌ Formato de hora inválido. Use HH:mm:ss ou HH:mm", stage, "red");
                return;
            }

            if (!validInputs(local, dataInicio, dataFim, equipaCasa, equipaVisitante, arbitroPrincipal)) {
                return;
            }

            LocalDateTime dataHoraInicio = LocalDateTime.of(dataInicio, horaInicio);
            LocalDateTime dataHoraFim = LocalDateTime.of(dataFim, horaFim);

            LocalDto localDto = new LocalDto(local, dataHoraInicio, dataHoraFim);
            // Converte para um Set para evitar duplicados
            Set<ArbitroDtoGet> assistentesSet = new HashSet<>(assistentes);
            assistentesSet.add(arbitroPrincipal);

            List<Long> assistentesIds = assistentesSet.stream()
                .map(ArbitroDtoGet::getId)
                .collect(Collectors.toList());

            ArbitrosNoJogoDto arbitrosDto = new ArbitrosNoJogoDto(
                assistentesIds,
                arbitroPrincipal.getId()
            );


            List<JogadorNoJogoDto> jogadoresNoJogo = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                JogadorDtoGet jogadorCasa = jogadoresCasaBoxes.get(i).getValue();
                JogadorDto.Posicao posicaoCasa = posicoesCasaFields.get(i).getValue();

                JogadorDtoGet jogadorVisitante = jogadoresVisitanteBoxes.get(i).getValue();
                JogadorDto.Posicao posicaoVisitante = posicoesVisitanteFields.get(i).getValue();

                if (jogadorCasa != null && posicaoCasa != null) {
                    jogadoresNoJogo.add(new JogadorNoJogoDto(jogadorCasa.getId(), posicaoCasa.name(), jogadorCasa.getNome()));
                }

                if (jogadorVisitante != null && posicaoVisitante != null) {
                    jogadoresNoJogo.add(new JogadorNoJogoDto(jogadorVisitante.getId(), posicaoVisitante.name(), jogadorVisitante.getNome()));
                }
            }

            JogoDto jogoDto = new JogoDto(
                localDto,
                equipaCasa.getId(),
                equipaVisitante.getId(),
                jogadoresNoJogo, 
                arbitrosDto
            );

            ApiClient.createJogo(jogoDto);
            Util.mostrarAvisoTemporario("✅ Jogo criado com sucesso!", stage, "green");

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }
    }

    private boolean validInputs(String local, LocalDate dataInicio, LocalDate dataFim,  EquipaDtoGet equipaCasa,
                            EquipaDtoGet equipaVisitante, ArbitroDtoGet arbitroPrincipal) {

        if (local.isEmpty()) {
            Util.mostrarAvisoTemporario("❌ Local é obrigatório", stage, "red");
            return false;
        }

        if (dataInicio == null) {
            Util.mostrarAvisoTemporario("❌ Data início é obrigatória", stage, "red");
            return false;
        }

        if (dataFim == null) {
            Util.mostrarAvisoTemporario("❌ Data fim é obrigatória", stage, "red");
            return false;
        }


        if (equipaCasa == null || equipaVisitante == null) {
            Util.mostrarAvisoTemporario("❌ Ambas as equipas devem ser selecionadas", stage, "red");
            return false;
        }

        if (equipaCasa.getId().equals(equipaVisitante.getId())) {
            Util.mostrarAvisoTemporario("❌ Equipa casa e visitante devem ser diferentes", stage, "red");
            return false;
        }

        if (arbitroPrincipal == null) {
            Util.mostrarAvisoTemporario("❌ Árbitro principal é obrigatório", stage, "red");
            return false;
        }

        // Verificar jogadores repetidos na equipa casa
        List<JogadorDtoGet> selecionadosCasa = jogadoresCasaBoxes.stream()
            .map(ComboBox::getValue)
            .filter(j -> j != null)
            .collect(Collectors.toList());

        Set<Long> idsUnicosCasa = selecionadosCasa.stream()
            .map(JogadorDtoGet::getId)
            .collect(Collectors.toSet());

        if (idsUnicosCasa.size() < selecionadosCasa.size()) {
            Util.mostrarAvisoTemporario("❌ A equipa da casa tem jogadores repetidos", stage, "red");
            return false;
        }

        // Verificar jogadores repetidos na equipa visitante
        List<JogadorDtoGet> selecionadosVisitante = jogadoresVisitanteBoxes.stream()
            .map(ComboBox::getValue)
            .filter(j -> j != null)
            .collect(Collectors.toList());

        Set<Long> idsUnicosVisitante = selecionadosVisitante.stream()
            .map(JogadorDtoGet::getId)
            .collect(Collectors.toSet());

        if (idsUnicosVisitante.size() < selecionadosVisitante.size()) {
            Util.mostrarAvisoTemporario("❌ A equipa visitante tem jogadores repetidos", stage, "red");
            return false;
        }


        // Validação dos jogadores da equipa casa
        for (int i = 0; i < jogadoresCasaBoxes.size(); i++) {
            JogadorDtoGet jogador = jogadoresCasaBoxes.get(i).getValue();
            JogadorDto.Posicao posicao = posicoesCasaFields.get(i).getValue();

            if ((jogador == null && posicao != null) || (jogador != null && posicao == null)) {
                Util.mostrarAvisoTemporario("❌ Jogador e posição da equipa casa devem estar preenchidos", stage, "red");
                return false;
            }
        }

        // Validação dos jogadores da equipa visitante
        for (int i = 0; i < jogadoresVisitanteBoxes.size(); i++) {
            JogadorDtoGet jogador = jogadoresVisitanteBoxes.get(i).getValue();
            JogadorDto.Posicao posicao = posicoesVisitanteFields.get(i).getValue();

            if ((jogador == null && posicao != null) || (jogador != null && posicao == null)) {
                Util.mostrarAvisoTemporario("❌ Jogador e posição da equipa visitante devem estar preenchidos", stage, "red");
                return false;
            }
        }

        return true;
    }

    //a partir das equipa selecionada, carrega os jogadores para essa equipa
    private void carregarJogadores(EquipaDtoGet equipa, VBox targetBox) {
        try {
            targetBox.getChildren().clear();

            List<JogadorDtoGet> jogadores = equipa.getJogadores();
            List<ComboBox<JogadorDtoGet>> caixasJogador = new ArrayList<>();
            List<ComboBox<JogadorDto.Posicao>> caixasPosicao = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                HBox linha = new HBox(10);

                ComboBox<JogadorDtoGet> comboJogador = new ComboBox<>();
                comboJogador.setPrefWidth(180);

                ComboBox<JogadorDto.Posicao> comboPosicao = new ComboBox<>();
                comboPosicao.getItems().addAll(JogadorDto.Posicao.values());
                comboPosicao.setPromptText("Posição");
                comboPosicao.setPrefWidth(120);

                linha.getChildren().addAll(comboJogador, comboPosicao);
                targetBox.getChildren().add(linha);

                caixasJogador.add(comboJogador);
                caixasPosicao.add(comboPosicao);
            }

            if (targetBox == boxJogadoresCasa) {
                jogadoresCasaBoxes.clear();
                jogadoresCasaBoxes.addAll(caixasJogador);
                posicoesCasaFields.clear();
                posicoesCasaFields.addAll(caixasPosicao);

            } else {
                jogadoresVisitanteBoxes.clear();
                jogadoresVisitanteBoxes.addAll(caixasJogador);
                posicoesVisitanteFields.clear();
                posicoesVisitanteFields.addAll(caixasPosicao);

            }

            for (ComboBox<JogadorDtoGet> combo : caixasJogador) {
                combo.getItems().setAll(jogadores); // carrega todos os jogadores da equipa
            }

        } catch (Exception e) {
            Util.mostrarAvisoTemporario("❌ Erro ao carregar jogadores", stage, "red");
            e.printStackTrace();
        }
    }


    @FXML
    void back(MouseEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/di/css/javafxexample/view/jogos.fxml", "SoccerNow", model, 600, 400);
    }

    @FXML
    public void initialize() {
        try {
            comboEquipaCasa.getItems().addAll(ApiClient.getAllEquipas());
            comboEquipaVisitante.getItems().addAll(ApiClient.getAllEquipas());
            List<ArbitroDtoGet> arbitros = ApiClient.getAllArbitros();
            comboArbitroPrincipal.getItems().addAll(arbitros);
            listArbitrosAssistentes.getItems().addAll(arbitros);
            listArbitrosAssistentes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            comboEquipaCasa.setOnAction(e -> carregarJogadores(comboEquipaCasa.getValue(), boxJogadoresCasa));
            comboEquipaVisitante.setOnAction(e -> carregarJogadores(comboEquipaVisitante.getValue(), boxJogadoresVisitante));

            //atualizar arbitros assistentes a partir do arbitro principal
            comboArbitroPrincipal.setOnAction(e -> {
                ArbitroDtoGet principal = comboArbitroPrincipal.getValue();
                List<ArbitroDtoGet> arbitrosTotais = new ArrayList<>();
                try {
                    arbitrosTotais = ApiClient.getAllArbitros();
                } catch (Exception ex) {
                    Util.mostrarAvisoTemporario("❌ Erro ao carregar árbitros: " + ex.getMessage(), stage, "red");
                    ex.printStackTrace();
                }

                listArbitrosAssistentes.getItems().clear();

                if (principal != null) {
                    // Filtra para não incluir o árbitro principal
                    List<ArbitroDtoGet> assistentes = arbitrosTotais.stream()
                        .filter(a -> !a.getId().equals(principal.getId()))
                        .collect(Collectors.toList());

                    listArbitrosAssistentes.getItems().addAll(assistentes);
                } else {
                    // Se nenhum principal selecionado, mostrar todos
                    listArbitrosAssistentes.getItems().addAll(arbitrosTotais);
                }
            });


        } catch (Exception e) {
            System.out.println(e.getMessage());
            Util.mostrarAvisoTemporario("❌ Falha ao carregar dados: " + e.getMessage(), stage, "red");
            e.printStackTrace();
        }

    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model já foi inicializado");
        }
        this.stage = stage;
        this.model = model;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
