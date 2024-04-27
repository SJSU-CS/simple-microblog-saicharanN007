package edu.sjsu.cmpe272.simpleblog.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.ini4j.Wini;

public class INIFile {

    public static void saveFile(String userid, String privateKey) throws IOException {
        String iniContent = "[User]\n" + "userid=" + userid + "\n" + "private-Key="+privateKey+"\n";
        String filePath = "mb.ini";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {bufferedWriter.write(iniContent);}
        catch (IOException e) {e.printStackTrace();}


    }

    public static String getUserId(){
        String userid = null;
        try {
            Wini ini = new Wini(new File("mb.ini"));

            userid = ini.get("User", "userid", String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userid;
    }
}