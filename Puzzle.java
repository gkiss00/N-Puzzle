import model.*;
import helper.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Puzzle {
    private static int size;
    private static int heuristic; // 1: manathan 2: euclidian 3: tiles out of place
    private static int type = 1; // 1: greedy 2: uniform
    private static int model = 1; // 1: snail 2: row
    private static boolean isTime = false;
    private static int timeout = -1; // seconds
    private static long startTime;
    private static long endTime;
    private static Node goal;
    private static int sizeComplexity = 0;
    private static int timeComplexity = 1;
    private static List<Node> closedList = new ArrayList<>();
    private static PriorityQueue<Node> openList = new PriorityQueue<>();
    private static Semaphore semaphore = new Semaphore(1);

    private static void updateSizeComplexity(){
        if(openList.size() + closedList.size() > sizeComplexity)
            sizeComplexity = openList.size() + closedList.size();
    }

    private static void outputTime(){
        System.out.println("Taken time     : " + 
        (endTime - startTime) / 1000 +
        " secondes and " +
        (endTime - startTime) % 1000 +
        " millisecondes");
    }

    private static void outputSequence(Node node){
        List<String> l = new ArrayList<>();
        while(node != null){
            l.add(node.toString());
            node = node.parent;
        }
        for (int i = l.size() -1; i >= 0; --i){
            System.out.println(l.get(i));
        }
    }

    private static void end(Node end){
        try {
            semaphore.acquire();
            System.out.println("Final state reached");
            outputTime();
            System.out.println("Time complexity: " + timeComplexity);
            System.out.println("Size complexity: " + sizeComplexity);
            System.out.println("Nb move:         " + end.g);
            System.out.println("Squence:         ");
            outputSequence(end);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        System.exit(0);
    }

    private static Node getSmallerNode(){
        Node node = openList.poll();
        return node;
    }

    private static List<Direction> getDirections(Node node){
        int x = node.getZeroX();
        int y = node.getZeroY();
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

    private static void addNodeToList(Node node){
        if(!closedList.contains(node)){
            openList.add(node);
        }
    }

    private static void getNext(Node node){
        List<Direction> directions = getDirections(node);
        for (int i = 0; i < directions.size(); ++i){
            Node tmp = new Node(node, goal, directions.get(i), heuristic);
            addNodeToList(tmp);
        }
    }
    
    private static void resolve(){
        boolean solvable = false;
        startTime = System.currentTimeMillis();
        if(isTime){
            Thread tc = new Thread(new TimeChecker(timeout, semaphore));
            tc.start();
        }
        while(openList.size() != 0){
            Node node = getSmallerNode();
            closedList.add(node);
            updateSizeComplexity();
            ++timeComplexity;
            if(node.compareWith(goal) == 0){
                endTime = System.currentTimeMillis();
                end(node);
                solvable = true;
                break;
            } else {
                if(type == 2)
                    openList.clear();
                getNext(node);
            }
        }
        if(!solvable){
            System.out.println("The puzzle can't be solved");
        }
    }

    private static Node parseFile(String[]args) throws Exception{
        if (args.length != 2 && args.length != 3 && args.length != 4 && args.length != 5){
            throw new Exception("The programe need two args: <File> <heuristic> <type> <model> <timeout>\n" +
                                "\theuristic:\n" +
                                "\t     1: manathan\n" +
                                "\t     2: euclidian\n" +
                                "\t     3: tiles out of place\n" +
                                "\t     4: random\n" +
                                "\ttype:\n" +
                                "\t     1: greedy\n" +
                                "\t     2: uniform\n" +
                                "\tmodel:\n" +
                                "\t     1: snail\n" +
                                "\t     2: row\n" +
                                "\ttimeout:\n" +
                                "\t     seconds\n"
                                );
        } else {
            heuristic = Integer.parseInt(args[1]);
            if (heuristic > 4 || heuristic < 1)
                throw new Exception("Bad heuristic value");
            if (args.length == 3 || args.length == 4 || args.length == 5){
                type = Integer.parseInt(args[2]);
                if (type > 2 || type < 1)
                    throw new Exception("Bad type value");
            }
            if (args.length == 4 || args.length == 5){
                model = Integer.parseInt(args[3]);
                if (model > 2 || model < 1)
                    throw new Exception("Bad model value");
            }
            if (args.length == 5){
                isTime = true;
                timeout = Integer.parseInt(args[4]);
                if (timeout < 1)
                    throw new Exception("Bad timeout value");
            }
            Parser parser = new Parser(args[0]);
            Node node = parser.parseFile();
            size = node.size;
            return node;
        }
    }
    public static void main(String[]args){
        try {
            //Set the starting node
            Node start = parseFile(args);
            openList.add(start);
            //Set the goal node
            goal = new Node(size);
            goal.type = heuristic;
            goal.setGoal(model);
            start.setEuristicValue(goal, heuristic);
            //start.h = new Resolver().manathan(start, goal);
            start.f = start.h;
            //Resolve the puzzle
            resolve();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}