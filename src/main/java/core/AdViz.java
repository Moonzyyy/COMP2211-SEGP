package core;

import model.Model;
import view.View;

public class AdViz {
    private View theView;
    private Model theModel;
    private Controller theController;

    public AdViz() {
        this.theModel = new Model();
        this.theView = new View();
        this.theController = new Controller(theView, theModel);
    }

    public static void main(String[] args) {
        AdViz app = new AdViz();
    }
}
