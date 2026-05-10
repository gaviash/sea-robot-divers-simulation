/**
 * Explorateur mecanique limite par une batterie.
 * Le robot cherche uniquement les tresors et se recharge quand sa batterie est vide.
 *
 * @author Gavriel Myara
 */
public class Robot extends Explorateur implements Rechargeable {
    /** Distance maximale parcourue par le robot en un tour. */
    public final static int DEPLACEMENT = 60;
    private static final int MAX_BATTERIE = 40;
    private static final int BATTERIE_DEPART = 40;
    private static final int USE_PER_TURN = 5;
    private static final int RECHARGE_PER_TURN = 5;
    private static int compteur_score = 0;

    private int batterie;
    private boolean enRecharge;

    /**
     * Construit un robot a une position precise.
     *
     * @param lig ligne de depart
     * @param col colonne de depart
     * @param terrain terrain associe
     */
    public Robot(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.batterie = BATTERIE_DEPART;
        this.enRecharge = false;
    }

    /**
     * Construit un robot a une position aleatoire.
     *
     * @param terrain terrain associe
     */
    public Robot(Terrain terrain) {
        super(terrain);
        this.batterie = BATTERIE_DEPART;
        this.enRecharge = false;
    }

    /**
     * Construit une copie d'un robot.
     * La copie garde la position, le terrain, le score, la batterie et l'etat de recharge,
     * mais recoit un nouvel identifiant.
     *
     * @param autre robot a copier
     */
    public Robot(Robot autre) {
        super(autre);
        this.batterie = autre.batterie;
        this.enRecharge = autre.enRecharge;
    }

    /**
     * Renvoie une description detaillee du robot.
     *
     * @return representation textuelle du robot
     */
    @Override
    public String toString() {
        return "[Robot No " + this.id + " Position : col-" + this.col + "lig-" + this.lig + " - Batterie " + this.batterie
                + " - En recharge : " + this.enRecharge + " - Score : " + this.score + "]";
    }

    /**
     * Execute le comportement du robot pendant un tour.
     */
    @Override
    public void agir() {
        if (enRecharge) {
            recharger(RECHARGE_PER_TURN);
        } else if (batterie <= 0) {
            this.enRecharge = true;
        } else {
            ramasseMemecaseTresor();
            robot_behavior();
            this.batterie -= USE_PER_TURN;
            // Comportement basique aller vers Tresor
        }
    }

    /**
     * Recharge la batterie du robot sans depasser le maximum.
     *
     * @param quantite quantite de batterie ajoutee
     */
    @Override
    public void recharger(int quantite) {
        this.batterie += quantite;
        if (this.batterie >= MAX_BATTERIE){
            this.batterie = MAX_BATTERIE;
            this.enRecharge = false;
        }
    }

    /**
     * Ramasse un tresor et ajoute sa quantite au score de l'equipe des robots.
     *
     * @param r ressource ramassee
     */
    @Override
    public void ramasserRessource(Ressource r){
        super.ramasserRessource(r);
        compteur_score += r.getQuantite();
        System.out.println(this.toString() + "a ramasse " + r.getQuantite() + " d'or.");
    }

    /**
     * Ramasse uniquement un tresor present sur la meme case que le robot.
     */
    protected void ramasseMemecaseTresor() {
        Ressource r = this.terrain.getCase(this.lig, this.col);
        if (r instanceof Tresor) {
            this.ramasserRessource(r);
        }
        // difference avec ramasser Ressource : verifie qu'il y a une ressource sur
        // cette case avant de recuperer
    }

    /**
     * Cherche le tresor le plus proche que le robot peut atteindre.
     */
    protected void robot_behavior(){
        Ressource[] tab_dist = this.tab_dist();
        for (int i = 0;i < tab_dist.length;i++){
            if (tab_dist[i] instanceof Tresor && this.seDeplacerVers(tab_dist[i].getLigne(), tab_dist[i].getColonne(), DEPLACEMENT)){
                this.ramasserRessource(tab_dist[i]);
                return;
            }
        }
    }

    /**
     * Renvoie le score total de l'equipe des robots.
     *
     * @return score collectif des robots
     */
    public static int getCompteurScore(){
        return compteur_score;
    }
}
