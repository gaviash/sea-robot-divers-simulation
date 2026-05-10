public class Robot extends Explorateur implements Rechargeable {
    public final static int DEPLACEMENT = 60;
    private static final int MAX_BATTERIE = 40;
    private static final int BATTERIE_DEPART = 40;
    private static final int USE_PER_TURN = 5;
    private static final int RECHARGE_PER_TURN = 5;
    private static int compteur_score = 0;

    private int batterie;
    private boolean enRecharge;

    public Robot(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.batterie = BATTERIE_DEPART;
        this.enRecharge = false;
    }

    public Robot(Terrain terrain) {
        super(terrain);
        this.batterie = BATTERIE_DEPART;
        this.enRecharge = false;
    }

    public Robot(Robot autre) {
        super(autre);
        this.batterie = autre.batterie;
        this.enRecharge = autre.enRecharge;
    }

    @Override
    public String toString() {
        return "[Robot No " + this.id + " Position : col-" + this.col + "lig-" + this.lig + " - Batterie " + this.batterie
                + " - En recharge : " + this.enRecharge + " - Score : " + this.score + "]";
    }

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

    @Override
    public void recharger(int quantite) {
        this.batterie += quantite;
        if (this.batterie >= MAX_BATTERIE){
            this.batterie = MAX_BATTERIE;
            this.enRecharge = false;
        }
    }

    @Override
    public void ramasserRessource(Ressource r){
        super.ramasserRessource(r);
        compteur_score += r.getQuantite();
        System.out.println(this.toString() + "a ramasse " + r.getQuantite() + " d'or.");
    }

    protected void ramasseMemecaseTresor() {
        Ressource r = this.terrain.getCase(this.lig, this.col);
        if (r instanceof Tresor) {
            this.ramasserRessource(r);
        }
        // difference avec ramasser Ressource : verifie qu'il y a une ressource sur
        // cette case avant de recuperer
    }

    protected void robot_behavior(){
        Ressource[] tab_dist = this.tab_dist();
        for (int i = 0;i < tab_dist.length;i++){
            if (tab_dist[i] instanceof Tresor && this.seDeplacerVers(tab_dist[i].getLigne(), tab_dist[i].getColonne(), DEPLACEMENT)){
                this.ramasserRessource(tab_dist[i]);
                return;
            }
        }
    }

    public static int getCompteurScore(){
        return compteur_score;
    }
}
