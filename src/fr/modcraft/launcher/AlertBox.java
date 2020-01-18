package fr.modcraft.launcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlertBox implements Initializable {

    private static Stage alWin;
    private Image image;
    @FXML
    private ImageView alertImage;
    @FXML
    private Label messageLabel;

    private static String messageS;

    public static void display(String title, String message){
        messageS = message;
        alWin = new Stage();
        System.out.println("nouveau message d'erreur !");
        alWin.initModality(Modality.APPLICATION_MODAL);
        alWin.initStyle(StageStyle.UNDECORATED);
        alWin.setTitle(title);
        Parent root = null;
        try {
            root = FXMLLoader.load(AlertBox.class.getResource("AlertWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 250, 150);

        alWin.setScene(scene);
        alWin.setOpacity(0);
        alWin.show();
        alWin.setX(ModcraftLauncher.getPremiumContent().getWidth()/2-250/2+ModcraftLauncher.getPremiumContent().getWindow().getX());
        alWin.setY(ModcraftLauncher.getPremiumContent().getHeight()/2-150/2+ModcraftLauncher.getPremiumContent().getWindow().getY());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen resolution: "+screenSize.width+"x"+screenSize.height);

        if (scene.getWindow().getX()+scene.getWidth() > screenSize.width)
            alWin.setX(screenSize.width-scene.getWidth());

        if (scene.getWindow().getY()+scene.getHeight() > screenSize.height)
            alWin.setY(screenSize.height-scene.getHeight());
        fadeInAnimation();
    }

    public void okButton(){
        ModcraftLauncher.deblurAnimation();
        fadeOutAnimation();
    }

    private static void fadeInAnimation(){
        System.out.println("Fade in transition");
        Thread t = new Thread(() ->{
            for (int i = 0; i <= 100; i++){
                int finalI = i;
                Platform.runLater(() -> alWin.setOpacity((double)finalI /100));
                try {
                    Thread.sleep(250/100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private static void fadeOutAnimation(){
        Thread t = new Thread(() -> {
            System.out.println("Fade out transition");
            for (int i = 100; i >= 0; i--){
                int finalI = i;
                Platform.runLater(() -> alWin.setOpacity((double)finalI /100));
                try {
                    Thread.sleep(250/100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> alWin.close());
        });
        t.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image = new Image("/resources/alert.png");
        alertImage.setImage(image);
        messageLabel.setText(messageS);
    }
}