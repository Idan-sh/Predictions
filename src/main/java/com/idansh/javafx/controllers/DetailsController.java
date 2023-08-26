package com.idansh.javafx.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class for the first screen of the application.
 * this screen will be in charge of loading new simulation data from XML files,
 * and in charge of showing the loaded simulation details.
 */
public class DetailsController {
    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
