//package ass1;
/**
 * Axel Davidsson
 * axda2670
 */

import java.math.BigInteger;
import peteria.javaconcurrency.annotation.ThreadSafe;
import peteria.javaconcurrency.annotation.GuardedBy;

@ThreadSafe
public class Factorizer implements Runnable {
    @GuardedBy("lockFactors")
    private static Factorizer[] factorizers;
    private static final Object lockFactors = new Object(); //global object used as a lock in all threads
    @GuardedBy("this")
    private boolean exit = false;

    // static list with our factors
    @GuardedBy("lockFactors")
    private static BigInteger[] factors = null;
    private final static int MIN = 2;
    private final int nrOfThreadsVal;
    private final long productVal;
    private final int minVal;
    private final long maxVal;

    Factorizer(long product, int nrOfThreads, int min) {
        this.productVal = product;
        this.nrOfThreadsVal = nrOfThreads;
        this.maxVal = product;
        this.minVal = min;
    }

    private boolean alreadyHasFactors(){
        synchronized (lockFactors) {
            return factors != null;
        }
    }

    private synchronized boolean isExit(){
        return exit;
    }
     private synchronized void setExit(boolean b){
       exit = b;
    }


    /**
     * This is the obligatory run method, that runs when we 'start()' the thread
     * step: should be the number of threads
     * min: the smallest number to investigate
     * max: the largest number to investigate
     */
    @Override
    public void run() {

        //since these local variables will be modified inside run(), it will be thread safe
        BigInteger number = new BigInteger(String.valueOf(minVal));
        BigInteger product = new BigInteger(String.valueOf(productVal));
        BigInteger max = new BigInteger(String.valueOf(maxVal));
        BigInteger nrOfThreads = new BigInteger(String.valueOf(nrOfThreadsVal));


        while (number.compareTo(max) <= 0 ) {

            //if factors have been found so that exit has been set to true, then exit the process
            if (isExit()) {
                return;
            }

            if (product.remainder(number).compareTo(BigInteger.ZERO) == 0) {

                    if(alreadyHasFactors()) return;

                    //we guard here so that not multiple threads can come in and try to set the factors simultaneously. Also meanwhile one thread is setting the factors, another thread could come here and check 'alreadyHasFactors' meanwhile we are in the process of setting them in another thread
                    synchronized (lockFactors) {
                        // should return these two factors, and also the computation time
                        BigInteger factor1 = number;
                        BigInteger factor2 = product.divide(factor1);

                        // put factor1, factor2 in some kind of static list-variable to be accessed from the main process
                        factors = new BigInteger[]{factor1,factor2};
                    }//synchronized

                    for (Factorizer factorizer : factorizers) {
                        factorizer.setExit(true); //set exit is synchronized
                    }

                    return;

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

            // create a list of factorizers here also
            factorizers = new Factorizer[numThreads];
            for (int i = 0; i < numThreads ; i++){
                factorizers[i] = new Factorizer(product, numThreads, MIN + i);
            }

            //create new threads with a seperate Factorizer-object in each of them
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







