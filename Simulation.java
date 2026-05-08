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
        for (int i = 0; i < nombres_ressources * 3 / 4; i++) {
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

    public static void simuler(int nbr){
        if (!singleton || terrain == null || ressources == null || Agents == null) {
            System.out.println("Impossible de simuler : aucune simulation n'a ete creee.");
            return;
        }

        System.out.println("=== Debut de la simulation ===");
        System.out.println("Nombre de tours : " + nbr);
        afficherEtatSimulation();

        for (int i = 0; i < nbr;i++){
            System.out.println();
            System.out.println("--- Tour " + (i + 1) + " / " + nbr + " ---");
            SimulationTurn();
            afficherEtatSimulation();
        }

        System.out.println();
        System.out.println("=== Fin de la simulation ===");
        afficherScores();
    }



    public static void SimulationTurn() {
        System.out.println("Repopulation des ressources...");
        repeuplerRessources();
        turn_refresh();
        System.out.println("Ressources disponibles : " + ressources.size() + " (" + resumeRessources() + ")");

        for (int i = 0; i < Agents.size(); i++) {
            System.out.println("Avant action : " + Agents.get(i));
            Agents.get(i).agir(); // les agents ont ete places en alternance, donc il n'y a pas de favoritisme.
            System.out.println("Apres action : " + Agents.get(i));
        }

        System.out.println("Evolution des ressources...");
        evoluerRessources();
        turn_refresh();
        System.out.println("Fin du tour : " + ressources.size() + " ressources restantes (" + resumeRessources() + ")");
        afficherScoresEquipes();
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
        ArrayList<Ressource> copie = new ArrayList<>(ressources);
        for (int i = 0; i < copie.size(); i++) {
            Ressource ress = copie.get(i);
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

    private static String resumeRessources() {
        int nbTresors = 0;
        int nbOxygene = 0;
        int nbAutres = 0;

        for (int i = 0; i < ressources.size(); i++) {
            Ressource r = ressources.get(i);
            if (r instanceof Tresor) {
                nbTresors++;
            } else if (r instanceof Oxygene) {
                nbOxygene++;
            } else {
                nbAutres++;
            }
        }

        return nbTresors + " tresors, " + nbOxygene + " oxygenes, " + nbAutres + " autres";
    }

    private static void afficherEtatSimulation() {
        turn_refresh();
        System.out.println("Terrain : " + terrain);
        System.out.println("Agents : " + Agents.size() + " (" + compterRobots() + " robots, " + compterPlongeurs() + " plongeurs)");
        System.out.println("Ressources : " + ressources.size() + " (" + resumeRessources() + ")");
        terrain.afficher(3);
    }

    private static void afficherScores() {
        System.out.println("Scores finaux par equipe :");
        afficherScoresEquipes();
    }

    private static void afficherScoresEquipes() {
        System.out.println("Score Robots : " + Robot.getCompteurScore() + " | Score Plongeurs : " + Plongeur.getCompteurScore());
    }

    private static int compterRobots() {
        int total = 0;
        for (int i = 0; i < Agents.size(); i++) {
            if (Agents.get(i) instanceof Robot) {
                total++;
            }
        }
        return total;
    }

    private static int compterPlongeurs() {
        int total = 0;
        for (int i = 0; i < Agents.size(); i++) {
            if (Agents.get(i) instanceof Plongeur) {
                total++;
            }
        }
        return total;
    }

}
