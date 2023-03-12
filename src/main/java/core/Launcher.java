package core;

import core.AdViz;
import model.Model;

/**
 * This core.Launcher class is used to allow the game to be built into a shaded jar file which then loads JavaFX. This
 * core.Launcher is used when running as a shaded jar file.
 */
public class Launcher {

   /**
    * Launch the JavaFX Application, passing through the commandline arguments
    *
    * @param args commandline arguments
    */
   public static void main(String[] args) {
     AdViz.main(args);
//       Model model = new Model();
//       model.importData();
   }
 }


