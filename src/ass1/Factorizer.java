package ass1;

import java.math.BigInteger;
import java.util.ArrayList;

public class Factorizer implements Runnable {
    /***
     *  This class is 'Runnable in a thread
     *  The program should take two integers:
     *  1. Product of two primes
     *
     *  2. Number of Concurrent Threads
     *
     *  So i think that our program should start by creating an instance of our Factorizer
      */

    //static flag to exit our processes when factors are found
    // "volatile boolean flag"
    private static boolean exit = false;
    // static list with our factors
    private static BigInteger[] factors = null;
    private final static long MIN = 2;
    BigInteger nrOfThreads;
    BigInteger product;
    BigInteger min;
    BigInteger max; // max should probably be the input we get from the

    Factorizer(BigInteger product, BigInteger nrOfThread, long min) {
        this.product = product;
        this.nrOfThreads = nrOfThread;
        this.max = product;
        this.min = new BigInteger(String.valueOf(min));

    }


    /**
     * This is the obligatory run method, that runs when we 'start()' the thread
     * step: should be the number of threads
     * min: the smallest number to investigate
     * max: the largest number to investigate
     */


    @Override
    public void run() {

        System.out.println("thread started");

        BigInteger number = min; // Vi får inte använda Wrapper-klasser som synchronized.
        while (number.compareTo(max) <= 0 ){

            //if factors have been found and exit has been set to true, then exit the process
            if(exit) {
                return;
            }

            if (product.remainder(number).compareTo(BigInteger.ZERO) == 0) { // om product är delbart med number


                // should return these two factors, and also the computation time
                BigInteger factor1 = number;
                BigInteger factor2 = product.divide(factor1);

                // put factor1, factor2 in some kind of static list-variable to be accessed from the main process
                factors = new BigInteger[]{factor1, factor2};

                //set exit to true
                exit = true;

                return;
            }
            //number = number.add(stcdep); //should be 'nrOfThreads' here as steps-size
            number = number.add(nrOfThreads);
        }
    }


    /**
     *
     * @param args - We use two args here: 1. Product, 2. nrOfThreads
     *             they are arguments written in the console
     */
    public static void main(String[] args) {

        try {
            // Start timing.
            long start = System.nanoTime();


            // Get console arguments
            BigInteger product = new BigInteger(args[0]);
            BigInteger numThreads = new BigInteger(args[1]);

            // create an array of threads
            Thread[] threads = new Thread[numThreads.intValue()];

            //create new threads with a seperate Factorizer object in each of them
            for (int i = 0; i < numThreads.intValue() ; i++) {
                threads[i] = new Thread(new Factorizer(product, numThreads, MIN + i ));
            }

            // Start the threads
            for (int i = 0; i < numThreads.intValue(); i++) {
                threads[i].start();
            }


            // Instead of an empty while loop we could collect all the threads with the join() method
            for (int i = 0; i < threads.length ; i++) {
                threads[i].join();
            }

            // Stop timing.
            long stop = System.nanoTime();

            System.out.println("Execution time (seconds): " + (stop-start)/1.0E9);

            for (int i = 0; i < factors.length; i++) {
                System.out.println(factors[i].toString());
            }

        }

        catch (Exception exception){
            System.out.println(exception);
        }
    }
}







