/**
 * Classe abstraite de base pour tous les agents de la simulation.
 * Un agent possede une position, un identifiant et un terrain sur lequel agir.
 *
 * @author Gavriel Myara
 */
public abstract class Agent {
    /** Ligne actuelle de l'agent sur le terrain. */
    protected int lig;
    /** Colonne actuelle de l'agent sur le terrain. */
    protected int col;
    /** Identifiant unique de l'agent. */
    protected int id;
    /** Terrain sur lequel l'agent evolue. */
    protected Terrain terrain;
    private static int compteur = 0;

    /**
     * Construit un agent a une position precise.
     *
     * @param lig ligne de depart
     * @param col colonne de depart
     * @param terrain terrain associe a l'agent
     */
    public Agent(int lig,int col,Terrain terrain){
        this.lig = lig;
        this.col = col;
        this.terrain = terrain;
        this.id = compteur;
        compteur++;
    }

    /**
     * Construit un agent a une position aleatoire sur le terrain.
     *
     * @param terrain terrain associe a l'agent
     */
    public Agent(Terrain terrain){
        this((int)(Math.random()* terrain.nbLignes)+1, (int)(Math.random()*terrain.nbColonnes)+1,terrain);
    }

    /**
     * Calcule la distance de Manhattan entre l'agent et une case cible.
     *
     * @param lig ligne cible
     * @param col colonne cible
     * @return distance entre l'agent et la case cible
     */
    public int distance(int lig, int col){
        return (Math.abs(this.col-col) + Math.abs(this.lig-lig));
    }

    /**
     * Compare deux ressources selon leur distance a l'agent.
     *
     * @param r1 premiere ressource
     * @param r2 seconde ressource
     * @return difference de distance entre les deux ressources
     */
    public int compare_distance(Ressource r1,Ressource r2){
        return this.distance(r1.getLigne(),r1.getColonne()) - this.distance(r2.getLigne(), r2.getColonne());
    }

    /**
     * Deplace l'agent vers une case valide du terrain.
     *
     * @param lig nouvelle ligne
     * @param col nouvelle colonne
     */
    public void seDeplacer(int lig,int col){
        if (this.terrain.sontValides(lig, col)){
            this.lig = lig;
            this.col = col;
        }
        //Rajouter un print au deplacement chez les enfants
    }

    /**
     * Deplace l'agent vers une cible, completement ou partiellement selon la distance maximale.
     *
     * @param cibleLig ligne cible
     * @param cibleCol colonne cible
     * @param distanceMax distance maximale parcourue pendant ce deplacement
     * @return true si la cible est atteinte, false sinon
     */
    protected boolean seDeplacerVers(int cibleLig, int cibleCol, int distanceMax){
        int ecartLig = cibleLig - this.lig;
        int ecartCol = cibleCol - this.col;
        int distance = Math.abs(ecartLig) + Math.abs(ecartCol);

        if (distance <= distanceMax){
            this.seDeplacer(cibleLig, cibleCol);
            return true;
        }

        int reste = distanceMax;
        int nouvelleLig = this.lig;
        int nouvelleCol = this.col;

        int pasLig = Math.min(Math.abs(ecartLig), reste) * Integer.signum(ecartLig);
        nouvelleLig += pasLig;
        reste -= Math.abs(pasLig);

        int pasCol = Math.min(Math.abs(ecartCol), reste) * Integer.signum(ecartCol);
        nouvelleCol += pasCol;

        this.seDeplacer(nouvelleLig, nouvelleCol);
        return false;
    }

    /**
     * Renvoie une description textuelle de l'agent.
     *
     * @return representation textuelle de l'agent
     */
    public String toString(){
        return "Agent N'" + this.id + " est a la position (" + this.lig + "," + this.col +")"; 
    }

    /**
     * Execute le comportement de l'agent pendant un tour.
     */
    public abstract void agir();

    /**
     * Renvoie les ressources de la simulation triees par distance a l'agent.
     *
     * @return tableau des ressources triees par distance croissante
     */
    protected Ressource[] tab_dist(){
        Simulation.ressources.sort(this::compare_distance);
        Ressource[] tab_dist = Simulation.ressources.toArray(new Ressource[0]);
        return tab_dist;
    }
    
}
