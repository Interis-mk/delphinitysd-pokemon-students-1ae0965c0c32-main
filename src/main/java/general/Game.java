package general;

import java.util.*;

import area.Area;
import area.Pokecenter;
import battle.Battle;
import item.ItemType;
import item.Shop;
import trainer.Badge;
import trainer.GymLeader;
import trainer.Trainer;


public class Game {

	private static final ArrayList<Area> areas = new ArrayList<>();
	private static final Scanner sc = new Scanner(System.in);
	private static Trainer trainer = null;
	// set up the game in this static block
	static {

		// PEWTER City
		Pokecenter pewterCenter = new Pokecenter("Pewter City's Pokecenter");
		Area pewterCity = new Area("Pewter city", null, true, null, pewterCenter);
		Shop Pewtershop = new Shop("pewtershop");
		pewterCity.setContainsPokemon(
				Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

		// VIRIDIAN City
		Pokecenter viridianCenter = new Pokecenter("Viridian City's Pokecenter");
		Area viridianCity = new Area("Viridian city", null, true, pewterCity, viridianCenter);
		Shop ViridianCityShop = new Shop("Viridian City Shop");
		viridianCity.setContainsPokemon(
				Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

		// PALLET Town
		Pokecenter palletCenter = new Pokecenter("Pallet Town's Pokecenter");
		Area palletTown = new Area("Pallet town", null, true, viridianCity, palletCenter);
		Shop PalletTownShop = new Shop("PalletTownShop");
		palletTown.setContainsPokemon(
				Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

		//CERULEAN City
		Pokecenter ceruleanCenter = new Pokecenter("Cerulean City's Pokecenter");
		Area ceruleanCity = new Area("Cerulean city", null, true, pewterCity, ceruleanCenter);
		Shop ceruleanCityShop = new Shop("Cerulean City Shop");
		
		areas.add(palletTown);
		areas.add(viridianCity);
		areas.add(pewterCity);
		areas.add(ceruleanCity);
		
		// SETUP gym leaders
		GymLeader pewterLeader = new GymLeader("Bram", new Badge("Boulder Badge"), pewterCity);
		Pokemon p = new Pokemon(PokemonData.ONIX);
		p.setLevel(5);
		p.setOwner(pewterLeader);
		pewterLeader.setCurrentArea(palletTown);
		pewterLeader.setActivePokemon(p);
		pewterLeader.getPokemonCollection().add(p);
		pewterCity.setGymLeader(pewterLeader);

	}

	public static void main(String[] args) {
		
		System.out.println("----------------");
		System.out.println("Welcome new trainer, what's your name?");
		String name = sc.nextLine();
		trainer = new Trainer(name, areas.get(0));
		System.out.println("Hi, " + trainer.getName());
		Pokemon firstPokemon = chooseFirstPokemon();
		firstPokemon.setOwner(trainer);
		trainer.getPokemonCollection().add(firstPokemon);
		System.out.println("You now have " + trainer.getPokemonCollection().size() + " pokemon in your collection!");
		System.out.println("----------------");

		while (true) {
			showGameOptions();
		}
	}

	public String getName() {
		return trainer.getName();
	}

	private static void showGameOptions() {
		System.out.println("----------------");
		System.out.println("What do you want to do?");
		System.out.println("1 ) Find Pokemon");
		System.out.println("2 ) My Pokemon");
		System.out.println("3 ) Inventory");
		System.out.println("4 ) Badges");
		System.out.println("5 ) Challenge " + trainer.getCurrentArea().getName() + "'s Gym Leader");
		System.out.println("6 ) Travel");
		System.out.println("7 ) Visit Pokecenter");
		System.out.println("8) Visit " + trainer.getCurrentArea().getName());
		System.out.println("9 ) Exit game");
		System.out.println("----------------");
		int action = sc.nextInt();
		switch (action) {
		case 1:
			findAndBattlePokemon();
			break;
		case 2:
			trainer.showPokemonColletion();
			break;
		case 3:
			ItemType item = showInventory();
			if (item != null) {
				trainer.useItem(item, null);
			}
			break;
		case 4:
			trainer.showBadges();
			break;
		case 5:
			if (trainer.getCurrentArea().getGymLeader() != null) {
				startGymBattle();
			} else {
				System.out.println("----------------");
				System.out.println("No Gym Leader in this town!");
			}
			break;
		case 6:
			Area area = showTravel();
			if (area != null) {
				trainer.travel(area);
			}
			break;
		case 7:
			trainer.visitPokeCenter(trainer.getCurrentArea().getPokecenter());

			break;
		case 8:
			
			
		case 9:
			quit();
			break;

		default:
			System.out.println("----------------");
			System.out.println("Sorry, that's not a valid option");
			break;
		}
	}

	// TODO: US-PKM-O-6
	private static void findAndBattlePokemon() {
		Pokemon randomPokemon = trainer.findPokemon();
		Battle battle = trainer.battle(trainer.getActivePokemon(), randomPokemon);
		battle.start();
	}

	private static Area showTravel() {
		Area travelTo = null;
		int index = 1;
		List<Area> travelToAreas = new ArrayList<>();

		for (Area area : areas) {
			if (!area.equals(trainer.getCurrentArea()) && area.isUnlocked()
					&& ((area.getNextArea() != null && area.getNextArea().equals(trainer.getCurrentArea()))
							|| trainer.getCurrentArea().getNextArea() != null
									&& trainer.getCurrentArea().getNextArea().equals(area))) {
				travelToAreas.add(area);
			}
		}
		for (Area a : travelToAreas) {
			System.out.println(index + ") " + a.getName());
			index++;
		}
		System.out.println(index + ") Back");
		int choice = sc.nextInt();
		if (choice != index) {
			travelTo = travelToAreas.get(choice - 1);
		}
		return travelTo;
	}

	private static ItemType showInventory() {
		HashMap<ItemType, Integer> items = trainer.getInventory().getItems();
		Set<Map.Entry<ItemType, Integer>> entries = items.entrySet();
		int index = 1;
		for (Map.Entry<ItemType, Integer> entry : entries) {
			System.out.println(index + ") " + entry.getKey() + " Je hebt er: " + entry.getValue());
			index++;
		}
		System.out.println(index + ") Back");
		int choice = sc.nextInt();
		if (choice != index) {
			return ItemType.values()[choice - 1];
		}
		return null;
	}

	// TODO: US-PKM-O-1
	private static Pokemon chooseFirstPokemon() {
		System.out.println("----------------");
		System.out.println("Please choose one of these three pokemon");
		System.out.println("1 ) Charmander");
		System.out.println("2 ) Bulbasaur");
		System.out.println("3 ) Squirtle");

		int choise = sc.nextInt();
		switch (choise) {
		case 1:
			Pokemon chosenPokemonC = new Pokemon(PokemonData.CHARMANDER);
			chosenPokemonC.setLevel(5);
			trainer.setActivePokemon(chosenPokemonC);
			return chosenPokemonC;

		case 2:
			Pokemon chosenPokemonB = new Pokemon(PokemonData.BULBASAUR);
			chosenPokemonB.setLevel(5);
			trainer.setActivePokemon(chosenPokemonB);
			return chosenPokemonB;

		case 3:
			Pokemon chosenPokemonS = new Pokemon(PokemonData.SQUIRTLE);
			chosenPokemonS.setLevel(5);
			trainer.setActivePokemon(chosenPokemonS);
			return chosenPokemonS;

		default:
			chooseFirstPokemon();
		}
		return null;
	}

	// TODO: US-PKM-O-8
	public static int i = 2;

	private static void startGymBattle() {
		if (i > 1) {
			Battle trainerBattle = trainer.challengeTrainer(Game.trainer.getCurrentArea().getGymLeader());
			Pokemon enemyPokemon = trainerBattle.getEnemy();
			GymLeader gymLeader = (GymLeader) enemyPokemon.getOwner();

			if (trainerBattle != null && trainerBattle.getWinner().getOwner().equals(trainer)) {
				if (trainerBattle.getEnemy().getOwner().getClass().equals(GymLeader.class)) {
					gymLeader.setDefeated(true);
					i--;
					awardBadge(gymLeader.getBadge().getName());
					System.out.println("----------------");
					Area gymLeaderArea = gymLeader.getCurrentArea();
					Area nextArea = gymLeaderArea.getNextArea();
					if (nextArea != null) {
						nextArea.setUnlocked(true);
					}
				}
			}
		} else {
			System.out.println("----------------");
			System.out.println("You already defeated this gymleader!");
			System.out.println("----------------");
		}
	}

	// TODO: US-PKM-O-9
	public static void awardBadge(String badgeName) {
		Badge newBadge = new Badge(badgeName);
		trainer.addBadge(newBadge);
		System.out.println("You now own the " + badgeName);
	}

	public static void gameOver(String message) {
		System.out.println(message);
		System.out.println("----------------");
		System.out.println("Game over");
		quit();
	}

	// TODO: US-PKM-O-2:
	private static void quit() {
		System.out.println("----------------");
		System.out.println("Goodbye");
		System.exit(0);
	}
}
