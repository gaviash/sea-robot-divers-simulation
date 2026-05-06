public class Ressource {
   private static int nbRessources = 0;
   public final int ident;
   public final String type;
   private int quantite;
   private int lig;
   private int col;

   public Ressource(String var1, int var2) {
      this.type = var1;
      this.quantite = var2;
      ++nbRessources;
      this.ident = nbRessources;
      this.lig = -1;
      this.col = -1;
   }

   public int getQuantite() {
      return this.quantite;
   }

   public void setQuantite(int var1) {
      this.quantite = var1;
   }

   public int getLigne() {
      return this.lig;
   }

   public int getColonne() {
      return this.col;
   }

   public void setPosition(int var1, int var2) {
      this.lig = var1;
      this.col = var2;
   }

   public void resetPosition() {
      this.lig = -1;
      this.col = -1;
   }

   public String toString() {
      return this.type + "[id:" + this.ident + " quantite:" + this.quantite + " position:(" + this.lig + "," + this.col + ")]";
   }

   public static int getNbRessources() {
      return nbRessources;
   }
}