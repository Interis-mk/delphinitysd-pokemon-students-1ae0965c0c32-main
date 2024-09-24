package general;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.processing.Generated;

import battle.Attack;
import trainer.Trainer;

public class Pokemon {
	private final int maxPP;
	private final int currentPP;
	private final Random r = new Random();
	private PokemonData pokedata;
	private Trainer owner;
	private int currentHp;
	private int maxHp;
	private int level;
	private int xpToNext;
	private int currentXp;

	public Pokemon(PokemonData pokedata) {
		this.pokedata = pokedata;
		this.currentHp = pokedata.maxHp;
		this.maxHp = pokedata.maxHp;
		this.currentPP = pokedata.maxPp;
		this.maxPP = pokedata.maxPp;
		this.level = 5;
		this.xpToNext = 8;
		this.currentXp = 0;
	}

	public PokemonData getPokedata() {
		return pokedata;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Trainer getOwner() {
		return owner;
	}

	public void setOwner(Trainer owner) {
		this.owner = owner;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	private int getXpToNext() {
		return xpToNext;
	}

	public List<Attack> getAttacks() {
		List<Attack> attacks = new ArrayList<>();
		attacks.addAll(pokedata.specificAttacks);
		attacks.addAll(pokedata.pokemonType.typeAttacks);
		return attacks;
	}

	// TODO: US-PKM-O-4E
	private void evolve() {
		System.out.println("----------------");
		String oldPokemon = this.pokedata.name();
		System.out.println("Your " + this.pokedata.name() + " is evolving!");
		try {
			for (int i = 0; i < 5; i++) {
				System.out.print(".");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" ");
		this.pokedata = pokedata.evolvesIn;
		System.out.println("----------------");
		System.out.println("Your " + oldPokemon + " Evolved into " + this.pokedata.name());
	}

	// TODO: US-PKM-O-4D
	public void gainXp(int amount) {
		if (currentXp + amount >= xpToNext) {
			int remainingXp = (currentXp + amount) - xpToNext;
			levelUp();
			currentXp = 0;
			xpToNext = xpToNext + r.nextInt(xpToNext + 5);

			gainXp(remainingXp);
		} else {
			currentXp = currentXp + amount;
			status();
		}
	}

	// TODO: US-PKM-O-4C
	private void levelUp() {
		maxHp = maxHp + r.nextInt(maxHp / 10);
		currentHp = currentHp + r.nextInt(currentHp / 10);
		level++;
		if (evolveCheck()) {
			evolve();
		}
		System.out.println("----------------");
		System.out.println(pokedata.name() + " grew to level " + level);
	}

	// TODO: US-PKM-O-4E
	private boolean evolveCheck() {
		if (pokedata.evolvesIn != null && level >= pokedata.evolutionLevel) {
			return true;
		}
		return false;
	}

	// TODO: US-PKM-O-4
	public void attack(Pokemon otherPokemon, Attack attack) {
		int power = attack.getPower();
		if (otherPokemon.getCurrentHp() - power < 0) {
			otherPokemon.setCurrentHp(0);
		} else {
			setCurrentHp(currentHp = otherPokemon.getCurrentHp() - power);
		}
	}

	// TODO: US-PKM-O-4B
	public Attack getRandomAttack() {
		getAttacks();
		int attackSize = getAttacks().size();
		int randomAttackIndex = r.nextInt(attackSize);
		return getAttacks().get(randomAttackIndex);
	}

	public double getHpPercentage() {
		return currentHp / maxHp * 100;
	}

	public void status() {
		System.out.println("----------------");
		System.out.println(this.getPokedata().name());
		System.out.println("LVL " + this.level);
		System.out.println("HP " + this.currentHp + "/" + this.maxHp);
		System.out.println("PP " + this.currentPP + "/" + this.maxPP);
		System.out.println("XP " + this.currentXp + "/" + this.xpToNext);
		System.out.println("----------------");
	}

	// TODO: US-PKM-O-5
	public boolean isKnockout() {
		if (currentHp <= 0) {
			return true;
		}
		return false;
	}
}
