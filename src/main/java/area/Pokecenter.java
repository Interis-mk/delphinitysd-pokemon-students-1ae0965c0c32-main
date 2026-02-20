package area;

import java.util.List;

import general.Pokemon;

public class Pokecenter {

    private final String name;

    public Pokecenter(String name) {
        this.name = name;
    }

    public void healPokemon(List<Pokemon> pokemonToHeal) {
        for (Pokemon p : pokemonToHeal) {
            int maxHp = p.getMaxHp();
            p.setCurrentHp(maxHp);
        }
    }
}
