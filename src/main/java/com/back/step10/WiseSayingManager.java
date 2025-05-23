package com.back.step10;

import java.util.Scanner;

public class WiseSayingManager {
    WiseSayingArray wiseSayingArray;
    Scanner scanner;
    FileManager fileManager;

    WiseSayingManager(){
        scanner = new Scanner(System.in);
        System.out.println("== 명언 앱 ==");
        wiseSayingArray = new WiseSayingArray();
        fileManager = new FileManager();
    }

    private String getInput(){
        String input = scanner.nextLine().trim();
        return banSpecialLetters(input);
    }

    private String banSpecialLetters(String str){
        return str.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");
    }

    private void addWiseSaying(){
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();
        WiseSaying newWiseSaying = new WiseSaying(content, author);
        wiseSayingArray.add(newWiseSaying);
        System.out.println(newWiseSaying.getId() + "번 명언이 등록되었습니다.");
    }

    private int getNum(String num_str){
        String only_num_str;
        for (int i = 0; i< num_str.length(); i++){
            if (num_str.charAt(i)<'0' || num_str.charAt(i)>'9'){
                only_num_str = num_str.substring(0,i);
                int num = Integer.parseInt(only_num_str);
                return num;
            }
        }
        return Integer.parseInt(num_str);
    }

    private int getId(String str){
        String id_str = "id=";
        for (int i = 0 ; i< str.length(); i++){
            boolean found = false;
            for (int j = 0 ; j< id_str.length(); j++){
                if (str.charAt(i+j) != id_str.charAt(j)){
                    break;
                }
                found = true;
            }
            if (found){
                String sub_str = str.substring(i + id_str.length());
                int id = getNum(sub_str);
                return id;
            }
        }
        return WiseSaying.NULL_ID;
    }

    private void deleteWiseSaying(String str){
        int id = getId(str);
        if (id == WiseSaying.NULL_ID){
            System.out.println("ID를 입력하시오.");
            return;
        }
        boolean success = wiseSayingArray.removeContentById(id);
        if (success){
            System.out.println( id+"번 명언이 삭제되었습니다.");
        }else{
            System.out.println( id+"번 명언은 존재하지 않습니다.");
        }
    }

    private void editWiseSaying(String str){
        int id = getId(str);
        WiseSaying wiseSaying = wiseSayingArray.getContentById(id);
        if (wiseSaying == null){
            System.out.println( id+ "번 명언은 존재하지 않습니다.");
            return;
        }
        System.out.println("명언(기존) : " + wiseSaying.getContent());
        System.out.print("명언 : ");
        String content_str = getInput();
        System.out.println("작가(기존) : " + wiseSaying.getAuthor());
        System.out.print("작가 : ");
        String author = getInput();
        wiseSaying.setContent(content_str);
        wiseSaying.setAuthor(author);
    }

    private void build(){
        fileManager.saveWiseSayingsAsOneJson(wiseSayingArray.iterator(), wiseSayingArray.getContentId());
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    public void run(){
        fileManager.loadJsonsAsWiseSayings(wiseSayingArray);
        while (true){
            System.out.print("명령) ");
            String input = scanner.nextLine().trim();
            if (input.equals("종료")){
                break;
            }
            else if (input.equals("등록")){
                addWiseSaying();
            }
            else if (input.equals("목록")){
                System.out.print(wiseSayingArray);
            }else if (input.startsWith("삭제")){
                deleteWiseSaying(input);
            }else if (input.startsWith("수정")){
                editWiseSaying(input);
            }else if (input.equals("빌드")){
                build();
            }
        }
        fileManager.saveWiseSayingsAsJsons(wiseSayingArray);
        return;
    }

}
