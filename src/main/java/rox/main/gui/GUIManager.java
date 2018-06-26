package rox.main.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import rox.main.Main;
import rox.main.gamesystem.GameType;

import java.util.Timer;
import java.util.UUID;

public class GUIManager extends Application {

    private Parent root;

    private FXMLLoader loader;

    private Timeline timeline;

    @FXML
    private TextField serverName;

    @FXML
    private PasswordField serverPassword;

    @FXML
    private ChoiceBox<String> serverType;

    @FXML
    private ToggleButton mainServerToggle;

    @FXML
    private Pane memPane;

    @FXML
    private CheckBox memCheck;

    @FXML
    private Label usedMemory, freeMemory, maxMemory, responseServer;

    @Override
    public void init(){
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ROX - GUI");
        root = (loader = new FXMLLoader(getClass().getResource("/fxml/GUI.fxml"))).load();
        root.getStylesheets().add(getClass().getResource("/fxml/css/style.css").toExternalForm());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public void toggleMainServer(){
        if(mainServerToggle.isSelected()){
            mainServerToggle.setText("Online");
            Main.getMainServer().start();
        }else{
            mainServerToggle.setText("Offline");
            Main.getMainServer().stop();
        }
    }

    public void displayMem(){
        if(memCheck.isSelected()){
            timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                    event -> {
                        long usedmem = Runtime.getRuntime().totalMemory() / 1000000;
                        long maxmem = Runtime.getRuntime().maxMemory() / 1000000;
                        freeMemory.setText((maxmem - usedmem) + " MB");
                        maxMemory.setText(maxmem + " MB");
                        usedMemory.setText(usedmem + " MB");
                    }));

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }else{
            timeline.stop();
            freeMemory.setText("0 MB");
            maxMemory.setText("0 MB");
            usedMemory.setText("0 MB");
        }
    }

    public void registerGameServer(){
        String serverName = this.serverName.getText();
        String password = this.serverPassword.getText();
        String type = this.serverType.getValue().toUpperCase();
        UUID uuid = UUID.randomUUID();
        Main.getGameSystem().register(serverName, uuid, password, GameType.valueOf(type));
        responseServer.setText("Created! UUID: " + uuid.toString());
        responseServer.setVisible(true);

    }

    public void show(){
        launch();
    }
}
