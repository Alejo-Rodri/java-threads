package com.training.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Duration;

public class Lec04ComplexCalculation {
    private static final Logger log = LoggerFactory.getLogger(Lec04ComplexCalculation.class);

    public static void main(String[] args) throws InterruptedException {
        ComplexCalculation complexCalculation = new ComplexCalculation();
        var result = complexCalculation.calculateResult(
                new BigInteger("2"),
                new BigInteger("3"),
                new BigInteger("4"),
                new BigInteger("5")
        );

        log.info("result: {}", result);
    }

    private static class ComplexCalculation {
        public BigInteger calculateResult(
                BigInteger base1,
                BigInteger power1,
                BigInteger base2,
                BigInteger power2
        ) throws InterruptedException {
            BigInteger result = BigInteger.ZERO;

            PowerCalculatingThread op1 = new PowerCalculatingThread(base1, power1);
            PowerCalculatingThread op2 = new PowerCalculatingThread(base2, power2);

            op1.start();
            op1.join(Duration.ofSeconds(3));
            op2.start();
            op2.join(Duration.ofSeconds(3));

            result = result.add(op1.getResult()).add(op2.getResult());
            return result;
        }

        private static class PowerCalculatingThread extends Thread {
            private BigInteger result = BigInteger.ONE;
            private final BigInteger base;
            private final BigInteger power;

            public PowerCalculatingThread(BigInteger base, BigInteger power) {
                this.base = base;
                this.power = power;
            }

            @Override
            public void run() {
                result = base.pow(power.intValue());
            }

            public BigInteger getResult() { return result; }
        }
    }
}
