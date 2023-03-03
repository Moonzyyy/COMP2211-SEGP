package core;

import model.Model;
import view.AppView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private final AppView theView;

    public Controller(AppView view, Model model) {
        this.theView = view;

//        this.theView.addMetricSelectionListener(new MetricSelectionListener());
    }

    private class MetricSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Metric Selected");
        }
    }
}
