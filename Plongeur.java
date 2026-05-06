public class Plongeur extends Explorateur implements Rechargeable {
    private static final int MAX_OXYGENE = 100;
    private static final int SEUIL_HAUT_OXYGENE = 80;
    private static final int SEUIL_BAS_OXYGENE = 15;
    private static final int USE_PER_TURN = 5;
    private static final int DEPLACEMENT = 30;
     
    private int oxygene;

    public Plongeur(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.oxygene = 10;
    }

    @Override
    public String toString(){
        return "[Plongeur N°" + this.id + "Position : col-"+this.col + "lig-" + this.lig +" - Oxygene " + this.oxygene + " - Score : " + this.score + "]";
    }

    @Override
    public void ramasserRessource(Ressource r) {
        if (r instanceof Oxygene) {
            recharger(r.getQuantite());
        } else {
            super.ramasserRessource(r);
            System.out.println(this.toString() + "a ramassé " + r.getQuantite() + " d'or.");
        }

    }

    @Override
    public void agir() {
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
            Simulation.enleverRessource(id);
        } else {
            System.out.println("Plongeur N°" + this.id + " a trop d'oxygene et ne peut plus en ramasser");
        }
    }



    protected void low_oxygen_behavior(Ressource[] tab_dist){
        System.out.println(this.toString() + " est en basse oxygene - comportement de panique.");

    } 

    protected void high_oxygen_behavior(Ressource[] tab_dist){

    }

    protected void normal_behavior(Ressource[] tab_dist){

    }
}
