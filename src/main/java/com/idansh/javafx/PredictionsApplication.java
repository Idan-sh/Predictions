package com.idansh.javafx;

import com.idansh.javafx.manager.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class PredictionsApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        int APPLICATION_WIDTH = 500, APPLICATION_LENGTH = 400;
        String MAIN_SCREEN_RELATIVE_PATH = "/fxml/MainScreen.fxml";

        // Load the first screen in which a simulation can be loaded, and its details will be showed
        // this path is relative to the resources directory
        FXMLLoader mainScreenLoader = getFXMLLoader(MAIN_SCREEN_RELATIVE_PATH);
        Scene mainScreen = new Scene(mainScreenLoader.load(mainScreenLoader.getLocation().openStream()), APPLICATION_WIDTH, APPLICATION_LENGTH);

        // Set up the stage, show the first screen
        setStageProperties(primaryStage);
        primaryStage.setScene(mainScreen);
        primaryStage.show();
    }


    /**
     * Set up a stage's basic properties.
     * @param stage the main stage on which the scenes will be shown.
     */
    private void setStageProperties(Stage stage) {
        // Set the minimal size of the window
        stage.setMinWidth(300);
        stage.setMinHeight(200);
        stage.setWidth(800);
        stage.setHeight(600);

        stage.setTitle("Predictions Simulator");
    }


    /**
     * Using the received relative path to an FXML file, creates a FXMLLoader object that
     * can be used to load the file.
     * @param relativePath path relative to the resources package of the application (src/main/resources).
     * @return created FXMLLoader object that handles the FXML in the received path.
     */
    private FXMLLoader getFXMLLoader(String relativePath) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(relativePath);
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }


    /**
     * The 'main' method is ignored in a correctly deployed JavaFX application.
     * The 'main' method serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g. in IDEs with limited FX
     * support. NetBeans ignores this method.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
