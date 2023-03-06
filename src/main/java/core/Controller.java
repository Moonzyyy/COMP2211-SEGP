package core;

import model.Model;
import view.AppView;

public class Controller {
    private final AppView theView;

    public Controller(AppView view, Model model) {
        this.theView = view;

//        this.theView.addMetricSelectionListener(new MetricSelectionListener());
    }
}
