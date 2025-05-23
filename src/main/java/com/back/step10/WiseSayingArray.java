package com.back.step10;

public class WiseSayingArray {

    private WiseSaying[] wiseSayings = null;
    final static private int DEFAULT_SIZE = 10;
    private int current_size;
    private int current_index;
    private int wise_saying_id = 0;

    WiseSayingArray() {
        wiseSayings = new WiseSaying[DEFAULT_SIZE];
        current_size = DEFAULT_SIZE;
        current_index = 0;
    }

    // getter
    public int getWiseSayingId(){
        return wise_saying_id;
    }

    private int getWiseSayingIndexByID(int id){
        int index = -1;
        for (int i = 0; i < current_index; i++){
            if (wiseSayings[i].getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    public WiseSaying getWiseSayingById(int id){
        int index = getWiseSayingIndexByID(id);
        if (index == -1){
            return null;
        }
        return wiseSayings[index];
    }

    //setter

    public void setWiseSayingId(int id){
        wise_saying_id = id;
    }

    /**
     * when adding wiseSaying, id is given by this
     * @param wiseSaying
     */
    public void add(WiseSaying wiseSaying) {
        if (current_index == current_size) {
            WiseSaying[] pre_wiseSayings = wiseSayings;
            // 두 배
            int new_size = current_size << 1;
            wiseSayings = new WiseSaying[new_size];
            System.arraycopy(pre_wiseSayings, 0, wiseSayings, 0, current_size);
            current_size = new_size;

        }
        // must give id to only new made wiseSaying
        if (wiseSaying.getId() == 0){
            wiseSaying.setID(++wise_saying_id);
        }
        wiseSayings[current_index++] = wiseSaying;
    }

    public int getCurrentIndex(){
        return current_index;
    }

    /**
     * make string to show all the items
     * @return
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("번호 / 작가 / 명언\n");
        str.append("----------------------\n");
        for (int i = current_index -1; i>=0; i--){
            str.append(wiseSayings[i].getId());
            str.append(" / ");
            str.append(wiseSayings[i].getAuthor());
            str.append(" / ");
            str.append(wiseSayings[i].getContent());
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * removes an item of array and shift all items next to it
     * @param id
     * @return shows success or fail on remove
     */
    public boolean removeWiseSayingById(int id){
        int index = getWiseSayingIndexByID(id);
        if (index == -1){
            return false;
        }
        for (int i = index; i< current_index-1;i++){
            wiseSayings[i] = wiseSayings[i+1];
        }
        wiseSayings[current_index-1] = null;
        current_index--;
        return true;
    }



    /**
     * returns iterator
     * @return
     */
    public WiseSayingIterator iterator(){
        return new WiseSayingIterator(0, current_index-1, wiseSayings);
    }

    /**
     * returning wiseSayings is dangerous because there's null value in there,<br>
     * so I made this not to point null value
     *
     */
    static public class WiseSayingIterator {
        private int end_index;
        private int current_index;
        private WiseSaying[] wiseSayings;

        WiseSayingIterator(int start_index, int end_index, WiseSaying[] wiseSayings){
            this.end_index = end_index;
            current_index = start_index;
            this.wiseSayings = wiseSayings;
        }

        /**
         * this will prevent to point null value
         * @return
         */
        public Boolean hasNext(){
            return current_index < end_index + 1;
        }

        public WiseSaying next(){
            if (!hasNext()){
                return null;
            }
            return wiseSayings[current_index++];
        }

    }
}
