package ass1;

import java.math.BigInteger;

public class MyTest implements Runnable{


    @Override
    public void run() {

    }

    public static void main(String[] args) {
        BigInteger[] factors = new BigInteger[2];
        BigInteger[] factors2 = {new BigInteger("1"), new BigInteger("2")};
        BigInteger[] factors3 = null;
        factors3 = new BigInteger[]{new BigInteger("3"), new BigInteger("4")};
        System.out.println(factors2);

        for (int i = 0; i < factors3.length; i++) {
            System.out.println(factors3[i].toString());
        }
    }
}


