package com.back.step10;

import java.io.*;

public class FileManager {
    final static String JSON_FOLDER = "./db/wiseSaying/";
    final static String FINAL_ID_FILE = "./db/wiseSaying/lastId.txt";
    final static String JSON_FILE = "./db/wiseSaying/data.json";

    JsonManager jsonManager;

    FileManager() {
        jsonManager = new JsonManager();
    }

    /**
     * save one wiseSaying as json file
     * @param wiseSaying
     * @throws IOException
     */
    public void saveWiseSayingAsJson(WiseSaying wiseSaying)  {
        try(FileOutputStream output = new FileOutputStream(JSON_FOLDER + wiseSaying.getId() +".json");){
            output.write(jsonManager.WiseSaying2json(wiseSaying).getBytes());
        }catch (IOException e){
            System.out.println("error while saving wiseSaying");
        }
    }

    /**
     * save many wiseSayings as many json files and last id too
     * @param array
     */
    public void saveWiseSayingsAsJsons(WiseSayingArray array){
        WiseSayingArray.WiseSayingIterator iterator = array.iterator();
        int id = array.getWiseSayingId();
        // saves many wise sayings
        while (iterator.hasNext()) {
            WiseSaying wiseSaying = iterator.next();
                saveWiseSayingAsJson(wiseSaying);
        }
        // saves last id
        saveLastId(id);
        System.out.println("Error saving content");
    }

    /**
     * save wise sayings to data.json file
     * @param iterator
     */
    public void saveWiseSayingsAsOneJson(WiseSayingArray.WiseSayingIterator iterator){
        try{
            try(FileOutputStream output = new FileOutputStream(JSON_FILE);){
                output.write(jsonManager.wiseSaying2json(iterator).getBytes());
            }
        } catch (IOException e) {
            System.out.println("Error saving wise saying");
        }
    }

    /**
     * save last id to txt file
     * @param id
     * @throws IOException
     */
    public void saveLastId(int id) {
        try(FileOutputStream output = new FileOutputStream(FINAL_ID_FILE);){
            output.write(Integer.toString(id).getBytes());
        }catch (IOException e){
            System.out.println("Error saving last id");
        }
    }

    public Boolean deleteWiseSayingJson(int id) {
        File file = new File(JSON_FOLDER + id + ".json");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * load last id and return
     * @return
     * @throws IOException
     */
    public int loadLastId() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(FINAL_ID_FILE));
        StringBuilder sb = new StringBuilder();
        // there won't be '\n' in this file
        sb.append(br.readLine());
        br.close();
        return Integer.parseInt(sb.toString());
    }

    /**
     * load one json and make wise saying then return
     * @param fileName
     * @return
     * @throws IOException
     */
    public WiseSaying loadJsonAsWiseSaying(String fileName) throws IOException  {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine());
        }
        br.close();
        return jsonManager.json2wiseSaying(sb.toString());
    }

    /**
     * load many json files (except data.json) and make many wise saying, also load last id too
     * @param array
     */
    public void loadJsonsAsWiseSayings(WiseSayingArray array) {
        File dir = new File(JSON_FOLDER);
        File id_file = new File(FINAL_ID_FILE);
        File[] files = dir.listFiles();
        FileListLink fileListLink = new FileListLink();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith("data.json")) {
                continue;
            }
            if (fileName.endsWith(".json")) {
                fileListLink.addFile(file);
            }
        }
        FileListLink.FileIterator iterator = fileListLink.iterator();

        try{
            // load all json file but data.json
            while (iterator.hasNext()) {
                File file = iterator.next();
                String fileName = file.getPath();
                WiseSaying wiseSaying = loadJsonAsWiseSaying(fileName);
                array.add(wiseSaying);
            }
            // load last id
            if (id_file.exists()) {
                int id=loadLastId();
                array.setWiseSayingId(id);
            }

        }catch (IOException e){
            System.out.println("Error loading jsons");
        }
    }
}
