package core;

import java.awt.event.ActionListener;
import javafx.event.ActionEvent;
import listeners.MetricListener;
import model.Model;
import view.AppView;

public class Controller {
    private final AppView theView;

    public Controller(AppView view, Model model) {
        this.theView = view;
        //model.setMetricListener((metrics -> theView.getDashBoard().createScene(metrics)));

    }







}
