package fr.modcraft.launcher;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class SaveLauncherInfos {

    private static File launcherFile;
    private static int ram;
    private static String username, password;
    private static boolean savePassword;

    public SaveLauncherInfos(File launcherFile){
        this.launcherFile = launcherFile;
        File parent = new File(launcherFile.getParent());
        if (!parent.exists()){
            System.out.println("j'existe pas");
            parent.mkdirs();
            System.out.println("je me suis crée à: "+parent.toString());
        }
    }

    private static void saveLauncherInfos(){
        //Initialisation de la sauvegarde JSON
        System.out.println("Sauvegarde de ram en cours...");
        JSONObject jObject = new JSONObject();

        //Sauvegarde de l'username
        if (username != null)
            jObject.put("username", username);
        else if (ReadLauncherInfos.getUsername() != null)
            jObject.put("username", ReadLauncherInfos.getUsername());

        //Sauvegarde du password
        try {
            if (password != null)
                jObject.put("password", Base64.getEncoder().encodeToString(password.getBytes("utf-8")));
            else if (ReadLauncherInfos.getPassword() != null)
                jObject.put("password", Base64.getEncoder().encodeToString(ReadLauncherInfos.getPassword().getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Sauvegarde de la ram
        if (ram != 0)
            jObject.putIfAbsent("ram", ram);
        else
            jObject.put("ram", ReadLauncherInfos.getRAM());

        //Sauvegarde du boolean save password
        jObject.put("savepassword", savePassword);

        if (!savePassword)
            jObject.remove("password");

        //Écriture du fichier
        try (FileWriter writer = new FileWriter(launcherFile)){
            writer.write(jObject.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sauvegarde de ram terminé !");
    }

    public static void saveLauncherInfos(int value){
        ram = value;
        saveLauncherInfos();
    }

    public static void saveLauncherInfos(String user, String pass){
        username = user;
        password = pass;
        saveLauncherInfos();
    }

    public static void saveLauncherInfos(boolean value){
        savePassword = value;
        saveLauncherInfos();
    }
}
