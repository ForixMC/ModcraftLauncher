package fr.modcraft.launcher;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionApp implements Initializable {

    private static Stage window;
    private int maxRam;
    private static Scene scene;
    @FXML
    private ComboBox ramChoice;
    @FXML
    private CheckBox savePassword;
    @FXML
    private ImageView logIMG;

    public static void display(){
        window = new Stage();
        window.setTitle("Options");
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        Parent root = null;
        try {
            root = FXMLLoader.load(OptionApp.class.getResource("OptionApp.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene = new Scene(root, 400, 130);
        window.setScene(scene);
        window.show();

        window.setX(ModcraftLauncher.getPremiumContent().getWidth()/2-400/2+ModcraftLauncher.getPremiumContent().getWindow().getX());
        window.setY(ModcraftLauncher.getPremiumContent().getHeight()/2-130/2+ModcraftLauncher.getPremiumContent().getWindow().getY());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Screen resolution: "+screenSize.width+"x"+screenSize.height);

        if (scene.getWindow().getX()+scene.getWidth() > screenSize.width)
            window.setX(screenSize.width-scene.getWidth());

        if (scene.getWindow().getY()+scene.getHeight() > screenSize.height)
            window.setY(screenSize.height-scene.getHeight());
        fadeInAnimation();
    }

    public void setMaxRam(){
        maxRam = Integer.parseInt(ramChoice.getValue().toString().replaceAll("Go",""));
        SaveLauncherInfos.saveLauncherInfos(maxRam);
    }

    @FXML
    private void close(){
        ModcraftLauncher.deblurAnimation();
        fadeOutAnimation();
    }

    @FXML
    private void changeSavePassword(){
        if (ModcraftLauncher.getSavePassword()){
            ModcraftLauncher.setSavePassword(false);
        } else {
            ModcraftLauncher.setSavePassword(true);
        }
    }

    public static boolean isShowing(){
        return window.isShowing();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ramChoice.getItems().addAll("1Go", "2Go", "3Go", "4Go", "5Go", "6Go", "7Go", "8Go");
        ramChoice.setValue(ReadLauncherInfos.getRAM()+"Go");
        savePassword.setSelected(ReadLauncherInfos.getSavePassword());
        if (savePassword.isSelected())
            ModcraftLauncher.setSavePasswordValue(true);
        else
            ModcraftLauncher.setSavePasswordValue(false);
    }

    public String[] getRamArguments() {
        if (maxRam == 0)
            maxRam = ReadLauncherInfos.getRAM();
        String ramArgs[] = {"-Xmx"+maxRam+"G"};
        System.out.println("ram args: "+ramArgs[0]);
        return ramArgs;
    }

    @FXML
    private void openLogs(){
        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.home")+"\\AppData\\Roaming\\."+GameUpdate.getServerName()+"\\logs"));
            System.out.println("Ouverture des logs dans; "+System.getProperty("user.home")+"\\AppData\\Roaming\\."+GameUpdate.getServerName()+"\\logs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fadeInAnimation(){
        System.out.println("Fade in transition");
        Thread t = new Thread(() ->{
            for (int i = 0; i <= 100; i++){
                int finalI = i;
                Platform.runLater(() -> window.setOpacity((double)finalI /100));
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
                Platform.runLater(() -> {
                    window.setOpacity((double)finalI /100);
                });
                try {
                    Thread.sleep(250/100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> window.close());
        });
        t.start();
        System.out.println("in the function");
    }


    //Détails bouton logs
    @FXML
    private void mouseEnterLogs(){
        logIMG.setImage(new javafx.scene.image.Image("/resources/logButtonM.png"));
    }

    @FXML
    private void mouseExitLogs(){
        logIMG.setImage(new javafx.scene.image.Image("/resources/logButton.png"));
    }

    @FXML
    private void mouseClicLogs(){
        logIMG.setImage(new javafx.scene.image.Image("/resources/logButtonC.png"));
    }

    @FXML
    private void mouseReleaseLogs(){
        logIMG.setImage(new Image("/resources/logButtonM.png"));
    }
}
