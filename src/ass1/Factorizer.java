package ass1;

import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Factorizer implements Runnable {
    /***
     *  This class is 'Runnable' in a thread.
     *  The program should take two integers:
     *  1. Product of two primes
     *
     *  2. Number of Concurrent Threads
     *
     *  So i think that our program should start by creating an instance of our Factorizer
      */


    private static Factorizer[] factorizers;
    private static final Object lockFactors = new Object(); //global object used as a lock in all threads
    private static boolean stop = false;
    private boolean exit = false;

    // static list with our factors
    private static BigInteger[] factors = null;
    private final static int MIN = 2;
    private final int nrOfThreadsVal;
    private final long productVal;
    private final int minVal;
    private final long maxVal; // max should probably be the input we get from the

    Factorizer(long product, int nrOfThreads, int min) {
        this.productVal = product;
        this.nrOfThreadsVal = nrOfThreads;
        this.maxVal = product;
        this.minVal = min;
    }

    static boolean alreadyHasFactors(){
        return factors != null;
    }

    public synchronized boolean isExit(){
        return exit;
    }
     public synchronized void setExit(boolean b){
       exit = b;
    }

    public void setFactors(BigInteger a, BigInteger b){
        factors = new BigInteger[]{a,b};
    }



    /**
     * This is the obligatory run method, that runs when we 'start()' the thread
     * step: should be the number of threads
     * min: the smallest number to investigate
     * max: the largest number to investigate
     */
    @Override
    public void run() {

        //since only these local variables will be modified inside run(), it will be thread safe
        BigInteger number = new BigInteger(String.valueOf(minVal)); // Vi får inte använda Wrapper-klasser till primitiva typer som synchronized.
        BigInteger product = new BigInteger(String.valueOf(productVal));
        BigInteger max = new BigInteger(String.valueOf(maxVal));
        BigInteger min = new BigInteger(String.valueOf(minVal));
        BigInteger nrOfThreads = new BigInteger(String.valueOf(nrOfThreadsVal));

        System.out.println("thread started. " + "min: " + min.toString() + " nrOfThreads: " + nrOfThreads.toString());

        while (number.compareTo(max) <= 0 ) {

            //if factors have been found and exit has been set to true, then exit the process
            if (isExit()) {
                System.out.println("exits thread: other");
                return;
            }

            if (product.remainder(number).compareTo(BigInteger.ZERO) == 0) {

                //we guard here so that not multiple threads can come in and try to set the factors simultaneously. Also meanwhile one thread is setting the factors, another thread could come here and check 'alreadyHasFactors' meanwhile we are in the process of setting them in another thread
                synchronized (lockFactors) {
                    if(alreadyHasFactors()) return;

                    // should return these two factors, and also the computation time
                    BigInteger factor1 = number;
                    BigInteger factor2 = product.divide(factor1);

                    // put factor1, factor2 in some kind of static list-variable to be accessed from the main process
                    setFactors(factor1, factor2);

                    // each factorizer sets its exit-value to true
                    for (Factorizer factorizer : factorizers) {
                        factorizer.setExit(true);
                    }

                    System.out.println("exits thread: factors found");
                    return;
                }//synchronized
            }//if
            number = number.add(nrOfThreads);
        }
    }

    public static void main(String[] args) {

        try {
            // Start timing.
            long start = System.nanoTime();

            // Get console arguments
            long product = Long.parseLong(args[0]);
            int numThreads = Integer.parseInt(args[1]);

            // create an array of threads
            Thread[] threads = new Thread[numThreads];

            //we have to create a list of factorizers here also
            factorizers = new Factorizer[numThreads];
            for (int i = 0; i < numThreads ; i++){
                factorizers[i] = new Factorizer(product, numThreads, MIN + i);
            }

            //create new threads with a seperate Factorizer object in each of them
            for (int i = 0; i < numThreads ; i++) {
                threads[i] = new Thread(factorizers[i]);
            }


            // Start the threads
            for (int i = 0; i < numThreads; i++) {
                threads[i].start();
            }

            // Collect all the threads with the join() method
            for (Thread thread : threads) {
                thread.join();
            }

            // Stop timing.
            long stop = System.nanoTime();

            // print result
            System.out.println("Execution time (seconds): " + (stop-start)/1.0E9);
            if(factors[0].equals(new BigInteger("1") ))
                System.out.println("No factorization possible");
            else if ( factors[1].equals(new BigInteger("1"))) {
                System.out.println("No factorization possible");
            }
            else {
                for (BigInteger factor : factors) {
                    System.out.println(factor.toString());
                }
            }

        }

        catch (Exception exception){
            System.out.println(exception);
        }
    }
}







