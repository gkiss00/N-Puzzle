
import model.*;
import java.util.*;
import java.io.*;

public class Generator {
    private static int size;
    private static int moves;
    private static int model;
    private static int[][] tab;

    private static void setSnail(){
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

    private static void setRow(){
        int i = 1;
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                tab[y][x] = i % (size * size);
                ++i;
            }
        }
    }

    private static void setGoal(){
        if(model == 1){
            setSnail();
        } else if (model == 2){
            setRow();
        }
    }

    private static int getX(){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (tab[y][x] == 0)
                    return x;
            }
        }
        return 0;
    }
    private static int getY(){
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                if (tab[y][x] == 0)
                    return y;
            }
        }
        return 0;
    }

    private static List<Direction> getDirections(){
        int x = getX();
        int y = getY();
        List<Direction> directions = new ArrayList<>();
        if(x != 0 && x != size - 1){
            directions.add(Direction.LEFT);
            directions.add(Direction.RIGTH);
        } else if(x == 0){
            directions.add(Direction.RIGTH);
        } else {
            directions.add(Direction.LEFT);
        }
        if(y != 0 && y != size - 1){
            directions.add(Direction.TOP);
            directions.add(Direction.BOTTOM);
        } else if(y == 0){
            directions.add(Direction.BOTTOM);
        } else {
            directions.add(Direction.TOP);
        }
        return directions;
    }

    private static void moveTiles(){
        Random rand = new Random();
        for (int i = 0; i < moves; ++i){
            int x = getX();
            int y = getY();
            int tmp = 0;
            List<Direction> list = getDirections();
            Direction d = list.get(rand.nextInt(list.size()));
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
    }

    private static void writeFile() throws Exception{
        File output = new File("puzzle.txt");
        FileWriter w = new FileWriter("puzzle.txt");
        w.write(size + "\n");
        for (int y = 0; y < size; ++y){
            for (int x = 0; x < size; ++x){
                w.write(tab[y][x] + " ");
            }
            w.write("\n");
        }
        w.close();
    }
    public static void main(String[]args){
        try {
            size = Integer.parseInt(args[0]);
            moves = Integer.parseInt(args[1]);
            model = Integer.parseInt(args[2]);
            tab = new int[size][size];
            setGoal();
            moveTiles();
            writeFile();
        } catch (Exception e){
            System.out.println("args: <size> <moves>");
        }
        
    }
}
