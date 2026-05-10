import java.util.ArrayList;

public final class Terrain {
   public static final int NB_LIGNES_MAX = 20;
   public static final int NB_COLONNES_MAX = 20;
   public final int nbLignes;
   public final int nbColonnes;
   private Ressource[][] terrain;

   public Terrain() {
      this(20, 20);
   }

   public Terrain(int var1, int var2) {
      if (var1 > 0 && var1 <= 20) {
         if (var2 > 0 && var2 <= 20) {
            this.nbLignes = var1;
            this.nbColonnes = var2;
            this.terrain = new Ressource[var1][var2];
         } else {
            throw new RuntimeException("Terrain:constructeur nbColonnes=" + var2 + " hors bornes (0<nbColonnes<=" + 20 + ")");
         }
      } else {
         throw new RuntimeException("Terrain:constructeur nbLignes=" + var1 + " hors bornes (0<nbLignes<=" + 20 + ")");
      }
   }

   public Ressource getCase(int var1, int var2) {
      if (!this.sontValides(var1, var2)) {
         throw new RuntimeException("Terrain:getCase(" + var1 + "," + var2 + ") hors terrain (1<=lig<=" + this.nbLignes + ",1<=col<=" + this.nbColonnes + ")");
      } else {
         return this.terrain[var1 - 1][var2 - 1];
      }
   }

   public Ressource viderCase(int var1, int var2) {
      if (!this.sontValides(var1, var2)) {
         throw new RuntimeException("Terrain:viderCase(" + var1 + "," + var2 + ") hors terrain (1<=lig<=" + this.nbLignes + ",1<=col<=" + this.nbColonnes + ")");
      } else if (this.terrain[var1 - 1][var2 - 1] != null) {
         Ressource var3 = this.terrain[var1 - 1][var2 - 1];
         var3.resetPosition();
         this.terrain[var1 - 1][var2 - 1] = null;
         return var3;
      } else {
         return null;
      }
   }

   public boolean setCase(int var1, int var2, Ressource var3) {
      if (!this.sontValides(var1, var2)) {
         throw new RuntimeException("Terrain:setCase(" + var1 + "," + var2 + ") hors terrain (1<=lig<=" + this.nbLignes + ",1<=col<=" + this.nbColonnes + ")");
      } else if (this.terrain[var1 - 1][var2 - 1] != null) {
         return false;
      } else {
         this.terrain[var1 - 1][var2 - 1] = var3;
         var3.setPosition(var1, var2);
         return true;
      }
   }

   public boolean caseEstVide(int var1, int var2) {
      if (!this.sontValides(var1, var2)) {
         throw new RuntimeException("Terrain:caseEstVide(" + var1 + "," + var2 + ") hors terrain (1<=lig<=" + this.nbLignes + ",1<=col<=" + this.nbColonnes + ")");
      } else {
         return this.terrain[var1 - 1][var2 - 1] == null;
      }
   }

   public ArrayList<Ressource> lesRessources() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.nbLignes; ++var2) {
         for(int var3 = 0; var3 < this.nbColonnes; ++var3) {
            if (this.terrain[var2][var3] != null) {
               var1.add(this.terrain[var2][var3]);
            }
         }
      }

      return var1;
   }

   public boolean sontValides(int var1, int var2) {
      return var1 >= 1 && var1 <= this.nbLignes && var2 >= 1 && var2 <= this.nbColonnes;
   }

   public void afficher(int var1) {
      String var2 = "";
      String var3 = ":";
      String var4 = "";
      int var5 = Math.max(var1, 1);

      for(int var6 = 0; var6 < var5; ++var6) {
         var4 = var4 + "-";
      }

      for(int var10 = 0; var10 < this.nbColonnes; ++var10) {
         var3 = var3 + var4 + ":";
      }

      var3 = var3 + "\n";
      var2 = var3;

      for(int var11 = 0; var11 < this.nbLignes; ++var11) {
         for(int var7 = 0; var7 < this.nbColonnes; ++var7) {
            if (this.terrain[var11][var7] == null) {
               var2 = var2 + "|" + String.format("%-" + var5 + "s", " ");
            } else {
               var2 = var2 + "|" + premiersCar(this.terrain[var11][var7].type, var5);
            }
         }

         var2 = var2 + "|\n" + var3;
      }

      System.out.print(var2);
   }

   public int compterRessources() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.nbLignes; ++var2) {
         for(int var3 = 0; var3 < this.nbColonnes; ++var3) {
            if (this.terrain[var2][var3] != null) {
               ++var1;
            }
         }
      }

      return var1;
   }

   public String toString() {
      int var1 = this.compterRessources();
      String var2 = "Terrain de " + this.nbLignes + "x" + this.nbColonnes + " cases : ";
      if (var1 == 0) {
         var2 = var2 + "toutes les cases sont libres.";
      } else if (var1 == 1) {
         var2 = var2 + "il y a une case occupee.";
      } else {
         var2 = var2 + "il y a " + var1 + " cases occupees.";
      }

      return var2;
   }

   private static String premiersCar(String var0, int var1) {
      String var2 = String.format("%-" + var1 + "s", var0);
      return var2.substring(0, var1);
   }

   public void verifierPositionRessources() {
      int var1 = 0;
      Object var2 = null;

      for(int var3 = 0; var3 < this.nbLignes; ++var3) {
         for(int var4 = 0; var4 < this.nbColonnes; ++var4) {
            if (this.terrain[var3][var4] != null) {
               ++var1;
               Ressource var5 = this.terrain[var3][var4];
               if (var5.getLigne() != var3 + 1) {
                  throw new RuntimeException(var5 + " pas sur la bonne ligne du terrain : " + (var3 + 1));
               }

               if (var5.getColonne() != var4 + 1) {
                  throw new RuntimeException(var5 + " pas sur la bonne colonne du terrain : " + (var4 + 1));
               }
            }
         }
      }

      System.out.println("Les " + var1 + " ressources ont les bonnes coordonnees sur le terrain");
   }
}
