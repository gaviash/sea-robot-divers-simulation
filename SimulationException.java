public class SimulationException extends Exception {
    public SimulationException(String message) {
        super(message);
    }

    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SimulationException placementImpossible(Ressource ressource, Terrain terrain, int tentatives) {
        return new SimulationException(
            "Impossible de placer la ressource " + ressource
            + " sur le terrain " + terrain.nbLignes + "x" + terrain.nbColonnes
            + " apres " + tentatives + " tentatives."
        );
    }

    public static SimulationException simulationNonInitialisee() {
        return new SimulationException("aucune simulation n'a ete creee.");
    }

    public static SimulationException nombreAgentsInvalide(int nombreAgents) {
        return new SimulationException("le nombre d'agents est invalide : " + nombreAgents + ".");
    }

    public static SimulationException nombreToursInvalide(int nombreTours) {
        return new SimulationException("le nombre de tours est invalide : " + nombreTours + ".");
    }

    public static SimulationException indiceRessourceInvalide(int indice) {
        return new SimulationException("indice de ressource invalide : " + indice + ".");
    }

    public static SimulationException ressourceAbsente(Ressource ressource) {
        return new SimulationException("la ressource " + ressource + " n'est pas presente dans la simulation.");
    }
}
