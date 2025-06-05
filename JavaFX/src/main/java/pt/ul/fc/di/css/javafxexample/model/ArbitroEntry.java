package pt.ul.fc.di.css.javafxexample.model;

public class ArbitroEntry {
    
    private final String arbitroPrincipal;
    private final String arbitroAssistente;

    public ArbitroEntry(String arbitroPrincipal, String arbitroAssistente) {
        this.arbitroPrincipal = arbitroPrincipal;
        this.arbitroAssistente = arbitroAssistente;
    }

    public String getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public String getArbitroAssistente() {
        return arbitroAssistente;
    }
}