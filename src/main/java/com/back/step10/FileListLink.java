package com.back.step10;

import java.io.File;

/**
 * 영어로 적는거 너무 귀찮으니 한글로 작성할게요. <br>
 * 파일이 1, 10, 11, 12, 13, 14, 15, 2, 20,... 순서대로 입력받는데 <br>
 * 이걸 전부 배열에 넣고 퀵 소트 돌리기엔 이미 어느정도 정렬이 되어있다고 생각했어요. <br>
 * 그냥 생각한건 한 자릿수 파일들, 두 자릿수 파일들을 분리해서 입력받고 출력할 때는 한 자릿수 파일들 > 두 자릿수 파일들로<br>
 * 순서대로 출력하면 될거 같았어요. 그런데 파일의 개수를 확정할 수 없더라구요. 그래서 리스트 링크 방식을 채택했습니다.
 */
public class FileListLink {
    FileLinkNode root;
    FileLinkNode tail;
    int max_depth;
    FileLinkNode recentNode;

    private String getNumberStringFromFile(File file){
        String name = file.getName();
        if (name.endsWith(".json")){
            return name.substring(0, name.length()-5);
        }
        return "";
    }

    public FileListLink(){
        root = new FileLinkNode(1);
        tail = root;
        max_depth = 1;
    }

    private void addLink(int depth){
        FileLinkNode newNode = new FileLinkNode(depth);
        max_depth = depth;
        tail.next = newNode;
        tail = newNode;
    }

    private FileLinkNode insertLink(FileLinkNode node, int depth){
        FileLinkNode tmp = node.next;
        FileLinkNode newNode = new FileLinkNode(depth);
        node.next = newNode;
        newNode.next = tmp;
        return newNode;
    }

    private FileLinkNode findLink(int depth){
        FileLinkNode current;
        if (recentNode != null && recentNode.depth <= depth ) {
            current = recentNode;
        }else{
            current = root;
        }
        while (current.next != null){
            if (current.depth == depth){
                return current;
            }
            if (current.depth < depth && depth <current.next.depth){
                return insertLink(current, depth);
            }
            current = current.next;
        }
        if (depth == current.depth){
            return current;
        }
        return null;
    }

    public void addFile(File file){
        String number_str = getNumberStringFromFile(file);
        int len = number_str.length();
        if (len > max_depth){
            addLink(number_str.length());
            tail.addFile(file);
            return;
        }
        FileLinkNode node = findLink(len);
        if (node == null){
            System.out.println("File link not found");
            return;
        }
        node.addFile(file);
        recentNode = node;

    }

    public FileIterator iterator(){
        return new FileIterator(root);
    }

    public static class FileIterator{
        private FileLinkNode currentLink;
        private FileNode currentNode;

        FileIterator(FileLinkNode root){
            currentLink = root;
            currentNode = root.file_root;
        }

        public boolean hasNext(){
            if (currentNode == null){
                return false;
            }
            return true;
        }

        private void goNext(){
            if (currentNode.next != null){
                currentNode = currentNode.next;
                return;
            }
            if (currentLink.next != null){
                currentLink = currentLink.next;
                currentNode = currentLink.file_root;
                return;
            }
            currentLink = null;
            currentNode = null;
        }

        public File next(){
            File file = currentNode.file;
            if (hasNext()){
                goNext();
            }
            return file;
        }
    }
    private static class FileNode{
        private File file;
        private FileNode next;

        FileNode(File file){
            this.file = file;
            next = null;
        }

        public boolean hasNext(){
            return next != null;
        }
    }

    private static class FileLinkNode{
        private FileNode file_root;
        private FileNode file_tail;
        private FileLinkNode next;
        private int depth;

        FileLinkNode(int depth){
            file_root = null;
            file_tail = null;
            this.depth = depth;
            next = null;
        }

        public void addFile(File file){
            // no need to check is full
            FileNode newNode = new FileNode(file);
            if (file_root == null){
                file_root = newNode;
                file_tail = newNode;
            }else{
                file_tail.next = newNode;
                file_tail = newNode;
            }
        }

        public boolean hasNext(){
            return next != null;
        }
    }
}
