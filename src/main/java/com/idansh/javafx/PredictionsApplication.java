package com.idansh.javafx;

import com.idansh.javafx.manager.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PredictionsApplication extends Application {
    private final String LOADER_SCENE_RELATIVE_PATH = "scenes/loader/LoaderScene.fxml";
    private SimulationManager simulationManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        simulationManager = new SimulationManager();
        FXMLLoader loaderSceneLoader = getFXMLLoader(LOADER_SCENE_RELATIVE_PATH);

//        Scene scene = new Scene(loaderSceneLoader.load(loaderSceneLoader.getLocation().openStream()), 500, 400);
//
//        primaryStage.setTitle("Players Manager");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    /**
     * Using the received relative path to an FXML file, creates a FXMLLoader object that
     * can be used to load the file.
     * @param relativePath this path will be relative to the resources package of the application (src/main/resources).
     * @return created FXMLLoader object that handles the FXML in the received path.
     */
    private FXMLLoader getFXMLLoader(String relativePath) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/LoaderScene.fxml");
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
