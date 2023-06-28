package com.github.pvriel.oprf4j.nroprf.semihonest.KALES19;

import com.github.pvriel.mpcutils4j.array.ArrayUtils;
import com.github.pvriel.mpcutils4j.stream.StreamUtils;
import com.github.pvriel.oblivioustransfer4j.rrot.RandomObliviousTransferReceiver;
import com.github.pvriel.oprf4j.nroprf.NROPRFEvaluator;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFEvaluator;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.IntStream;

/**
 * Implementation of the {@link NROPRFEvaluator} class that uses the <a href="https://eprint.iacr.org/2019/517">KALES19 variant</a>.
 */
public class KALES19NROPRFEvaluator extends NROPRFEvaluator implements PrecomputedOPRFEvaluator<Pair<boolean[], BigInteger[]>> {

    private final RandomObliviousTransferReceiver ROTReceiver;
    private final int bitLengthRandomValues;

    /**
     * Constructor for the {@link NROPRFEvaluator} class.
     *
     * @param p The (not-null) prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @param q The (not-null) prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     * @param ROTReceiver The (not-null) {@link RandomObliviousTransferReceiver} that is used to receive the random values.
     * @param bitLengthRandomValues The bit length of the random values that are used to apply the PRF.
     */
    public KALES19NROPRFEvaluator(BigInteger p, BigInteger q, RandomObliviousTransferReceiver ROTReceiver, int bitLengthRandomValues) {
        super(p, q);
        this.ROTReceiver = ROTReceiver;
        this.bitLengthRandomValues = bitLengthRandomValues;
    }

    @Override
    public Pair<boolean[], BigInteger[]> executeOfflinePhase(BigInteger[] elements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        return ROTReceiver.execute(elements.length * bitLength, bitLengthRandomValues, inputStream, outputStream);
    }

    @Override
    public BigInteger[] executeOnlinePhase(Pair<boolean[], BigInteger[]> resultOfflinePhase, BigInteger[] elements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException, ExecutionException, InterruptedException {
        boolean[] choices = resultOfflinePhase.getLeft();
        boolean[] b = new boolean[elements.length * bitLength];
        IntStream.range(0, elements.length).parallel().forEach(i -> {
            BigInteger element = elements[i];
            for (int j = 0; j < bitLength; j ++) b[i * bitLength + j] = (choices[i * bitLength + j] != element.testBit(j));
        });
        BigInteger bAsBigInteger = ArrayUtils.convertToBigInteger(b);
        StreamUtils.writeBigIntegerToOutputStream(bAsBigInteger, outputStream);
        outputStream.flush();

        BigInteger[] r_i_j_c_i_j = resultOfflinePhase.getRight();
        BigInteger[] C_i = new BigInteger[elements.length];
        BigInteger[] xor_product = new BigInteger[elements.length * bitLength];
        for (int i = 0; i < xor_product.length; i ++) xor_product[i] = StreamUtils.readBigIntegerFromInputStream(inputStream);
        ForkJoinTask<BigInteger[]> g_modded_task = ForkJoinPool.commonPool().submit(() -> {
            BigInteger[] g_modded = new BigInteger[elements.length];
            for (int i = 0; i < g_modded.length; i ++) {
                try {
                    g_modded[i] = StreamUtils.readBigIntegerFromInputStream(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return g_modded;
        });
        IntStream.range(0, elements.length).parallel().forEach(i -> {
            BigInteger element = elements[i];

            BigInteger[] R = new BigInteger[bitLength];
            for (int j = 0; j < bitLength; j ++) R[j] = r_i_j_c_i_j[i * bitLength + j].xor((element.testBit(j) ? BigInteger.ONE : BigInteger.ZERO).multiply(xor_product[i * bitLength + j]));

            BigInteger exponent = BigInteger.ONE;
            for (int j = 0; j < bitLength; j ++) exponent = exponent.multiply(R[j]);
            C_i[i] = exponent.mod(getQ());
        });

        BigInteger[] returnValue = new BigInteger[elements.length];
        BigInteger[] g_modded = g_modded_task.get();
        IntStream.range(0, returnValue.length).parallel().forEach(i -> {
            returnValue[i] = g_modded[i].modPow(C_i[i], getP());
        });
        return returnValue;
    }
}
