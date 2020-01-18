package fr.modcraft.launcher;

import be.manugame.SupdateManager;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Arrays;

public class GameUpdate {

    private static String serverName = "Modcraft";
    public GameVersion FL_VERSION;
    public final GameFolder FL_FOLDER = new GameFolder("assets", "libs", "natives", "minecraft.jar");
    public GameInfos FL_INFOS;
    public File FL_DIR;
    public final File FL_CRASH_FOLDER = new File(FL_DIR, "crashes");

    public static String getServerName(){
        return serverName;
    }

    private Thread updateThread;

    private static AuthInfos authInfos;

    private ModcraftLauncher modcraftLauncher;
    private OptionApp optionApp;

    public GameUpdate(ModcraftLauncher modcraftLauncher, OptionApp optionApp, GameVersion gameVersion, GameInfos gameInfos, File gameDir){
        System.out.println("Démarrage de la classe GameUpdate...");
        FL_VERSION = gameVersion;
        FL_INFOS = gameInfos;
        FL_DIR = gameDir;
        this.modcraftLauncher = modcraftLauncher;
        this.optionApp = optionApp;
    }

    public static void auth(String username, String password) throws AuthenticationException {
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
        authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
    }

    public static void auth(String username) throws AuthenticationException {
        authInfos = new AuthInfos(username, "CHEH 3lik", "blablabla");
    }

    public void launch() throws LaunchException {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(FL_INFOS, FL_FOLDER, authInfos);
        profile.getVmArgs().addAll(Arrays.asList(optionApp.getRamArguments()));
        ExternalLauncher launcher = new ExternalLauncher(profile);
        Process p = launcher.launch();

        ProcessLogManager manager = new ProcessLogManager(p.getInputStream(), new File(FL_DIR, "logs.txt"));
        manager.start();

        try {
            Thread.sleep(5000);
            modcraftLauncher.close();
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


}
