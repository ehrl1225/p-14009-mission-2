package com.back.step10;

public class JsonManager {
    private String str;
    private int index;

    /**
     * makes one WiseSaying to json String
     * @param wiseSaying
     * @return
     */
    public String WiseSaying2json(WiseSaying wiseSaying) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("    \"id\": ");
        json.append(wiseSaying.getId());
        json.append(",\n");
        json.append("    \"content\": \"");
        json.append(wiseSaying.getContent());
        json.append("\",\n");
        json.append("    \"author\": \"");
        json.append(wiseSaying.getAuthor());
        json.append("\"\n}");
        return json.toString();
    }

    /**
     * makes many WiseSayings to json string
     * @param iterator
     * @return
     */
    public String wiseSaying2json(WiseSayingArray.WiseSayingIterator iterator){
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        while(iterator.hasNext()){
            // makes indent spaces
            String content_json = WiseSaying2json(iterator.next());
            String[] lines = content_json.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                json.append("    ");
                json.append(line);
                // last one need "," before "\n"
                if (i < lines.length - 1) {
                    json.append("\n");
                }
            }
            if (iterator.hasNext()) {
                json.append(",\n");
            }
        }
        json.append("\n]");
        return json.toString();
    }

    /**
     * read all strings except char '\"'
     * @return
     */
    private String readString(){
        String answer;
        for (int i = index+1 ; i < str.length() ; i++){
            // end of string
            if (str.charAt(i) == '\"'){
                answer = str.substring(index+1, i);
                index = i;
                return answer;
            }
        }
        return "";
    }

    /**
     * read all integer string and return integer
     * @return
     */
    private int readInteger(){
        for (int i = index ; i < str.length() ; i++){
            if (str.charAt(i)< '0' || str.charAt(i)>'9'){
                String integer_str = str.substring(index,i);
                index = i;
                return Integer.parseInt(integer_str);
            }
        }
        // every string is integer
        return Integer.parseInt(str);
    }

    /**
     * find char '\"' and move this.index to that index
     */
    private void findString(){
        for (int i = index ; i < str.length() ; i++){
            if (str.charAt(i)=='\"'){
                index = i;
                return;
            }
        }
    }

    /**
     *find integer and move this.index to that index
     */
    private void findInteger(){
        for (int i = index ; i < str.length() ; i++){
            if (str.charAt(i)>='0' && str.charAt(i)<='9'){
                index = i;
                return;
            }
        }
    }

    /**
     * find char and move this.index to that index
     * @param c
     * @return shows success or fail to finding that char
     */
    private boolean findChar( char c){
        for (int i = index ; i < str.length() ; i++){
            if (str.charAt(i)==c){
                index = i;
                return true;
            }
        }
        return false;
    }

    /**
     * following the format, get information from json string<br>
     * if the string didn't followed the format, then this will return null<br>
     *
     * format is : <br>
     * {<br>
     * &nbsp&nbsp "id" : number,<br>
     * &nbsp&nbsp "content" : "string",<br>
     * &nbsp&nbsp "author" : "string"<br>
     * }<br>
     * @return
     */
    public WiseSaying readWiseSaying(){
        String retval;
        findChar('{');
        findString();
        retval = readString();
        if (!retval.equals("id")){
            return null;
        }
        findChar(':');
        findInteger();
        int id = readInteger();
        findChar(',');
        findString();
        retval = readString();
        if (!retval.equals("content")){
            return null;
        }
        findChar(':');
        findString();
        String content_str = readString();
        findChar(',');
        findString();
        retval = readString();
        if (!retval.equals("author")){
            return null;
        }
        findChar(':');
        findString();
        String author_str = readString();
        findChar('}');
        WiseSaying wiseSaying = new WiseSaying(content_str, author_str);
        wiseSaying.setID(id);
        return wiseSaying;
    }

    /**
     * get one wiseSaying from json string
     * @param str
     * @return
     */
    public WiseSaying json2wiseSaying(String str){
        index = 0;
        this.str = str;
        return readWiseSaying();
    }

    /**
     * I made this but didn't use....<br>
     * format is :<br>
     * [<br>
     * &nbsp&nbsp {<br>
     * &nbsp&nbsp&nbsp&nbsp "id" : number,<br>
     * &nbsp&nbsp&nbsp&nbsp "content" : "string",<br>
     * &nbsp&nbsp&nbsp&nbsp "author" : "string"<br>
     * &nbsp&nbsp },<br>
     * &nbsp&nbsp ... <br>
     * ]<br>
     * @param str total json text
     * @param array will save into
     */
    public void json2wiseSayings(String str, WiseSayingArray array){
        index = 0;
        this.str = str;
        findChar('[');
        findChar('{');
        do {
            WiseSaying wiseSaying = readWiseSaying();
            array.add(wiseSaying);
        } while (findChar(','));
        findChar(']');
    }
}
