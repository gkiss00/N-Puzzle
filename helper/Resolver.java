package helper;
import model.*;

public class Resolver {

    public int manathan(Node node, Node goal){
        int res = 0;
        for (int y1 = 0; y1 < node.size; ++y1){
            for (int x1 = 0; x1 < node.size; ++x1){
                int actual = node.tab[y1][x1];
                for (int y2 = 0; y2 < node.size; ++y2){
                    for (int x2 = 0; x2 < node.size; ++x2){
                        if (actual == goal.tab[y2][x2]) {
                            res += (Math.abs(x2 - x1) + Math.abs(y2 - y1));
                        }
                    }
                }
            }
        }
        return res;
    }

    public double euclidian(Node node, Node goal){
        double res = 0;
        for (int y1 = 0; y1 < node.size; ++y1){
            for (int x1 = 0; x1 < node.size; ++x1){
                int actual = node.tab[y1][x1];
                for (int y2 = 0; y2 < node.size; ++y2){
                    for (int x2 = 0; x2 < node.size; ++x2){
                        if (actual == goal.tab[y2][x2]) {
                            res += Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                        }
                    }
                }
            }
        }
        return res;
    }

    public int tile(Node node, Node goal){
        int res = 0;
        for (int y1 = 0; y1 < node.size; ++y1){
            for (int x1 = 0; x1 < node.size; ++x1){
                int actual = node.tab[y1][x1];
                if (actual != goal.tab[y1][x1])
                    ++res;
            }
        }
        return res;
    }
}
