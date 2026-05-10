/**
 * Exception controlee representant une erreur propre a la logique de simulation.
 *
 * @author Gavriel Myara
 */
public class SimulationException extends Exception {
    /**
     * Construit une exception de simulation avec un message.
     *
     * @param message description de l'erreur
     */
    public SimulationException(String message) {
        super(message);
    }

    /**
     * Construit une exception de simulation avec un message et une cause.
     *
     * @param message description de l'erreur
     * @param cause exception d'origine
     */
    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Cree une exception pour un placement de ressource impossible.
     *
     * @param ressource ressource qui n'a pas pu etre placee
     * @param terrain terrain concerne
     * @param tentatives nombre de tentatives effectuees
     * @return exception de simulation correspondante
     */
    public static SimulationException placementImpossible(Ressource ressource, Terrain terrain, int tentatives) {
        return new SimulationException(
            "Impossible de placer la ressource " + ressource
            + " sur le terrain " + terrain.nbLignes + "x" + terrain.nbColonnes
            + " apres " + tentatives + " tentatives."
        );
    }

    /**
     * Cree une exception pour une simulation non initialisee.
     *
     * @return exception de simulation correspondante
     */
    public static SimulationException simulationNonInitialisee() {
        return new SimulationException("aucune simulation n'a ete creee.");
    }

    /**
     * Cree une exception pour un nombre d'agents invalide.
     *
     * @param nombreAgents nombre d'agents fourni
     * @return exception de simulation correspondante
     */
    public static SimulationException nombreAgentsInvalide(int nombreAgents) {
        return new SimulationException("le nombre d'agents est invalide : " + nombreAgents + ".");
    }

    /**
     * Cree une exception pour un nombre de tours invalide.
     *
     * @param nombreTours nombre de tours fourni
     * @return exception de simulation correspondante
     */
    public static SimulationException nombreToursInvalide(int nombreTours) {
        return new SimulationException("le nombre de tours est invalide : " + nombreTours + ".");
    }

    /**
     * Cree une exception pour un indice de ressource invalide.
     *
     * @param indice indice fourni
     * @return exception de simulation correspondante
     */
    public static SimulationException indiceRessourceInvalide(int indice) {
        return new SimulationException("indice de ressource invalide : " + indice + ".");
    }

    /**
     * Cree une exception pour une ressource absente de la simulation.
     *
     * @param ressource ressource recherchee
     * @return exception de simulation correspondante
     */
    public static SimulationException ressourceAbsente(Ressource ressource) {
        return new SimulationException("la ressource " + ressource + " n'est pas presente dans la simulation.");
    }
}
