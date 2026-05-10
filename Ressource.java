/**
 * Ressource presente sur le terrain de simulation.
 * Une ressource possede un type, une quantite et une position.
 *
 * @author Gavriel Myara
 */
public class Ressource {
   private static int nbRessources = 0;
   /** Identifiant unique de la ressource. */
   public final int ident;
   /** Type textuel de la ressource. */
   public final String type;
   private int quantite;
   private int lig;
   private int col;

   /**
    * Construit une ressource.
    *
    * @param type type de la ressource
    * @param quantite quantite associee a la ressource
    */
   public Ressource(String type, int quantite) {
      this.type = type;
      this.quantite = quantite;
      ++nbRessources;
      this.ident = nbRessources;
      this.lig = -1;
      this.col = -1;
   }

   /**
    * Renvoie la quantite de la ressource.
    *
    * @return quantite actuelle
    */
   public int getQuantite() {
      return this.quantite;
   }

   /**
    * Modifie la quantite de la ressource.
    *
    * @param quantite nouvelle quantite
    */
   public void setQuantite(int quantite) {
      this.quantite = quantite;
   }

   /**
    * Renvoie la ligne de la ressource.
    *
    * @return ligne actuelle, ou -1 si la ressource n'est pas placee
    */
   public int getLigne() {
      return this.lig;
   }

   /**
    * Renvoie la colonne de la ressource.
    *
    * @return colonne actuelle, ou -1 si la ressource n'est pas placee
    */
   public int getColonne() {
      return this.col;
   }

   /**
    * Place la ressource sur une case du terrain.
    *
    * @param lig ligne de placement
    * @param col colonne de placement
    */
   public void setPosition(int lig, int col) {
      this.lig = lig;
      this.col = col;
   }

   /**
    * Retire la position de la ressource.
    */
   public void resetPosition() {
      this.lig = -1;
      this.col = -1;
   }

   /**
    * Renvoie une description textuelle de la ressource.
    *
    * @return representation textuelle de la ressource
    */
   public String toString() {
      return this.type + "[id:" + this.ident + " quantite:" + this.quantite + " position:(" + this.lig + "," + this.col + ")]";
   }

   /**
    * Renvoie le nombre total de ressources creees.
    *
    * @return nombre de ressources creees depuis le lancement
    */
   public static int getNbRessources() {
      return nbRessources;
   }
}
