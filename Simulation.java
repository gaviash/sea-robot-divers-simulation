import java.util.ArrayList;

public class Simulation {
    public static ArrayList<Ressource> ressources;
    public static ArrayList<Explorateur> Agents;
    public static boolean singleton = false;
    public static Terrain terrain;
    public static int DIMENSIONS = 10;
    public static int nombre_agents;
    public static int nombres_ressources;

    private Simulation(int nbr) {
        terrain = new Terrain(DIMENSIONS, DIMENSIONS);
        Agents = new ArrayList<>();
        nombre_agents = nbr;
        nombres_ressources = (int) (nombre_agents * 1.5);

        for (int i = 0; i < nombres_ressources / 4; i++) {
            placerRessourceAleatoire(terrain, new Oxygene(15, 2));
        }
        for (int i = 0; i < nombres_ressources * (3 / 4); i++) {
            placerRessourceAleatoire(terrain, new Tresor(1));
        }
        ressources = terrain.lesRessources();

        for (int i = 0; i < nombre_agents / 2; i++) {
            Agents.add(new Robot(terrain));
            Agents.add(new Plongeur(terrain));
        }
    }

    public static Simulation createSimulation(int nombre_agents) {
        if (!singleton) {
            singleton = true;
            return new Simulation(nombre_agents);
        } else {
            return null;
        }
    }

    public static void SimulationTurn() {
        repeuplerRessources();
        turn_refresh();

        for (int i = 0; i < Agents.size(); i++) {
            Agents.get(i).agir(); // les agents ont été placé en alternant dont il n'y a pas de favoritisme !
        }

        evoluerRessources();
    }

    public static void repeuplerRessources() {
        if (ressources.size() < nombres_ressources) {
            for (int i = 0; i < nombres_ressources - ressources.size(); i += 4) {
                placerRessourceAleatoire(terrain, new Tresor(1));
                placerRessourceAleatoire(terrain, new Tresor(1));
                placerRessourceAleatoire(terrain, new Tresor(1));
                placerRessourceAleatoire(terrain, new Oxygene(15, 2));
            }
        }
    }

    public static void evoluerRessources() {
        for (int i = 0; i < ressources.size(); i++) {
            Ressource ress = ressources.get(i);
            if (ress instanceof RessourceEvolutive) {
                ((RessourceEvolutive) ress).evoluer_temps();
            }
        }
    }

    public static void turn_refresh() {
        ressources = terrain.lesRessources();
    }

    public static void enleverRessource(int i) {
        terrain.viderCase(ressources.get(i).getLigne(), ressources.get(i).getColonne());
        ressources.remove(i);
    }

    public static void enleverRessource(Ressource r) {
        terrain.viderCase(r.getLigne(), r.getColonne());
        ressources.remove(ressources.indexOf(r));
    }

    public static void placerRessourceAleatoire(Terrain terrain, Ressource r) {
        boolean placee = false;

        while (!placee) {
            int lig = (int) (Math.random() * terrain.nbLignes) + 1;
            int col = (int) (Math.random() * terrain.nbColonnes) + 1;

            placee = terrain.setCase(lig, col, r);
        }
    }

}
