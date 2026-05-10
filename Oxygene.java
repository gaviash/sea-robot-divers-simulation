/**
 * Ressource evolutive permettant aux plongeurs de recharger leur oxygene.
 *
 * @author Gavriel Myara
 */
public class Oxygene extends RessourceEvolutive {
    /**
     * Construit une ressource d'oxygene.
     *
     * @param quantite quantite initiale d'oxygene
     * @param time_count nombre de tours avant evolution
     */
    public Oxygene(int quantite,int time_count){
        super("Oxygene",quantite,time_count);
    }

    /**
     * Fait evoluer la quantite d'oxygene.
     * La ressource peut augmenter par chance, diminuer, ou disparaitre si elle est vide.
     */
    protected void evoluer(){
        if (Math.random() < 0.2){
            this.setQuantite(this.getQuantite()+10); //10 ets le gain d'oxygene sur un coup de chance
        } else {
            if (this.getQuantite() <= 0){
                Simulation.enleverRessource(this);
            } else{
                this.setQuantite(this.getQuantite()-3);
            }
        }
    }

}
