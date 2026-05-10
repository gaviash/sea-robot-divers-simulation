/**
 * Agent capable de ramasser des ressources et de cumuler un score.
 *
 * @author Gavriel Myara
 */
public abstract class Explorateur extends Agent {

    /** Score individuel de l'explorateur. */
    protected int score;

    /**
     * Construit un explorateur a une position precise.
     *
     * @param lig ligne de depart
     * @param col colonne de depart
     * @param terrain terrain associe
     */
    public Explorateur(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.score = 0;
    }

    /**
     * Construit un explorateur a une position aleatoire.
     *
     * @param t terrain associe
     */
    public Explorateur(Terrain t) {
        super(t);
        this.score = 0; //La repetition de cette ligne est inevitable,ou alors on aurait du repeter Math.random
    }

    /**
     * Construit une copie d'un explorateur.
     * La copie garde la position, le terrain et le score, mais recoit un nouvel identifiant.
     *
     * @param autre explorateur a copier
     */
    public Explorateur(Explorateur autre) {
        super(autre.lig, autre.col, autre.terrain);
        this.score = autre.score;
    }

    /**
     * Renvoie le score de l'explorateur.
     *
     * @return score actuel
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Ramasse une ressource et ajoute sa quantite au score.
     *
     * @param r ressource ramassee
     */
    public void ramasserRessource(Ressource r) {
        //Comportement pour eviter la duplication de code : TOUT les explorateurs peuvent ramasser des tresors,mais les sous
        //-classes peuvent ramasser aussi de l'oxygene par exemple.
        this.score += r.getQuantite();
        Simulation.enleverRessource(r);
    }

    /**
     * Execute le comportement propre a chaque type d'explorateur.
     */
    @Override //Autant le mettre
    public abstract void agir(); //agir,c'est le comportement
}
