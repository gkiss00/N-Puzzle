package helper;
import java.util.*;
import java.util.concurrent.Semaphore;

public class TimeChecker implements Runnable {
    private long timeout;
    private long start;
    private Semaphore semaphore;

    public TimeChecker(int timeout, Semaphore semaphore){
        this.timeout = timeout * 1000;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        //start = Calendar.getInstance();
        start = System.currentTimeMillis();
        while(true){
            long now = System.currentTimeMillis();
            if (now - start > timeout) {
                try{
                    semaphore.acquire();
                    System.out.println("unsolvable in " + timeout / 1000 + " secondes");
                    System.exit(0);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            try{
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        
    }
    
}
