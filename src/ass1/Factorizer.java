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
     *  So i think that our program should start be starting creating an instance of our Factorized
      */

    Integer nrOfThreads;
    BigInteger product;
    BigInteger min = BigInteger.TWO;
    BigInteger max; // max should probably be the input we get from the
    //int step = nrOfThreads;

    Factorizer(BigInteger product, Integer nrOfThread) {
        this.product = product;
        this.nrOfThreads = nrOfThread;
        this.max = product;

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
            //number = number.add(step); //should be 'nrOfThreads' here
            number = number.add(BigInteger.valueOf(nrOfThreads));
        }
    }


    /**
     *
     * @param args - We use two args here: 1. Product, 2. nrOfThreads
     *             they are arguments written in the console
     */
    public static void main(String[] args) {
       //Factorizer f1 = new Factorizer();

        // Create a new instance of the runner. Remember that it should be concurrent and thread-safe
        Factorizer f1 = new Factorizer(new BigInteger(args[0]), new Integer(args[1]));


        System.out.println("hejehej" + args[0]);
    }
}







