public abstract class Agent {
    protected int lig;
    protected int col;
    protected int id;
    protected Terrain terrain;
    private static int compteur = 0;


    public Agent(int lig,int col,Terrain terrain){
        this.lig = lig;
        this.col = col;
        this.terrain = terrain;
        this.id = compteur;
        compteur++;
    }

    public Agent(Terrain terrain){
        this((int)(Math.random()* terrain.nbLignes)+1, (int)(Math.random()*terrain.nbColonnes)+1,terrain);
    }

    
    public int distance(int lig, int col){
        return (Math.abs(this.col-col) + Math.abs(this.lig-lig));
    }

    public int compare_distance(Ressource r1,Ressource r2){
        return this.distance(r1.getLigne(),r1.getColonne()) - this.distance(r2.getLigne(), r2.getColonne());
    }

    public void seDeplacer(int lig,int col){
        if (this.terrain.sontValides(lig, col)){
            this.lig = lig;
            this.col = col;
        }
        //Rajouter un print au deplacement chez les enfants
    }

    public String toString(){
        return "Agent N'" + this.id + " est a la position (" + this.lig + "," + this.col +")"; 
    }

    public abstract void agir();

    protected Ressource[] tab_dist(){
        Simulation.ressources.sort(this::compare_distance);
        Ressource[] tab_dist = Simulation.ressources.toArray(new Ressource[0]);
        return tab_dist;
    }
    
}
