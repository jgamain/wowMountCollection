package com.tpalt.upmc.wowmountcollection;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by Vaniisha on 01-Apr-18.
 */

public class AccessFile {
    public static final String WISH_FILE = "wish_mounts.json";

    public static void readFile(Context context,File directory){
        JSONParser parser = new JSONParser();
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
            JSONObject jsonObject = (JSONObject)parser.parse(fr);
            JSONArray wish = (JSONArray) jsonObject.get("wish");
            if(jsonObject==null) {
                System.out.println("FICHIER VIDE" + filePath);
                return;
            }


            for(int i = 0; i<wish.length();i++){
                JSONObject ii = wish.getJSONObject(i);
                Integer currentId = Integer.parseInt(ii.get("creatureId").toString());
                for(Mount m : WMCApplication.getALLMountList()){
                    if(m.getCreatureId() == currentId){
                        boolean add = WMCApplication.addWishList(m);
                        Log.d("ADDING", ""+ add );
                        System.out.println("ADDING =" + add);
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
            JSONParser parser = new JSONParser();

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
            String path = directory.getPath()+ "/" + "wish_mounts.json";
            System.out.println("> " + path);

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            if (!inFile.exists()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            JSONObject objRes = new JSONObject();
            JSONArray arrayRes = new JSONArray();
            if(add) {
                if (inFile.length() == 0) {
                    JSONObject j = new JSONObject();
                    j.put("creatureId", m.getCreatureId());
                    arrayRes.put(j);
                    objRes.put("wish", arrayRes);
                    pw.write(objRes.toString());
                    System.out.println("LENGTH =0" + "->" + objRes.toString());
                    pw.flush();
                    pw.close();
                    return;
                }
            }
            /*Modify to Generic*/
            JSONObject res = (JSONObject) parser.parse(new FileReader(path));



            if (res != null) {
                JSONArray mountsArray = res.getJSONArray("wish");
                if(mountsArray.length() ==0)
                    Log.d("Taille", "0");
                Log.d("RES" , "NOT NULL");

                for (int i = 0; i < mountsArray.length(); i++) {
                    JSONObject ii = mountsArray.getJSONObject(i);
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

            }else{
                Log.d("WRITE", "NULL");
            }
            objRes.put("wish",arrayRes);
            pw.write(objRes.toString());
            pw.flush();

            pw.close();
            Log.d("FILE LENGTH" , " "+ tempFile.length());
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Could not delete file 1");

        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Could not delete file 2");

        } catch (JSONException e) {
            System.out.println("Could not delete file 3");

            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Could not delete file 4");

            e.printStackTrace();
        }

        catch (ParseException e){
            System.out.println("Could not delete file5");

            e.printStackTrace();
        }
    }
}
