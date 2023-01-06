package com.github.pvriel.oprf4j;

import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;
import com.github.pvriel.oprf4j.oprf.OPRFProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractOPRFTest {

    protected abstract List<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> setup();

    @Test
    public void test() throws IOException, InterruptedException {
        for (var triple : setup()) {
            var pair = triple.getLeft();
            var bitLengthProvider = pair.getLeft().getLeft();
            var provider = pair.getLeft().getRight();
            var bitLengthEvaluator = pair.getRight().getLeft();
            var evaluator = pair.getRight().getRight();
            var input = triple.getMiddle();
            var expectedOutput = triple.getRight();

            PipedInputStream inOne = new PipedInputStream(10000);
            PipedInputStream inTwo = new PipedInputStream(10000);
            PipedOutputStream outOne = new PipedOutputStream(inTwo);
            PipedOutputStream outTwo = new PipedOutputStream(inOne);

            Thread providerThread = new Thread(() -> {
                try {
                    provider.execute(bitLengthProvider, inOne, outOne);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            providerThread.start();
            var output = evaluator.evaluate(input, bitLengthEvaluator, inTwo, outTwo);
            providerThread.join();

            assertEquals(expectedOutput, output);
        }
    }
}
