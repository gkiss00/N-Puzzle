package helper;
import java.io.*;
import java.util.*;
import model.*;

public class Parser {
    private String path;
    private int size;
    private int yy;

    
    public Parser(String path) {
        this.path = path;
        this.size = 0;
        this.yy = 0;
    }

    public Node parseFile() throws Exception{
        Node node = null;
        File file = new File(path);
        Scanner scan = new Scanner(file);
        boolean first = true;
        while(scan.hasNextLine()){
            String line = scan.nextLine().trim();
            if (line.length() == 0 || line.charAt(0) == '#'){
                //PASS, THIS LINE IS A COMMENT
            } else {
                if (first){
                    String[] args = line.split(" ");
                    size = Integer.parseInt(args[0].trim());
                    node = new Node(size);
                    first = false;
                } else {
                    String[] args = line.split(" ");
                    for (int i = 0; i < size; ++i){
                        node.tab[yy][i] = Integer.parseInt(args[i].trim());
                    }
                    ++yy;
                }
            }
        }
        scan.close();
        return node;
    }
}
