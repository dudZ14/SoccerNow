
package pt.ul.fc.css.soccernow.dto;

public class EquipaPontuacaoDto {
    private String nome;
    private Integer pontuacao;

    public EquipaPontuacaoDto(String nome, Integer pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public String getNome() { return nome; }
    public Integer getPontuacao() { return pontuacao; }
}
