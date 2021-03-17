package ru.chat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    private static Scene scene;
    private static Stage ourStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ourStage = primaryStage;
        scene = new Scene(loadFxml("/chat.fxml"), 700, 700);
        primaryStage.setTitle("Chat");
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Parent loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static Stage getStage(){
        return ourStage;
    }
}
