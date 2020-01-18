package fr.modcraft.launcher;

import be.manugame.SupdateManager;
import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class ModcraftLauncher extends Application implements Initializable {

    private static Stage window;
    private static Scene premiumContent;
    private boolean premiumMode = true;
    private static boolean savePassword = false;
    private String defaultText = "Premium Mode: ";
    private GameUpdate gameUpdate;
    private static String serverName = "Modcraft"; //Nom du server/launcher
    private static int defaultRamAmout;
    private static int blurAmount = 20;
    private static int fadingTime = 250; //En millisecondes
    @FXML
    private Label baseText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label infoText;
    @FXML
    private ProgressBar chargementBar;
    @FXML
    private ImageView discordIMG;
    @FXML
    private ImageView switchIMG;
    @FXML
    private ImageView settingsIMG;
    @FXML
    private ImageView modcraftIMG;
    @FXML
    private ImageView closeIMG;
    @FXML
    private ImageView hideIMG;
    @FXML
    private ImageView playIMG;

    private static Parent root;

    @FXML
    private Rectangle windowDrag;

    public static final GameVersion FL_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
    public static final GameInfos FL_INFOS = new GameInfos(serverName, FL_VERSION, new GameTweak[] {GameTweak.FORGE});
    public static final File FL_DIR = FL_INFOS.getGameDir();

    public static boolean getSavePassword(){
        return savePassword;
    }

    public static void setSavePassword(boolean value){
        savePassword = value;
        SaveLauncherInfos.saveLauncherInfos(savePassword);
        System.out.println("savePassword value: "+savePassword);
    }

    public static void setSavePasswordValue(boolean value){
        savePassword = value;
    }

    public static File getDir(){
        return FL_DIR;
    }

    public String getServerName() {
        return serverName;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static int getDefaultRamAmout(){
        return defaultRamAmout;
    }
    double sx = 0;
    double sy = 0;
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*JSONParser parser = new JSONParser();
        JSONObject Jobject;
        try(InputStreamReader inputStreamReader = new InputStreamReader(new URL("http://v1.modcraftmc.fr/server.json").openStream())){
            Object obj = parser.parse(inputStreamReader);
            Jobject = (JSONObject) obj;
            System.out.println("maintenance: "+Jobject.get("maintenance"));
        }*/
        defaultRamAmout = 2;
        new SaveLauncherInfos(new File(FL_DIR.toString()+"\\Launcher\\launcher_infos.json"));
        new ReadLauncherInfos(new File(FL_DIR.toString()+"\\Launcher\\launcher_infos.json"));

        window = primaryStage;
        window.initStyle(StageStyle.UNDECORATED);
        window.setResizable(false);
        window.setTitle("ModCraft Launcher");

        root = FXMLLoader.load(getClass().getResource("ModcraftLauncher.fxml"));

        premiumContent = new Scene(root, 1080-10, 608-10);
        //premiumContent.getStylesheets().add("Design.css");

        System.out.println(windowDrag);
        window.setScene(premiumContent);
        window.show();
    }

    public static Scene getPremiumContent(){
        return premiumContent;
    }

    //vgtom4

    @FXML
    public void switchMode(){
        if (premiumMode) {
            premiumMode = false;
            baseText.setText(defaultText+"Désactivé");
        }else {
            premiumMode = true;
            baseText.setText(defaultText+"Activé");
        }
        System.out.println("Premium Mode: "+premiumMode);
    }

    @FXML
    public void showOptions(){
        blurAnimation();
        OptionApp.display();
    }
    @FXML
    private void goToSite(){
        BrowserControl.displayURL("https://modcraftmc.fr/");
    }
    @FXML
    private void goToDiscord(){
        BrowserControl.displayURL("https://discord.io/modcraftmc");
    }

    @FXML
    public void Iconified(){
        System.out.println(window);
        Platform.runLater(() -> window.setIconified(true));
    }

    //en fait vgtom4 est nul

    @FXML
    private void logButton(){
        System.out.println("voila:"+passwordField.getText().replaceAll(" ", "")+":ici");
        if (!(usernameField.getText().replaceAll(" ", "").equals("") || passwordField.getText().replaceAll(" ", "").equals(""))) {
            System.out.println(usernameField.getText().replaceAll(" ", ""));
            System.out.println(passwordField.getText().replaceAll(" ", ""));
            SaveLauncherInfos.saveLauncherInfos(usernameField.getText(), passwordField.getText());
            ModcraftLauncher modcraftLauncher = this;
            System.out.println("GetUsername(): "+ReadLauncherInfos.getUsername());
            System.out.println("GetPassword(): "+ReadLauncherInfos.getPassword());
            infoText.setText("Analyse des fichiers du jeu en cours...");
            Thread t = new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        if (premiumMode)
                            GameUpdate.auth(usernameField.getText(), passwordField.getText());
                        else
                            GameUpdate.auth(usernameField.getText());
                        System.out.println("accès en cours à la classe GameUpdate...");
                        OptionApp optionApp = new OptionApp();
                        gameUpdate = new GameUpdate(modcraftLauncher, optionApp, FL_VERSION, FL_INFOS, FL_DIR);

                        SupdateManager supdateManager = new SupdateManager("http://v1.modcraftmc.fr:100", FL_DIR, chargementBar, infoText);
                        supdateManager.setDefaultText("Téléchargement des fichiers");

                        supdateManager.supdate().progressProperty().addListener((observable, oldValue, newValue) -> {
                            int number = (int) (newValue.doubleValue() * 100);
                            infoText.setText("Téléchargement des fichiers "+number+"%");
                        });

                        supdateManager.getTask().setOnSucceeded(event -> {
                            infoText.setText("Lancement du jeu");
                            try {
                                sleep(1000);
                                gameUpdate.launch();
                            } catch (LaunchException e) {
                                e.printStackTrace();
                                blurAnimation();
                                AlertBox.display("Erreur", "Impossible de lancer le jeu");
                                Platform.runLater(() -> infoText.setText("Bienvenue sur Modcraft !"));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        supdateManager.start();
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                        blurAnimation();
                        Platform.runLater(() -> AlertBox.display("Erreur", "Identifiant ou mot de passe incorrect."));
                        Platform.runLater(() -> infoText.setText("Bienvenue sur Modcraft !"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        blurAnimation();
                        AlertBox.display("Erreur", "Mise à jour du jeu impossible");
                        Platform.runLater(() -> infoText.setText("Bienvenue sur Modcraft !"));
                    }
                }
            };
            t.start();
        } else {
            System.out.println("dans le else");
            //alertBox.test();
            blurAnimation();
            AlertBox.display("Erreur", "Veuillez mettre un identifiant et un mot de passe !");
        }
    }

    private void blurAnimation(){
        Thread t = new Thread(() -> {
            for (int i = 0; i < blurAmount; i++){
                int finalI = i;
                Platform.runLater(() -> root.setEffect(new GaussianBlur(finalI)));
                try {
                    Thread.sleep(fadingTime/blurAmount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static void deblurAnimation(){
        Thread t = new Thread(() -> {
            for (int i = blurAmount; i > 0; i--){
                int finalI = i;
                Platform.runLater(() -> root.setEffect(new GaussianBlur(finalI)));
                try {
                    Thread.sleep(fadingTime/blurAmount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void close(){
        window.close();
        System.exit(0);
    }

    //Détails bouton Discord
    @FXML
    private void mouseEnterDiscord(){
        discordIMG.setImage(new Image("/resources/discordM.png"));
    }

    @FXML
    private void mouseExitDiscord(){
        discordIMG.setImage(new Image("/resources/discord.png"));
    }

    @FXML
    private void mouseClickDiscord(){
        discordIMG.setImage(new Image("/resources/discordC.png"));
    }

    @FXML
    private void mouseReleaseDiscord(){
        discordIMG.setImage(new Image("/resources/discordM.png"));
    }


    //Détails bouton switch
    @FXML
    private void mouseEnterSwitch(){
        switchIMG.setImage(new Image("/resources/switchM.png"));
    }

    @FXML
    private void mouseExitSwitch(){
        switchIMG.setImage(new Image("/resources/switch.png"));
    }

    @FXML
    private void mouseClickSwitch(){
        switchIMG.setImage(new Image("/resources/switchC.png"));
    }

    @FXML
    private void mouseReleaseSwitch(){
        switchIMG.setImage(new Image("/resources/switchM.png"));
    }

    //Détails bouton Options
    @FXML
    private void mouseEnterOptions(){
        settingsIMG.setImage(new Image("/resources/settingsM.png"));
    }

    @FXML
    private void mouseExitOptions(){
        settingsIMG.setImage(new Image("/resources/settings.png"));
    }

    @FXML
    private void mouseClickOptions(){
        settingsIMG.setImage(new Image("/resources/settingsC.png"));
    }

    @FXML
    private void mouseReleaseOptions(){
        settingsIMG.setImage(new Image("/resources/settingsM.png"));
    }

    //Détails bouton Modcraft
    @FXML
    private void mouseEnterModcraft(){
        modcraftIMG.setImage(new Image("/resources/modcraftM.png"));
    }

    @FXML
    private void mouseExitModcraft(){
        modcraftIMG.setImage(new Image("/resources/modcraft.png"));
    }

    @FXML
    private void mouseClickModcraft(){
        modcraftIMG.setImage(new Image("/resources/modcraftC.png"));
    }

    @FXML
    private void mouseReleaseModcraft(){
        modcraftIMG.setImage(new Image("/resources/modcraftM.png"));
    }


    //Détails bouton close
    @FXML
    private void mouseEnterExit(){
        closeIMG.setImage(new Image("/resources/closeM.png"));
    }

    @FXML
    private void mouseExitExit(){
        closeIMG.setImage(new Image("/resources/close.png"));
    }

    @FXML
    private void mouseClicExit(){
        closeIMG.setImage(new Image("/resources/closeC.png"));
    }

    @FXML
    private void mouseReleaseExit(){
        closeIMG.setImage(new Image("/resources/closeM.png"));
    }


    //Détails bouton hide
    @FXML
    private void mouseEnterHide(){
        hideIMG.setImage(new Image("/resources/hideM.png"));
    }

    @FXML
    private void mouseExitHide(){
        hideIMG.setImage(new Image("/resources/hide.png"));
    }

    @FXML
    private void mouseClicHide(){
        hideIMG.setImage(new Image("/resources/hideC.png"));
    }

    @FXML
    private void mouseReleaseHide(){
        hideIMG.setImage(new Image("/resources/hideM.png"));
    }


    //Détails bouton play
    @FXML
    private void mouseEnterPlay(){
        playIMG.setImage(new Image("/resources/playM.png"));
    }

    @FXML
    private void mouseExitPlay(){
        playIMG.setImage(new Image("/resources/play.png"));
    }

    @FXML
    private void mouseClicPlay(){
        playIMG.setImage(new Image("/resources/playC.png"));
    }

    @FXML
    private void mouseReleasePlay(){
        playIMG.setImage(new Image("/resources/playM.png"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoText.setText("Bienvenue sur ModcraftMC !");
        System.out.println("je suis initialize");
        if (ReadLauncherInfos.getUsername() != null){
            System.out.println("voila l'user: "+ReadLauncherInfos.getUsername());
            usernameField.setText(ReadLauncherInfos.getUsername());
        } else
            System.out.println("c'est null !");

        if (ReadLauncherInfos.getPassword() != null){
            System.out.println("voila le mdp: "+ReadLauncherInfos.getPassword());
            passwordField.setText(ReadLauncherInfos.getPassword());
        } else
            System.out.println("mdp null !");
        windowDrag.addEventFilter(MOUSE_PRESSED, e -> {
            sx = e.getScreenX() - window.getX();
            sy = e.getScreenY() - window.getY();
        });
        windowDrag.addEventFilter(MOUSE_DRAGGED, e -> {
            window.setX(e.getScreenX() - sx);
            window.setY(e.getScreenY() - sy);
        });

        chargementBar.setStyle("-fx-accent: orange;");
    }
}