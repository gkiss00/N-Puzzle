package model;

import helper.*;
import java.util.*;

public class Node implements Comparable {
    public Node parent;
    public int[][] tab;
    public int type;
    public int f;
    public int g;
    public int h; // manathan
    public double h2; // euclidian
    public int h3; // tiles out of place
    public int size;

    public Node(){
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.size = 0;
        this.parent = null;
    }

    public Node(int size){
        this.tab = new int[size][size];
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.size = size;
        this.parent = null;
    }

    public Node(Node node, Node goal, Direction direction, int type){
        this.size = node.size;
        this.type = type;
        this.tab = new int[node.size][node.size];
        initTab(node, direction);
        this.g = node.g + 1;
        setEuristicValue(goal, type);
        this.f = g + h;
        this.parent = node;
    }

    public void setEuristicValue(Node goal, int type){
        if (type == 1){
            this.h = new Resolver().manathan(this, goal);
        }else if (type == 2){
            this.h2 = new Resolver().euclidian(this, goal);
        }else if (type == 3){
            this.h3 = new Resolver().tile(this, goal);
        } else {
            this.h = new Resolver().manathan(this, goal);
            this.h2 = new Resolver().euclidian(this, goal);
            this.h3 = new Resolver().tile(this, goal);
        }
    }

    private void initTab(Node node, Direction d){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                this.tab[y][x] += node.tab[y][x];
            }
        }
        int x = getZeroX();
        int y = getZeroY();
        int tmp = 0;
        if (d == Direction.RIGTH){
            tmp = tab[y][x + 1];
            tab[y][x + 1] = 0;
            tab[y][x] = tmp;
        } else if (d == Direction.LEFT){
            tmp = tab[y][x - 1];
            tab[y][x - 1] = 0;
            tab[y][x] = tmp;
        } else if (d == Direction.TOP){
            tmp = tab[y - 1][x];
            tab[y - 1][x] = 0;
            tab[y][x] = tmp;
        } else {
            tmp = tab[y + 1][x];
            tab[y + 1][x] = 0;
            tab[y][x] = tmp;
        }
    }

    public void setGoal(int model){
        if(model == 1){
            setSnail();
        } else if (model == 2){
            setRow();
        }
    }

    private void setSnail(){
        int i = 0;
        int x = 0;
        int y = 0;
        int born1 = size - 1;
        int born2 = 0;
        Direction direction = Direction.RIGTH;
        while(i < size * size){
            tab[y][x] = (i + 1) % (size * size);
            if(direction == Direction.RIGTH) {
                if(x == born1){
                    direction = Direction.BOTTOM;
                    ++y;
                } else {
                    ++x;
                }
            }else if (direction == Direction.BOTTOM){
                if(y == born1){
                    direction = Direction.LEFT;
                    --x;
                } else {
                    ++y;
                }
            } else if (direction == Direction.LEFT){
                if(x == born2){
                    direction = Direction.TOP;
                    ++born2;
                    --y;
                } else {
                    --x;
                }
            }else if (direction == Direction.TOP){
                if(y == born2){
                    direction = Direction.RIGTH;
                    --born1;
                    ++x;
                } else {
                    --y;
                }
            }
            ++i;
        }
    }

    private void setRow(){
        int i = 1;
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                tab[y][x] = i % (size * size);
                ++i;
            }
        }
    }

    public int getZeroX(){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (tab[y][x] == 0)
                    return x;
            }
        }
        return 0;
    }
    public int getZeroY(){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (tab[y][x] == 0)
                    return y;
            }
        }
        return 0;
    }

    private int maxSize(){
        int nb = (size * size) - 1;
        int tmp = 1;

        while(nb / 10 != 0){
            nb /= 10;
            ++tmp;
        }

        return tmp;
    }

    private int intSize(int nb){
        int tmp = 1;

        while(nb / 10 != 0){
            nb /= 10;
            ++tmp;
        }
        return tmp;
    }

    private String fillStr(int i){
        String str = "";
        int maxSize = maxSize();
        int intSize = intSize(i);
        while(maxSize > intSize){
            str += " ";
            --maxSize;
        }
        str += i;

        return str;

    }

    @Override
    public String toString(){
        String str = "";
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                str += fillStr(tab[y][x]) + " ";
            }
            str += "\n";
        }
        return str;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o; 
        return compareWith(node) == 0 ? (true) : (false);
    }

    public int compareWith(Node node){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (this.tab[y][x] != node.tab[y][x])
                    return 1;
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        if (o == this) {
            return -1;
        }
        if (!(o instanceof Node)) {
            return -1;
        }
        Node node = (Node) o;
        if (type == 1){
            return this.h - node.h;
        }else if (type == 2){
            return (int)(this.h2 - node.h2);
        }else if (type == 3){
            return this.h3 - node.h3;
        } else {
            Random rand = new Random();
            int r = rand.nextInt(3);
            switch(r){
                case 1:
                    return this.h - node.h;
                case 2:
                    return (int)(this.h2 - node.h2);
                case 3:
                    return this.h3 - node.h3;
            }
        }
        return this.h - node.h;
    }
}
