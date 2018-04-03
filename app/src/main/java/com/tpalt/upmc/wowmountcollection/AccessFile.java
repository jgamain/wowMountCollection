package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.util.Log;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vaniisha on 01-Apr-18.
 */

public class AccessFile {
    public static final String WISH_FILE = "wish_mounts.json";

    public static void readFile(Context context,File directory){

        List <Integer> wish_mounts_ID = new ArrayList<>();
        //JSONParser parser = new JSONParser();
        try {
            File inFile = new File (directory,"wish_mounts.json");

            if(!inFile.exists()){
                try {
                    System.out.println("BOOLEAN2 :" + inFile.createNewFile());
                    return;
                }catch(IOException e){
                    e.printStackTrace();
                    System.err.println( "createNewFile");
                }
            }
            if(inFile.length() ==0){
                System.out.println("LENGTH = 0");
                return;
            }

             /*If file exits and is not empty : do stuff*/
            String filePath = directory.getPath() + "/" + WISH_FILE;
            System.out.println("OUVERTURE DU FICHIER" + filePath);
            FileReader fr = new FileReader(filePath);


            JSONObject jsonObject = WMCApplication.loadJSONFromFile(inFile);
            JSONArray wish = (JSONArray) jsonObject.get("wish");
            System.out.println("WISH DU FICHIER = " + wish.toString());

            if(jsonObject==null) {
                System.out.println("FICHIER VIDE" + filePath);
                return;
            }
            for(int i = 0; i<wish.length();i++){
                //Object iii = wish.getJSONObject(i);
                JSONObject ii = new JSONObject(wish.get(i).toString());
                Integer currentId = Integer.parseInt(ii.get("creatureId").toString());
                wish_mounts_ID.add(currentId);
                //CALL ADD MOUNT FROM INTEGER
                for(Mount m : WMCApplication.getALLMountList()){
                    if(m.getCreatureId() == currentId) {
                        if (!WMCApplication.getWishList().contains(m)) {
                            boolean add = WMCApplication.addWishList(m);
                            Log.d("ADDING", "" + add);
                            System.out.println("ADDING =" + add + "\nMonture ajoutée = " + m.toString());
                        }
                    }
                    continue;
                }
            }
            fr.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void writeFile( Mount m , Context context,File directory,boolean add) {
        try {
            File inFile = new File (directory,"wish_mounts.json");
            if(!inFile.exists()){
                try {
                    System.out.println("BOOLEAN2 :" + inFile.createNewFile());
                    return;
                }catch(IOException e){
                    e.printStackTrace();
                    System.err.println( "createNewFile");
                }
            }
            String path = directory.getPath()+ "/" + WISH_FILE;
            System.out.println("> " + path);

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            System.out.println("La taille du fichier que je vais lire = " + inFile.length());
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            JSONObject objRes = new JSONObject();
            JSONArray arrayRes = new JSONArray();

            if(add) {
                if (inFile.length() == 0) {
                    System.out.println("INPUT FILE IS EMPTY");
                    JSONObject j = new JSONObject();
                    j.put("creatureId", m.getCreatureId());
                    arrayRes.put(j);
                    objRes.put("wish", arrayRes);
                    pw.write(objRes.toString());
                    pw.flush();
                    pw.close();
                    System.out.println("Size tmp = " + tempFile.length() + "\n Contenu écrit : " + objRes.toString()) ;
                    //Delete the original file
                    if (!inFile.delete()) {
                        System.out.println("Could not delete file");
                        return;
                    }
                    //Rename the new file to the filename the original file had.
                    if (!tempFile.renameTo(inFile))
                        System.out.println("Could not rename file");
                    return;
                }
            }
            /*Modify to Generic*/
            //JSONObject res = (JSONObject) parser.parse(new FileReader(path));
            JSONObject res = WMCApplication.loadJSONFromFile(inFile);
            if (res != null) {
                System.out.println("READING FILE BEFORE ADDING ");
                JSONArray mountsArray = res.getJSONArray("wish");
                res.toString();
                if(mountsArray.length() ==0)
                    Log.d("Taille", "0");

                Log.d("Taille", "=" + mountsArray.length());
                for (int i = 0; i < mountsArray.length(); i++) {
                    JSONObject ii = new JSONObject(mountsArray.get(i).toString());
                    Integer currentId = Integer.parseInt(ii.get("creatureId").toString());
                    Log.d("FOR" , "OK");

                    /*if !add (==remove), copy ONLY mounts that not correspond to m*/
                    if (!add) {
                        if (currentId.intValue() != m.getCreatureId()) {
                            arrayRes.put(ii.toString());
                            Log.d("WRITE", "SUCCESS " + currentId);
                        }
                    } else {
                        arrayRes.put(ii.toString());
                    }
                }
                if (add) {
                    JSONObject j = new JSONObject();
                    j.put("creatureId", String.valueOf(m.getCreatureId()));
                    arrayRes.put(j.toString());
                    Log.d("WRITE", "SUCCESS ");
                }
                //Delete the original file
                if (!inFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inFile))
                    System.out.println("Could not rename file");

                Log.d("OBJ " , objRes.toString());

            }else{
                Log.d("WRITE", "NULL");
            }

            /*AJOUT DE LA NOUVELLE LISTE DES MONTURES AU FICHIER*/
            objRes.put("wish",arrayRes);
            pw.write(objRes.toString());
            pw.flush();

            pw.close();
            Log.d("FILE LENGTH" , " "+ tempFile.length());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not delete file 1");

        }
    }
}
