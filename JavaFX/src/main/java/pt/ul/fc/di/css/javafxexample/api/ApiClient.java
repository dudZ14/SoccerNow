package pt.ul.fc.di.css.javafxexample.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.javafxexample.dto.ArbitroDto;
import pt.ul.fc.di.css.javafxexample.dto.ArbitroDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDto;
import pt.ul.fc.di.css.javafxexample.dto.EquipaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.EstatisticaDto;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDto;
import pt.ul.fc.di.css.javafxexample.dto.JogadorDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.JogoDto;
import pt.ul.fc.di.css.javafxexample.dto.JogoDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.EstatisticaDtoGet;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDto;
import pt.ul.fc.di.css.javafxexample.dto.CampeonatoDtoGet;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        .findAndRegisterModules();

    
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void createArbitro(ArbitroDto arbitro) throws Exception {
        String json = mapper.writeValueAsString(arbitro);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static List<ArbitroDtoGet> getAllArbitros() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/arbitros"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<ArbitroDtoGet>>() {});

    }

    public static void deleteArbitro(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void updateArbitro(Long id, ArbitroDto arbitro) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(arbitro);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/arbitros/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void createJogador(JogadorDto jogador) throws Exception {
        String json = mapper.writeValueAsString(jogador);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static List<JogadorDtoGet> getAllJogadores() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/jogadores"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<JogadorDtoGet>>() {});

    }

    public static void deleteJogador(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void updateJogador(Long id, JogadorDto jogador) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(jogador);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogadores/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }


    public static void createEquipa(EquipaDto equipa) throws Exception {
        String json = mapper.writeValueAsString(equipa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/equipas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static List<EquipaDtoGet> getAllEquipas() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/equipas"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<EquipaDtoGet>>() {});
    }

    public static void deleteEquipa(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/equipas/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void updateEquipa(Long id, EquipaDto equipa) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(equipa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/equipas/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void createJogo(JogoDto jogo) throws Exception {
        String json = mapper.writeValueAsString(jogo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static List<JogoDtoGet> getAllJogos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<JogoDtoGet>>() {});
    }

    public static void registrarResultado(Long id, EstatisticaDto estatistica) throws Exception {
        String json = mapper.writeValueAsString(estatistica);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogos/resultado/" + id))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static EstatisticaDtoGet getResultado(Long jogoId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/jogos/resultado/" + jogoId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }

        return mapper.readValue(response.body(), new TypeReference<EstatisticaDtoGet>() {});
    }

    public static void createCampeonato(CampeonatoDto campeonato) throws Exception {
        String json = mapper.writeValueAsString(campeonato);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/campeonatos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static List<CampeonatoDtoGet> getAllCampeonatos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/campeonatos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<CampeonatoDtoGet>>() {});
    }

    public static void updateCampeonato(Long id, CampeonatoDto campeonato) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(campeonato);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/campeonatos/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }

    public static void deleteCampeonato(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/campeonatos/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }


    public static void cancelarJogo(Long campeonatoId, Long jogoId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/campeonatos/" + campeonatoId + "/cancelar-jogo/" + jogoId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String errorMessage = response.body();
            throw new RuntimeException(errorMessage);
        }
    }


}
