package view.scenes;

import javafx.scene.Scene;

import java.awt.*;

/**
 * Abstract class for all scenes in the application.
 */
abstract public class AbstractScene {

    protected Scene scene;
    protected String font;

    AbstractScene() {
    }

    /**
     * @return get the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param scene set the scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * create the scene
     */
    abstract void createScene();

    public void postShowEdits() {
    }

    /**
     * Set the theme of the scene.
     *
     * @param theme true for dark theme, false for light theme
     */
    public void setStyles(boolean theme) {
    }

    public void setTheme(boolean theme) {
        var classes = this.scene.getRoot().getStyleClass();
        if (!theme) {
            classes.remove("theme-dark");
            classes.add("theme--light");
        } else {
            classes.remove("theme--light");
            classes.add("theme--dark");
        }
    }

    public void setFont(String fontFamily) {
        this.font = fontFamily;
        var classes = this.scene.getRoot().getStyleClass();
        classes.remove("font--roboto");
        classes.remove("font--times_new_roman");
        classes.remove("font--arial");
        classes.remove("font--verdana");
        String fontClass;
        switch (getFont()) {
            case "Times New Roman" -> fontClass = "times_new_roman";
            case "Arial" -> fontClass = "arial";
            case "Verdana" -> fontClass = "verdana";
            default -> fontClass = "roboto";
        }
        classes.add("font--" + fontClass);
    }

    public String getFont() {
        return font;
    }
}
