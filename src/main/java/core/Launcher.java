package core;

import core.AdViz;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This core.Launcher class is used to allow the game to be built into a shaded jar file which then loads JavaFX. This
 * core.Launcher is used when running as a shaded jar file.
 */
public class Launcher {
  private static final Logger logger = LogManager.getLogger(Launcher.class);

   /**
    * Launch the JavaFX Application, passing through the commandline arguments
    *
    * @param args commandline arguments
    */
   public static void main(String[] args) {

     logger.info("Launching AdViz");
     AdViz.main(args);
   }
 }


