package fr.modcraft.launcher;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Base64;

public class ReadLauncherInfos {

    private static File launcherFile;
    private static JSONObject launcherInfos;
    private static Object obj;

    public ReadLauncherInfos(File launcherFile){
        this.launcherFile = launcherFile;
        loadInfos();
    }

    private static void loadInfos(){
        try(FileReader reader = new FileReader(launcherFile)){
            obj = new JSONParser().parse(reader);
            launcherInfos = (JSONObject) obj;
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'existe pas");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public static String getUsername(){
        loadInfos();
        if (launcherFile.exists()) {
            if (launcherInfos.containsKey("username")) {
                System.out.println("username: " + launcherInfos.get("username").toString());
                return launcherInfos.get("username").toString();
            } else return null;
        } else return null;
    }

    public static String getPassword(){
        loadInfos();
        if (launcherFile.exists()) {
            if (launcherInfos.containsKey("password")) {
                try {
                    String passwordDecoded = new String(Base64.getDecoder().decode(launcherInfos.get("password").toString()), "utf-8");
                    System.out.println("Password encoded: " + launcherInfos.get("password").toString());
                    return passwordDecoded;
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Impossible de décoder le mot de passe");
                    e.printStackTrace();
                    return null;
                }
            } else return null;
        } else return null;
    }

    public static boolean getSavePassword(){
        loadInfos();
        if (launcherFile.exists()){
            if (launcherInfos.containsKey("savepassword")) {
                return (boolean) launcherInfos.get("savepassword");
            }else return false;
        } else return false;
    }

    public static int getRAM(){
        loadInfos();
        if (launcherFile.exists()) {
            if (launcherInfos.containsKey("ram")) {
                System.out.println("Ram readed: " + launcherInfos.get("ram"));
                return Integer.parseInt(launcherInfos.get("ram").toString());
            } else return ModcraftLauncher.getDefaultRamAmout();
        } else return ModcraftLauncher.getDefaultRamAmout();
    }
}
