import java.util.ArrayList;

public class Simulation {
    public static ArrayList<Ressource> ressources;
    public static boolean singleton = false;
    public static Terrain terrain;
    public static int DIMENSIONS = 10;
    public final int nombre_agents;
    public final int nombres_ressources;

    private Simulation(int nombre_agents){
        terrain = new Terrain(DIMENSIONS, DIMENSIONS);
        ressources = terrain.lesRessources();
        this.nombre_agents = nombre_agents;
        this.nombres_ressources = (int)(this.nombre_agents * 1.5);

        
    }

    public static Simulation createSimulation(int nombre_agents){
        if (!singleton){
            singleton = true;
            return new Simulation(nombre_agents);
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
