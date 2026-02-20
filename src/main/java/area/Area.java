package area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import general.Pokemon;
import general.PokemonData;
import general.PokemonType;
import jakarta.persistence.*;
import trainer.GymLeader;

@Entity
@Table(name = "area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Area nextArea;

    @Transient
    private Random r = new Random();

    @Transient
    private Pokecenter pokecenter;

    @Transient
    private List<PokemonType> containsPokemon = new ArrayList<>();

    private boolean isUnlocked;

    @OneToOne(fetch = FetchType.LAZY)
    private GymLeader gymLeader;

    public Area(String name, GymLeader gymLeader, boolean isUnlocked, Area nextArea, Pokecenter pokecenter) {
        this.name = name;
        this.gymLeader = gymLeader;
        this.isUnlocked = isUnlocked;
        this.nextArea = nextArea;
        this.pokecenter = pokecenter;
    }

    public Area() {
        this.name = null;
        this.nextArea = null;
        this.pokecenter = null;
    }

    public void setContainsPokemon(List<PokemonType> containsPokemon) {
        this.containsPokemon = containsPokemon;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public GymLeader getGymLeader() {
        return gymLeader;
    }

    public void setGymLeader(GymLeader gymLeader) {
        this.gymLeader = gymLeader;
    }

    public Area getNextArea() {
        return nextArea;
    }

    public void setNextArea(Area nextArea) {
        this.nextArea = nextArea;
    }

    public Pokemon getRandomPokemonFromArea(int level) {
        List<PokemonData> pokeData = Arrays.asList(PokemonData.values());
        ArrayList<PokemonData> collect = new ArrayList<>();
        for (PokemonData p : pokeData) {
            if (containsPokemon.contains(p.pokemonType)) {
                collect.add(p);
            }
        }
        int randomPokemonIndex = r.nextInt(collect.size());
        PokemonData randomPokemonData = collect.get(randomPokemonIndex);
        Pokemon randomPokemon = new Pokemon(randomPokemonData);
        int lowestLevel = level - 5 > 0 ? level - 5 : level;
        int highestLevel = lowestLevel + level;
        int randomLevel = r.nextInt(highestLevel - lowestLevel);
        while (randomLevel <= 0) {
            lowestLevel = level - 5 > 0 ? level - 5 : level;
            highestLevel = lowestLevel + level;
            randomLevel = r.nextInt(highestLevel - lowestLevel);
        }
        randomPokemon.setLevel(randomLevel);
        randomPokemon.setMaxHp(randomPokemon.getLevel() * 10);
        randomPokemon.setCurrentHp(randomPokemon.getMaxHp());
        return randomPokemon;
    }

    public Pokecenter getPokecenter() {
        return pokecenter;
    }
}
