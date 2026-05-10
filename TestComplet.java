public class TestComplet {
    private static int verifications = 0;

    public static void main(String[] args) {
        testTerrain();
        testSimulation();
        testSimulationExceptionPlacementTerrainPlein();
        testSimulationExceptionNombreAgentsInvalide();
        testSimulationExceptionRessourceAbsente();
        testSimulationEnleverRessourceParIndice();
        testSimulationSimulerUnTour();
        testDistanceEtTriDesRessources();
        testDeplacementVers();
        testPlongeurNormalRamasseTresor();
        testPlongeurLowOxygenChercheOxygene();
        testPlongeurHighOxygenChercheTresor();
        testRamassageMemeCasePuisNouveauTableau();
        testRobotConstructeurEtToString();
        testRobotRamasseSeulementTresorMemeCase();
        testRobotAgirVaVersTresorEtConsommeBatterie();
        testRobotRechargeRespecteQuantiteEtMaximum();
        testOxygeneEvolutive();

        System.out.println("Tous les tests sont passes (" + verifications + " verifications).");
    }

    private static void resetSimulation(Terrain terrain) {
        Simulation.singleton = true;
        Simulation.terrain = terrain;
        Simulation.Agents = new java.util.ArrayList<>();
        Simulation.nombre_agents = 0;
        Simulation.nombres_ressources = terrain.compterRessources();
        Simulation.ressources = terrain.lesRessources();
    }

    private static void verifier(boolean condition, String message) {
        verifications++;
        if (!condition) {
            throw new RuntimeException("Test echoue: " + message);
        }
    }

    private static void verifierEgal(int attendu, int obtenu, String message) {
        verifier(attendu == obtenu, message + " attendu=" + attendu + " obtenu=" + obtenu);
    }

    private static void verifierMemeObjet(Object attendu, Object obtenu, String message) {
        verifier(attendu == obtenu, message);
    }

    private static void verifierContient(String texte, String attendu, String message) {
        verifier(texte.contains(attendu), message + " texte=" + texte);
    }

    private static void verifierException(Runnable action, String message) {
        verifications++;
        try {
            action.run();
        } catch (RuntimeException e) {
            return;
        }
        throw new RuntimeException("Test echoue: " + message);
    }

    private static void testTerrain() {
        Terrain terrain = new Terrain(4, 4);
        Tresor tresor = new Tresor(12);

        verifierEgal(0, terrain.compterRessources(), "un terrain neuf est vide");
        verifier(terrain.setCase(2, 3, tresor), "setCase accepte une case vide valide");
        verifierEgal(1, terrain.compterRessources(), "le terrain contient une ressource");
        verifierEgal(2, tresor.getLigne(), "setCase met a jour la ligne de la ressource");
        verifierEgal(3, tresor.getColonne(), "setCase met a jour la colonne de la ressource");
        verifierMemeObjet(tresor, terrain.getCase(2, 3), "getCase retrouve la ressource placee");
        verifier(!terrain.caseEstVide(2, 3), "caseEstVide detecte une case occupee");
        verifier(!terrain.setCase(2, 3, new Oxygene(10, 1)), "setCase refuse une case deja occupee");
        verifierException(() -> terrain.getCase(0, 1), "getCase refuse une position invalide");

        Ressource retiree = terrain.viderCase(2, 3);
        verifierMemeObjet(tresor, retiree, "viderCase renvoie la ressource retiree");
        verifier(terrain.caseEstVide(2, 3), "viderCase libere la case");
        verifierEgal(-1, tresor.getLigne(), "viderCase reset la ligne de la ressource");
        verifierEgal(-1, tresor.getColonne(), "viderCase reset la colonne de la ressource");
    }

    private static void testSimulation() {
        Terrain terrain = new Terrain(5, 5);
        Tresor tresor = new Tresor(7);
        Oxygene oxygene = new Oxygene(20, 2);

        terrain.setCase(1, 1, tresor);
        terrain.setCase(5, 5, oxygene);
        resetSimulation(terrain);

        verifierEgal(2, Simulation.ressources.size(), "la simulation charge les ressources du terrain");

        Tresor nouveauTresor = new Tresor(3);
        terrain.setCase(3, 3, nouveauTresor);
        verifierEgal(2, Simulation.ressources.size(), "la liste ne change pas avant turn_refresh");

        Simulation.turn_refresh();
        verifierEgal(3, Simulation.ressources.size(), "turn_refresh recharge la liste des ressources");

        Simulation.enleverRessource(oxygene);
        verifier(terrain.caseEstVide(5, 5), "enleverRessource vide la case du terrain");
        verifierEgal(2, Simulation.ressources.size(), "enleverRessource retire aussi de la liste");
    }

    private static void testSimulationExceptionPlacementTerrainPlein() {
        Terrain terrain = new Terrain(1, 1);
        terrain.setCase(1, 1, new Tresor(1));

        try {
            Simulation.placerRessourceAleatoire(terrain, new Tresor(2));
        } catch (SimulationException e) {
            verifierContient(e.getMessage(), "Impossible de placer la ressource", "SimulationException signale un terrain plein");
            return;
        }

        throw new RuntimeException("Test echoue: placerRessourceAleatoire doit echouer sur un terrain plein");
    }

    private static void testSimulationExceptionNombreAgentsInvalide() {
        Simulation.singleton = false;
        Simulation.terrain = null;
        Simulation.ressources = null;
        Simulation.Agents = null;

        Simulation simulation = Simulation.createSimulation(-1);

        verifier(simulation == null, "createSimulation refuse un nombre d'agents invalide");
        verifier(!Simulation.singleton, "une creation echouee libere le singleton");
        verifier(Simulation.terrain == null, "une creation echouee ne garde pas de terrain");
    }

    private static void testSimulationExceptionRessourceAbsente() {
        Terrain terrain = new Terrain(3, 3);
        Tresor tresorPresent = new Tresor(4);
        Tresor tresorAbsent = new Tresor(9);

        terrain.setCase(1, 1, tresorPresent);
        resetSimulation(terrain);

        Simulation.enleverRessource(tresorAbsent);

        verifierMemeObjet(tresorPresent, terrain.getCase(1, 1), "enleverRessource ignore une ressource absente");
        verifierEgal(1, Simulation.ressources.size(), "une ressource absente ne modifie pas la liste");
    }

    private static void testSimulationEnleverRessourceParIndice() {
        Terrain terrain = new Terrain(5, 5);
        Tresor premier = new Tresor(4);
        Tresor deuxieme = new Tresor(6);

        terrain.setCase(1, 1, premier);
        terrain.setCase(1, 2, deuxieme);
        resetSimulation(terrain);

        Simulation.enleverRessource(0);
        verifier(terrain.caseEstVide(1, 1), "enleverRessource par indice vide la bonne case");
        verifierMemeObjet(deuxieme, Simulation.ressources.get(0), "enleverRessource par indice garde les autres ressources");
        verifierEgal(1, Simulation.ressources.size(), "enleverRessource par indice reduit la liste");
    }

    private static void testSimulationSimulerUnTour() {
        Simulation.singleton = false;
        Simulation.terrain = null;
        Simulation.ressources = null;
        Simulation.Agents = null;

        Simulation.createSimulation(2);
        Simulation.simuler(1);

        verifier(Simulation.terrain != null, "simuler conserve un terrain initialise");
        verifier(Simulation.ressources != null, "simuler conserve une liste de ressources");
        verifier(Simulation.Agents != null, "simuler conserve une liste d'agents");
        verifierEgal(2, Simulation.Agents.size(), "createSimulation cree les agents demandes quand le nombre est pair");
    }

    private static void testDistanceEtTriDesRessources() {
        Terrain terrain = new Terrain(5, 5);
        Plongeur plongeur = new Plongeur(3, 3, terrain);
        Tresor proche = new Tresor(5);
        Tresor loin = new Tresor(9);
        Oxygene milieu = new Oxygene(10, 1);

        terrain.setCase(3, 4, proche);
        terrain.setCase(1, 1, loin);
        terrain.setCase(5, 3, milieu);
        resetSimulation(terrain);

        verifierEgal(1, plongeur.distance(3, 4), "distance calcule une distance de Manhattan");
        verifierEgal(4, plongeur.distance(1, 1), "distance additionne ecart ligne et colonne");

        Ressource[] triees = plongeur.tab_dist();
        verifierMemeObjet(proche, triees[0], "tab_dist place la ressource la plus proche en premier");
        verifierMemeObjet(milieu, triees[1], "tab_dist place la deuxieme ressource par distance");
        verifierMemeObjet(loin, triees[2], "tab_dist place la ressource la plus loin en dernier");
    }

    private static void testDeplacementVers() {
        Terrain terrain = new Terrain(20, 20);
        Plongeur plongeur = new Plongeur(1, 1, terrain);

        boolean atteint = plongeur.seDeplacerVers(20, 20, 30);
        verifier(!atteint, "une cible trop loin n'est pas atteinte en un deplacement");
        verifierEgal(20, plongeur.lig, "deplacement partiel avance sur les lignes");
        verifierEgal(12, plongeur.col, "deplacement partiel avance sur les colonnes restantes");

        atteint = plongeur.seDeplacerVers(20, 20, 30);
        verifier(atteint, "le second deplacement atteint la cible");
        verifierEgal(20, plongeur.lig, "ligne finale correcte");
        verifierEgal(20, plongeur.col, "colonne finale correcte");
    }

    private static void testPlongeurNormalRamasseTresor() {
        Terrain terrain = new Terrain(5, 5);
        Plongeur plongeur = new Plongeur(1, 1, terrain);
        Tresor tresor = new Tresor(8);

        terrain.setCase(1, 2, tresor);
        resetSimulation(terrain);
        plongeur.recharger(10);
        plongeur.agir();

        verifierEgal(1, plongeur.lig, "normal_behavior place le plongeur sur la ligne du tresor");
        verifierEgal(2, plongeur.col, "normal_behavior place le plongeur sur la colonne du tresor");
        verifierEgal(8, plongeur.getScore(), "le plongeur gagne le score du tresor");
        verifier(terrain.caseEstVide(1, 2), "le tresor ramasse disparait du terrain");
        verifierEgal(0, Simulation.ressources.size(), "le tresor ramasse disparait de la simulation");
    }

    private static void testPlongeurLowOxygenChercheOxygene() {
        Terrain terrain = new Terrain(5, 5);
        Plongeur plongeur = new Plongeur(1, 1, terrain);
        Oxygene oxygene = new Oxygene(25, 2);
        Tresor tresor = new Tresor(9);

        terrain.setCase(1, 2, oxygene);
        terrain.setCase(1, 3, tresor);
        resetSimulation(terrain);
        plongeur.agir();

        verifierEgal(1, plongeur.lig, "low_oxygen_behavior va vers l'oxygene");
        verifierEgal(2, plongeur.col, "low_oxygen_behavior atteint l'oxygene");
        verifierEgal(0, plongeur.getScore(), "ramasser de l'oxygene ne change pas le score");
        verifier(terrain.caseEstVide(1, 2), "l'oxygene ramassee disparait");
        verifierMemeObjet(tresor, terrain.getCase(1, 3), "le tresor reste sur le terrain");
    }

    private static void testPlongeurHighOxygenChercheTresor() {
        Terrain terrain = new Terrain(5, 5);
        Plongeur plongeur = new Plongeur(1, 1, terrain);
        Oxygene oxygene = new Oxygene(20, 2);
        Tresor tresor = new Tresor(11);

        terrain.setCase(1, 2, oxygene);
        terrain.setCase(1, 3, tresor);
        resetSimulation(terrain);
        plongeur.recharger(100);
        plongeur.agir();

        verifierEgal(1, plongeur.lig, "high_oxygen_behavior va vers le tresor");
        verifierEgal(3, plongeur.col, "high_oxygen_behavior atteint le tresor");
        verifierEgal(11, plongeur.getScore(), "le score augmente avec le tresor");
        verifierMemeObjet(oxygene, terrain.getCase(1, 2), "l'oxygene reste sur le terrain");
        verifier(terrain.caseEstVide(1, 3), "le tresor est retire du terrain");
    }

    private static void testRamassageMemeCasePuisNouveauTableau() {
        Terrain terrain = new Terrain(5, 5);
        Plongeur plongeur = new Plongeur(2, 2, terrain);
        Tresor tresor = new Tresor(5);
        Oxygene oxygene = new Oxygene(15, 2);

        terrain.setCase(2, 2, tresor);
        terrain.setCase(2, 3, oxygene);
        resetSimulation(terrain);
        plongeur.recharger(10);
        plongeur.agir();

        verifierEgal(5, plongeur.getScore(), "le tresor sur la meme case est ramasse au debut du tour");
        verifierEgal(2, plongeur.lig, "apres le ramassage initial, le plongeur agit avec la liste mise a jour");
        verifierEgal(3, plongeur.col, "le plongeur atteint ensuite l'autre ressource");
        verifierEgal(0, Simulation.ressources.size(), "les deux ressources ont ete retirees");
    }

    private static void testRobotConstructeurEtToString() {
        Terrain terrain = new Terrain(5, 5);
        Robot robot = new Robot(2, 3, terrain);
        String texte = robot.toString();

        verifierContient(texte, "Robot N", "toString indique que l'agent est un robot");
        verifierContient(texte, "col-3lig-2", "toString affiche la position du robot");
        verifierContient(texte, "Batterie 40", "le robot commence avec la batterie de depart");
        verifierContient(texte, "En recharge : false", "le robot ne commence pas en recharge");
        verifierContient(texte, "Score : 0", "le robot commence avec un score nul");
    }

    private static void testRobotRamasseSeulementTresorMemeCase() {
        Terrain terrainOxygene = new Terrain(5, 5);
        Robot robotOxygene = new Robot(2, 2, terrainOxygene);
        Oxygene oxygene = new Oxygene(20, 1);

        terrainOxygene.setCase(2, 2, oxygene);
        resetSimulation(terrainOxygene);
        robotOxygene.ramasseMemecaseTresor();

        verifierEgal(0, robotOxygene.getScore(), "le robot ne gagne pas de score avec l'oxygene");
        verifierMemeObjet(oxygene, terrainOxygene.getCase(2, 2), "le robot ne ramasse pas l'oxygene");

        Terrain terrainTresor = new Terrain(5, 5);
        Robot robotTresor = new Robot(2, 2, terrainTresor);
        Tresor tresor = new Tresor(13);

        terrainTresor.setCase(2, 2, tresor);
        resetSimulation(terrainTresor);
        robotTresor.ramasseMemecaseTresor();

        verifierEgal(13, robotTresor.getScore(), "le robot ramasse un tresor sur sa case");
        verifier(terrainTresor.caseEstVide(2, 2), "le tresor ramasse par le robot disparait du terrain");
        verifierEgal(0, Simulation.ressources.size(), "le tresor ramasse par le robot disparait de la simulation");
    }

    private static void testRobotAgirVaVersTresorEtConsommeBatterie() {
        Terrain terrain = new Terrain(5, 5);
        Robot robot = new Robot(1, 1, terrain);
        Tresor tresor = new Tresor(17);

        terrain.setCase(1, 3, tresor);
        resetSimulation(terrain);
        robot.agir();

        verifierEgal(1, robot.lig, "robot_behavior va vers la ligne du tresor");
        verifierEgal(3, robot.col, "robot_behavior va vers la colonne du tresor");
        verifierEgal(17, robot.getScore(), "le robot ramasse le tresor atteint");
        verifier(terrain.caseEstVide(1, 3), "le tresor atteint par le robot est retire du terrain");
        verifierContient(robot.toString(), "Batterie 35", "agir consomme la batterie du robot");
    }

    private static void testRobotRechargeRespecteQuantiteEtMaximum() {
        Terrain terrain = new Terrain(5, 5);
        Robot robot = new Robot(1, 1, terrain);

        robot.recharger(100);

        verifierContient(robot.toString(), "Batterie 40", "recharger doit utiliser la quantite recue et plafonner au maximum");
        verifierContient(robot.toString(), "En recharge : false", "un robot plein ne doit plus etre en recharge");
    }

    private static void testOxygeneEvolutive() {
        Oxygene oxygene = new Oxygene(6, 1);

        oxygene.evoluer_temps();
        verifierEgal(6, oxygene.getQuantite(), "l'oxygene n'evolue pas avant la fin du compteur");

        oxygene.evoluer_temps();
        verifier(
            oxygene.getQuantite() == 3 || oxygene.getQuantite() == 16,
            "l'oxygene evolue ensuite selon le tirage aleatoire prevu"
        );

        RessourceEvolutivePourTest evolutive = new RessourceEvolutivePourTest(3, 2);
        evolutive.evoluer_temps();
        evolutive.evoluer_temps();
        verifierEgal(3, evolutive.getQuantite(), "une ressource evolutive attend que le compteur arrive a zero");
        evolutive.evoluer_temps();
        verifierEgal(4, evolutive.getQuantite(), "une ressource evolutive appelle evoluer quand le compteur vaut zero");
        verifierEgal(1, evolutive.nbEvolutions, "evoluer est appelee une seule fois au bon moment");
    }

    private static class RessourceEvolutivePourTest extends RessourceEvolutive {
        private int nbEvolutions;

        RessourceEvolutivePourTest(int quantite, int timeCount) {
            super("Test", quantite, timeCount);
            this.nbEvolutions = 0;
        }

        protected void evoluer() {
            this.nbEvolutions++;
            this.setQuantite(this.getQuantite() + 1);
        }
    }
}
