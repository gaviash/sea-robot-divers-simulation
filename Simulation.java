import java.util.ArrayList;

public class Simulation {
    public static ArrayList<Ressource> ressources;
    public static boolean singleton = false;
    public static Terrain terrain;

    private Simulation(Terrain t){
        this.ressources = t.lesRessources();
        this.terrain = t;
    }

    public static Simulation createSimulation(Terrain t){
        if (!singleton){
            singleton = true;
            return new Simulation(t);
        } else {
            return null;
        }
    }

    public static void turn_refresh(){
        ressources = terrain.lesRessources();
    }

    public static void enleverRessource(int i){
        terrain.viderCase(ressources.get(i).getLigne(), ressources.get(i).getColonne());
        ressources.remove(i);
    }

    public static void enleverRessource(Ressource r){
        terrain.viderCase(r.getLigne(), r.getColonne());
        ressources.remove(ressources.indexOf(r));
    }
}
