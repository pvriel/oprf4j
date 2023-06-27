package com.github.pvriel.oprf4j;

import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;
import com.github.pvriel.oprf4j.oprf.OPRFProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractOPRFTest {

    protected abstract Triple<Integer, Pair<BigInteger[], BigInteger[]>, Pair<OPRFProvider, OPRFEvaluator>> setup();

    @Test
    @DisplayName("Check if the OPRF function works as expected.")
    public void test() throws IOException, InterruptedException {
        Triple<Integer, Pair<BigInteger[], BigInteger[]>, Pair<OPRFProvider, OPRFEvaluator>> setup = setup();
        int bitLength = setup.getLeft();
        Pair<BigInteger[], BigInteger[]> inputsAndExpectedOutputs = setup.getMiddle();
        BigInteger[] inputs = inputsAndExpectedOutputs.getLeft();
        BigInteger[] expectedOutputs = inputsAndExpectedOutputs.getRight();
        Pair<OPRFProvider, OPRFEvaluator> oprfProviderAndEvaluator = setup.getRight();
        OPRFProvider oprfProvider = oprfProviderAndEvaluator.getLeft();
        OPRFEvaluator oprfEvaluator = oprfProviderAndEvaluator.getRight();

        PipedInputStream inOne = new PipedInputStream(100_000);
        PipedOutputStream outTwo = new PipedOutputStream(inOne);
        PipedInputStream inTwo = new PipedInputStream(100_000);
        PipedOutputStream outOne = new PipedOutputStream(inTwo);

        System.out.println("Starting test with " + inputs.length + " inputs of bit length " + bitLength + "...");
        Thread providerThread = new Thread(() -> {
            try {
                oprfProvider.execute(inputs.length, bitLength, inOne, outOne);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        providerThread.start();

        BigInteger[] outputs = oprfEvaluator.evaluate(inputs, bitLength, inTwo, outTwo);
        providerThread.join();

        assertEquals(inputs.length, outputs.length);
        for (int i = 0; i < inputs.length; i++) {
            assertEquals(expectedOutputs[i], outputs[i]);
        }
    }
}
