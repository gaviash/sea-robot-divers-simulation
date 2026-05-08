public class Plongeur extends Explorateur implements Rechargeable {
    private static final int MAX_OXYGENE = 100;
    private static final int SEUIL_HAUT_OXYGENE = 80;
    private static final int SEUIL_BAS_OXYGENE = 15;
    private static final int USE_PER_TURN = 5;
    private static final int DEPLACEMENT = 30;
    private static final int OXYGENE_DEPART = 40;
    private static int compteur_score = 0;
    private int oxygene;

    public Plongeur(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.oxygene = OXYGENE_DEPART;
    }

    public Plongeur(Terrain terrain){
        super(terrain);
        this.oxygene = OXYGENE_DEPART;
    }

    @Override
    public String toString(){
        return "[Plongeur N°" + this.id + "Position : col-"+this.col + "lig-" + this.lig +" - Oxygene " + this.oxygene + " - Score : " + this.score + "]";
    }

    @Override
    public void ramasserRessource(Ressource r) {
        if (r instanceof Oxygene) {
            recharger(r.getQuantite());
            Simulation.enleverRessource(r);
        } else {
            super.ramasserRessource(r);
            System.out.println(this.toString() + "a ramassé " + r.getQuantite() + " d'or.");
        }

    }

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

    @Override
    public void recharger(int quantite) {
        if (this.oxygene + quantite < MAX_OXYGENE){
            this.oxygene += quantite;
        } else {
            this.oxygene = MAX_OXYGENE;
            System.out.println("Plongeur N°" + this.id + " a trop d'oxygene et ne peut plus en ramasser");
        }
    }



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


    protected void normal_behavior(Ressource[] tab_dist){
        if (seDeplacerVers(tab_dist[0].getLigne(), tab_dist[0].getColonne(), DEPLACEMENT)){
            ramasserRessource(tab_dist[0]);
        }
    }


    protected void ramasseMemecase(){
        if (!terrain.caseEstVide(this.lig, this.col)){
            this.ramasserRessource(terrain.getCase(this.lig, this.col));
        }
    }
}
