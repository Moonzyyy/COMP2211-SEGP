package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FaqScene extends AbstractScene {

    private ListView<VBox> faqList;
    private Button backButton;

    public FaqScene() {
        super();

    }

    public void createScene() {
        Text faq1 = new Text("Q - I currently have the wrong csv files open; how do I re-import the correct ones?\n" +
                "A - Navigate back to the main menu, and you will see the import button which will allow you to import new csv files.\n");

        Text faq2 = new Text("Q - How do I save the graphs?\n" +
                "A - If you are on the graph scene you can right click on the graph and save it as a PNG.\n");

        Text faq3 = new Text("Q - How do I zoom in and out of the graph?\n" +
                "A- You can zoom in and out of the graph using the scroll wheel.\n");

        Text faq4 = new Text("Q - I can’t seem to move the graph around; I am only able to zoom in and out of the graph. Is this something not added to the software yet?\n" +
                "A - To move around the graph, you need to hold CTRL while dragging out with left click.\n");

        Text faq5 = new Text("Q - How can I print out the graphs?\n" +
                "A - If you are on one of the metric’s Graph scene, on the top right you should find a print button, once clicked it will pop up with a print ui where you can print out the graph!\n");

        Text faq6 = new Text("Q - How do I filter my data by users in the graph scene?\n" +
                "A - Once you are in the graph scene you can find check boxes on the right of the screen to pick users by gender, age, income, etc.. " +
                "\nChoose the users you would like the graph to represent and click on apply. This should change the data of the graph with the specified users wanted.\n");

        Text faq7 = new Text("Q - How do I filter my data by date in the graph scene?\n" +
                "A - Once you are in the graph scene there will be a drop down box at the bottom middle of the screen which lets you choose between months, weeks, days and hours. " +
                "\nAny change in the drop down box will immediately update the graph. " +
                "\nThere will also be two date pickers, if the one on the left has a date earlier than the one on the right you will find double the amount of lines at their specified dates." +
                "\nThis is to remove unwanted dates from the graph and allow more specified date comparison of the data.\n");

        Text faq8 = new Text("Q - Is it possible to filter the metrics in the dashboard?\n" +
                "A - In the dashboard if you click on the button with the 3 lines at the top left a box will appear on the left side of the software " +
                "\nwith check boxes you can click and choose to filter your metrics by.\n");

        Text faq9 = new Text("Q - Is there a way to change the definition of a bounce?\n" +
                "A - Yes, if you go on the dashboard scene and click the button on the bottom right “Bounce Def” you can change the current bounce definition to pages viewed, amount of time." +
                "\nYou can see your current bounce definition if you click on the top left of the screen to pull out the filtering system. You can find your current bounce definition at the top.\n");

        Text faq10 = new Text("Q - I am trying to view my click cost as a histogram as it is clearer than using a line graph but I am unsure how to do that?\n" +
                "A - If you press on total cost on the dashboard and are on the graph scene, click on the bottom left the button which says “Histogram” which will show you the histogram of click cost.\n");

        Text faq11 = new Text("\n" +
                "Q - Every software I use I prefer using lightmode, how would I be able to change that?\n" +
                "A - Find the “Setting” button within the starting page of the software and within the Settings Scene you can change the color scheme to dark mode or light mode.\n");


        VBox vBox = new VBox(7);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(faq1, faq2, faq3, faq4, faq5, faq6, faq7, faq8, faq9, faq10, faq11);

        faqList = new ListView<>();
        faqList.getStyleClass().add("list-cell");

        faqList.getItems().add(vBox);
        faqList.setMinHeight(600);
        faqList.setMaxWidth(1200);


        backButton = new Button("Back");
        backButton.getStyleClass().add("backButton");

        var startBorderPane = new BorderPane();

        BorderPane.setAlignment(faqList, Pos.TOP_CENTER);

        BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

        startBorderPane.setTop(faqList);
        startBorderPane.setLeft(backButton);

        scene = new Scene(startBorderPane, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/view/startLight.css").toExternalForm());

    }

    public Button getBackButton(){return backButton;}

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
////            menuImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu.png"))));
//
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardLight.css").toExternalForm());
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardCompLight.css").toExternalForm());
////            menuImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menuLight.png"))));
//
//        }
    }
}
