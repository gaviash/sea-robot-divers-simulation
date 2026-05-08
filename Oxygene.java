public class Oxygene extends RessourceEvolutive {
    public Oxygene(int quantite,int time_count){
        super("Oxygene",quantite,time_count);
    }

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
