/**
 * Explorateur humain limite par une reserve d'oxygene.
 * Le plongeur adapte son comportement selon son niveau d'oxygene.
 *
 * @author Gavriel Myara
 */
public class Plongeur extends Explorateur implements Rechargeable {
    private static final int MAX_OXYGENE = 100;
    private static final int SEUIL_HAUT_OXYGENE = 80;
    private static final int SEUIL_BAS_OXYGENE = 15;
    private static final int USE_PER_TURN = 6;
    private static final int DEPLACEMENT = 5;
    private static final int OXYGENE_DEPART = 40;
    private static int compteur_score = 0;
    private int oxygene;

    /**
     * Construit un plongeur a une position precise.
     *
     * @param lig ligne de depart
     * @param col colonne de depart
     * @param terrain terrain associe
     */
    public Plongeur(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.oxygene = OXYGENE_DEPART;
    }

    /**
     * Construit un plongeur a une position aleatoire.
     *
     * @param terrain terrain associe
     */
    public Plongeur(Terrain terrain){
        super(terrain);
        this.oxygene = OXYGENE_DEPART;
    }

    /**
     * Construit une copie d'un plongeur.
     * La copie garde la position, le terrain, le score et l'oxygene, mais recoit un nouvel identifiant.
     *
     * @param autre plongeur a copier
     */
    public Plongeur(Plongeur autre) {
        super(autre);
        this.oxygene = autre.oxygene;
    }

    /**
     * Renvoie une description detaillee du plongeur.
     *
     * @return representation textuelle du plongeur
     */
    @Override
    public String toString(){
        return "[Plongeur No " + this.id + " Position : col-"+this.col + "lig-" + this.lig +" - Oxygene " + this.oxygene + " - Score : " + this.score + "]";
    }

    /**
     * Ramasse une ressource.
     * L'oxygene recharge le plongeur, les tresors augmentent son score.
     *
     * @param r ressource ramassee
     */
    @Override
    public void ramasserRessource(Ressource r) {
        if (r instanceof Oxygene) {
            recharger(r.getQuantite());
            Simulation.enleverRessource(r);
        } else {
            super.ramasserRessource(r);
            compteur_score += r.getQuantite();
            System.out.println(this.toString() + "a ramasse " + r.getQuantite() + " d'or.");
        }

    }

    /**
     * Execute le comportement du plongeur pendant un tour.
     */
    @Override
    public void agir() {
        ramasseMemecase();
        Ressource[] tab_dist = this.tab_dist();
        if (this.oxygene <= SEUIL_BAS_OXYGENE){
            low_oxygen_behavior(tab_dist);
        } else if (this.oxygene >= SEUIL_HAUT_OXYGENE){
            high_oxygen_behavior(tab_dist);
        } else {
            normal_behavior(tab_dist);
        }
        this.oxygene -= USE_PER_TURN;

    }

    /**
     * Recharge la reserve d'oxygene du plongeur sans depasser le maximum.
     *
     * @param quantite quantite d'oxygene ajoutee
     */
    @Override
    public void recharger(int quantite) {
        if (this.oxygene + quantite < MAX_OXYGENE){
            this.oxygene += quantite;
        } else {
            this.oxygene = MAX_OXYGENE;
            System.out.println("Plongeur No " + this.id + " a trop d'oxygene et ne peut plus en ramasser");
        }
    }



    /**
     * Comportement applique quand le plongeur manque d'oxygene.
     *
     * @param tab_dist ressources triees par distance
     */
    protected void low_oxygen_behavior(Ressource[] tab_dist){
        System.out.println(this.toString() + " est en basse oxygene - comportement de panique.");
        for (int i = 0;i < tab_dist.length;i++){
            if (tab_dist[i] instanceof Oxygene){
                if (this.seDeplacerVers(tab_dist[i].getLigne(), tab_dist[i].getColonne(), DEPLACEMENT)){
                    this.ramasserRessource(tab_dist[i]);
                }
                return;
            }
        }
    } 

    /**
     * Comportement applique quand le plongeur a beaucoup d'oxygene.
     *
     * @param tab_dist ressources triees par distance
     */
    protected void high_oxygen_behavior(Ressource[] tab_dist){
        System.out.println(this.toString() + " est en haute oxygene - comportement cupide.");
        for (int i = 0;i < tab_dist.length;i++){
            if (tab_dist[i] instanceof Tresor){
                if (this.seDeplacerVers(tab_dist[i].getLigne(), tab_dist[i].getColonne(), DEPLACEMENT)){
                    this.ramasserRessource(tab_dist[i]);
                }
                return;
            }
        }
    }


    /**
     * Comportement standard du plongeur.
     *
     * @param tab_dist ressources triees par distance
     */
    protected void normal_behavior(Ressource[] tab_dist){
        if (seDeplacerVers(tab_dist[0].getLigne(), tab_dist[0].getColonne(), DEPLACEMENT)){
            ramasserRessource(tab_dist[0]);
        }
    }


    /**
     * Ramasse la ressource presente sur la meme case que le plongeur, si elle existe.
     */
    protected void ramasseMemecase(){
        if (!terrain.caseEstVide(this.lig, this.col)){
            this.ramasserRessource(terrain.getCase(this.lig, this.col));
        }
    }

    /**
     * Renvoie le score total de l'equipe des plongeurs.
     *
     * @return score collectif des plongeurs
     */
    public static int getCompteurScore(){
        return compteur_score;
    }
}
