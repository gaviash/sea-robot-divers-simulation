public abstract class RessourceEvolutive extends Ressource {
    protected int time_count; //Au bout de combien de tour la ressource subit un changement
    protected final int reference_time_count;
    
    public RessourceEvolutive(String type,int quantite,int time_count){
        super(type,quantite);
        this.time_count = time_count;
        this.reference_time_count = time_count;
    }

    protected abstract void evoluer();

    public void evoluer_temps(){
        if (this.time_count == 0){
            evoluer();
            this.time_count = this.reference_time_count;
        } else {
            this.time_count--;
        }
    }
}
