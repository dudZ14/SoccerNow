package pt.ul.fc.di.css.javafxexample.dto;
import java.util.List;

public class EstatisticaDto {
    private List<GoloDto> golos;
    private List<CartaoJogadorDto> cartoes;

    public EstatisticaDto() {}

    public EstatisticaDto(List<GoloDto> golos, List<CartaoJogadorDto> cartoes) {
        this.golos = golos;
        this.cartoes = cartoes;
    }

    public List<GoloDto> getGolos() {
        return golos;
    }

    public void setGolos(List<GoloDto> golos) {
        this.golos = golos;
    }


    public List<CartaoJogadorDto> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoJogadorDto> cartoes) {
        this.cartoes = cartoes;
    }
    
}