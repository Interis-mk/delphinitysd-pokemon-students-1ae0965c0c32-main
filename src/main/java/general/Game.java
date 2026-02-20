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
import jakarta.persistence.*;

public class Game {

    private static final ArrayList<Area> areas = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static Trainer trainer = null;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemonPU");

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
        Area viridianCity = new Area("Viridian city", null, true, null, viridianCenter);
        Shop ViridianCityShop = new Shop("Viridian City Shop");
        viridianCity.setContainsPokemon(
                Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

        // PALLET Town
        Pokecenter palletCenter = new Pokecenter("Pallet Town's Pokecenter");
        Area palletTown = new Area("Pallet town", null, true, null, palletCenter);
        Shop PalletTownShop = new Shop("PalletTownShop");
        palletTown.setContainsPokemon(
                Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

        // CERULEAN City
        Pokecenter ceruleanCenter = new Pokecenter("Cerulean City's Pokecenter");
        Area ceruleanCity = new Area("Cerulean city", null, true, null, ceruleanCenter);
        Shop ceruleanCityShop = new Shop("Cerulean City Shop");

        areas.add(palletTown);
        areas.add(viridianCity);
        areas.add(pewterCity);
        areas.add(ceruleanCity);

        saveAllAreasWithNextArea(areas);

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

    private static void saveGame() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(trainer);
            tx.commit();
            System.out.println("Game saved successfully.");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static Trainer loadGame(String trainerName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t " +
                "LEFT JOIN FETCH t.pokemonCollection " +
                "LEFT JOIN FETCH t.currentArea ca " +
                "LEFT JOIN FETCH ca.nextArea " +
                "WHERE t.name = :name", Trainer.class);
            query.setParameter("name", trainerName);
            Trainer loadedTrainer = query.getResultStream().findFirst().orElse(null);
            if (loadedTrainer != null) {
                // Initialize badges collection while session is open
                loadedTrainer.getBadges().size();
                System.out.println("Game loaded for trainer: " + trainerName);
            }
            return loadedTrainer;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {

        System.out.println("----------------");
        System.out.println("Welcome new trainer, what's your name?");
        String name = sc.nextLine();
        System.out.println("Do you want to load a saved game? (yes/no)");
        String load = sc.nextLine();
        if (load.equalsIgnoreCase("yes")) {
            trainer = loadGame(name);
            if (trainer == null) {
                System.out.println("No saved game found. Starting new game.");
                trainer = new Trainer(name, areas.get(0));
            }
        } else {
            trainer = new Trainer(name, areas.get(0));
        }
        System.out.println("Hi, " + trainer.getName());
        if (trainer.getPokemonCollection().isEmpty()) {
            Pokemon firstPokemon = chooseFirstPokemon();
            firstPokemon.setOwner(trainer);
            trainer.getPokemonCollection().add(firstPokemon);
            System.out.println("You now have " + trainer.getPokemonCollection().size() + " pokemon in your collection!");
        }
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


    private static void quit() {
        saveGame();
        System.out.println("----------------");
        System.out.println("Goodbye");
        System.exit(0);
    }

    private static void saveAllAreas(List<Area> areas) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Area area : areas) {
                em.merge(area);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void saveAllAreasWithNextArea(List<Area> areas) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Area area : areas) {
                area.setNextArea(null);
                em.persist(area);
            }
            em.flush();
            for (Area area : areas) {
                em.refresh(area);
            }
            Area palletTown = areas.get(0);
            Area viridianCity = areas.get(1);
            Area pewterCity = areas.get(2);
            Area ceruleanCity = areas.get(3);
            palletTown.setNextArea(viridianCity);
            viridianCity.setNextArea(pewterCity);
            pewterCity.setNextArea(null);
            ceruleanCity.setNextArea(pewterCity);
            for (Area area : areas) {
                em.merge(area);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
