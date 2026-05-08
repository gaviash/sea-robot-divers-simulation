public class TestSimulation {
    private static final int NB_AGENTS_DEFAUT = 4;
    private static final int NB_TOURS_DEFAUT = 5;
    private static final int DIMENSION_DEFAUT = 10;

    public static void main(String[] args) {
        int nbAgents = NB_AGENTS_DEFAUT;
        int nbTours = NB_TOURS_DEFAUT;
        int dimension = DIMENSION_DEFAUT;

        if (args.length > 0) {
            nbAgents = lireEntier(args[0], "nombre d'agents");
        }
        if (args.length > 1) {
            nbTours = lireEntier(args[1], "nombre de tours");
        }
        if (args.length > 2) {
            dimension = lireEntier(args[2], "dimension du terrain");
        }
        if (args.length > 3) {
            afficherUtilisation();
            return;
        }

        verifierParametres(nbAgents, nbTours, dimension);
        resetSimulation();

        Simulation.DIMENSIONS = dimension;
        Simulation.createSimulation(nbAgents);
        Simulation.simuler(nbTours);
    }

    private static int lireEntier(String texte, String nom) {
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parametre invalide pour " + nom + " : " + texte);
        }
    }

    private static void verifierParametres(int nbAgents, int nbTours, int dimension) {
        if (nbAgents < 0) {
            throw new IllegalArgumentException("Le nombre d'agents doit etre positif.");
        }
        if (nbTours < 0) {
            throw new IllegalArgumentException("Le nombre de tours doit etre positif.");
        }
        if (dimension <= 0 || dimension > Terrain.NB_LIGNES_MAX) {
            throw new IllegalArgumentException("La dimension doit etre entre 1 et " + Terrain.NB_LIGNES_MAX + ".");
        }
    }

    private static void resetSimulation() {
        Simulation.singleton = false;
        Simulation.terrain = null;
        Simulation.ressources = null;
        Simulation.Agents = null;
        Simulation.nombre_agents = 0;
        Simulation.nombres_ressources = 0;
    }

    private static void afficherUtilisation() {
        System.out.println("Utilisation : java TestSimulation [nbAgents] [nbTours] [dimension]");
        System.out.println("Exemple    : java TestSimulation 6 10 12");
    }
}
