package ass1;

import java.math.BigInteger;

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

    private final static long MIN = 1;
    BigInteger nrOfThreads;
    BigInteger product;
    BigInteger min;
    BigInteger max; // max should probably be the input we get from the
    //int step = nrOfThreads;

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
        BigInteger number = min; // Vi får inte använda Wrapper-klasser som synchronized.
        while (number.compareTo(max) <= 0 ){
            if (product.remainder(number).compareTo(BigInteger.ZERO) == 0) { // om product är delbart med number

                // should return these two factors, and also the computation time
                BigInteger factor1 = number;
                BigInteger factor2 = product.divide(factor1);
                return;
            }
            //number = number.add(step); //should be 'nrOfThreads' here as steps-size
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

            // Collect the threads
            for (int i = 0; i < numThreads.intValue(); i++) {
                threads[i].join();
            }

            System.out.println("hejehej" + args[0]);

        }

        catch (Exception exception){
            System.out.println(exception);
        }
    }
}







