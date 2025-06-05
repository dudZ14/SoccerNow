package pt.ul.fc.di.css.javafxexample.model;

public class ConquistaTableEntry {
    private final String nome;
    private final String modalidade;
    private final String posicao;
    private final int numEquipas;

    public ConquistaTableEntry(String nome, String modalidade, String posicao, int numEquipas) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.posicao = posicao;
        this.numEquipas = numEquipas;
    }

    public String getNome() {
        return nome;
    }

    public String getModalidade() {
        return modalidade;
    }

    public String getPosicao() {
        return posicao;
    }
    
    public int getNumEquipas() {
        return numEquipas;
    }
}

