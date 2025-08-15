package com.training.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Duration;

public class Lec01ThreadInterrupt{
    private static final Logger log = LoggerFactory.getLogger(Lec01ThreadInterrupt.class);

    public static void main(String[] args) {
        demo1();
    }

    private static void demo1() {
        Thread thread = new Thread(new Waiter());

        thread.start();
        // al llamar interrupt en un hilo que ejecuta un metodo que arroja InterruptedException
        // este lo termina
        thread.interrupt();
    }

    private static void demo2() {
        Thread thread = new Thread(
                new LongComputationTask(new BigInteger("200000"), new BigInteger("1000000"))
        );

        thread.start();
        thread.interrupt();
    }

    private static class Waiter implements Runnable {
        @Override
        public void run() {
            //try {
//                for (int i = 0; i < 10; i++) {
//                    log.info("{}", i);
//                    Thread.sleep(Duration.ofSeconds(1));
//                }
                int i = 0;
                while (true) {
                    try {
                        log.info("{}", i++);
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        log.info("Exiting blocking thread");
                        // si no ponemos un return nunca se completara el hilo
                        return;
                    }
                }
//            } catch (InterruptedException e) {
//                log.info("Exiting blocking thread");
//            }
        }
    }

    private static class LongComputationTask implements Runnable {
        private final BigInteger base;
        private final BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            log.info("{}^{} = {}", base, power, pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0 ; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    log.info("Prematurely Interrupted Signal");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
