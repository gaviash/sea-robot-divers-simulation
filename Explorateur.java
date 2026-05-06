public abstract class Explorateur extends Agent {

    protected int score;

    public Explorateur(int lig, int col, Terrain terrain) {
        super(lig, col, terrain);
        this.score = 0;
    }

    public Explorateur(Terrain t) {
        super(t);
        this.score = 0; //La repetition de cette ligne est inevitable,ou alors on aurait du repeter Math.random
    }

    public int getScore() {
        return this.score;
    }

    public void ramasserRessource(Ressource r) {
        //Comportement pour eviter la duplication de code : TOUT les explorateurs peuvent ramasser des tresors,mais les sous
        //-classes peuvent ramasser aussi de l'oxygene par exemple.
        this.score += r.getQuantite();
        Simulation.enleverRessource(r);
    }

    @Override //Autant le mettre
    public abstract void agir(); //agir,c'est le comportement
}
