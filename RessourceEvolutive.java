/**
 * Ressource dont la quantite peut changer apres un certain nombre de tours.
 *
 * @author Gavriel Myara
 */
public abstract class RessourceEvolutive extends Ressource {
    /** Compteur restant avant la prochaine evolution. */
    protected int time_count; //Au bout de combien de tour la ressource subit un changement
    /** Valeur de reference utilisee pour reinitialiser le compteur. */
    protected final int reference_time_count;
    
    /**
     * Construit une ressource evolutive.
     *
     * @param type type de la ressource
     * @param quantite quantite initiale
     * @param time_count nombre de tours avant evolution
     */
    public RessourceEvolutive(String type,int quantite,int time_count){
        super(type,quantite);
        this.time_count = time_count;
        this.reference_time_count = time_count;
    }

    /**
     * Applique l'evolution concrete de la ressource.
     */
    protected abstract void evoluer();

    /**
     * Fait avancer le compteur de temps et declenche l'evolution si necessaire.
     */
    public void evoluer_temps(){
        if (this.time_count == 0){
            evoluer();
            this.time_count = this.reference_time_count;
        } else {
            this.time_count--;
        }
    }
}
