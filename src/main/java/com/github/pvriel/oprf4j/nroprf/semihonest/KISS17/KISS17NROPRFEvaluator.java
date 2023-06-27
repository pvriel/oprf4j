package com.github.pvriel.oprf4j.nroprf.semihonest.KISS17;

import com.github.pvriel.mpcutils4j.stream.StreamUtils;
import com.github.pvriel.oblivioustransfer4j.precomputed.PrecomputedObliviousTransferReceiver;
import com.github.pvriel.oprf4j.nroprf.NROPRFEvaluator;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFEvaluator;
import org.apache.commons.lang3.tuple.Triple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Class representing the <a href="https://www.semanticscholar.org/paper/Private-Set-Intersection-for-Unequal-Set-Sizes-with-Kiss-Liu/ef148d510ce6fd445b73584a238f6244660efbe6">KISS17</a> variant of the
 * {@link NROPRFEvaluator} protocol.
 */
public class KISS17NROPRFEvaluator extends NROPRFEvaluator implements PrecomputedOPRFEvaluator<Triple<boolean[], BigInteger[], Object>> {

    private final PrecomputedObliviousTransferReceiver<Object> precomputedObliviousTransferReceiver;
    private final int bitLengthRandomValues;

    /**
     * Constructor for the {@link KISS17NROPRFEvaluator} class.
     * @param   bitLengthRandomValues
     *          The bit length of the random values that are used in the protocol.
     *          <br>This value should be strictly positive, and should match the value used at the evaluator side.
     * @param   precomputedObliviousTransferReceiver
     *          The (not-null) {@link PrecomputedObliviousTransferReceiver} used to perform the oblivious transfer.
     * @param   p
     *          The (not-null) prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @param   q
     *          The (not-null) prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     * @param  <T>
     *          The type of the elements that are used in the oblivious transfer.
     */
    public <T> KISS17NROPRFEvaluator(int bitLengthRandomValues, PrecomputedObliviousTransferReceiver<T> precomputedObliviousTransferReceiver,
                                 BigInteger p, BigInteger q) {
        super(p, q);

        this.bitLengthRandomValues = bitLengthRandomValues;
        this.precomputedObliviousTransferReceiver = (PrecomputedObliviousTransferReceiver<Object>) precomputedObliviousTransferReceiver;
    }

    @Override
    public Triple<boolean[], BigInteger[], Object> executeOfflinePhase(BigInteger[] elements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        boolean[] choices = new boolean[elements.length * bitLength];
        for (int i = 0; i < elements.length; i ++) for (int j = 0; j < bitLength; j ++) choices[i * bitLength + j] = elements[i].testBit(j);

        BigInteger[] g_modded = new BigInteger[elements.length];
        for (int i = 0; i < g_modded.length; i ++) g_modded[i] = StreamUtils.readBigIntegerFromInputStream(inputStream);
        var resultOfflinePhaseOTs =  precomputedObliviousTransferReceiver.executeOfflinePhase(choices, bitLengthRandomValues, inputStream, outputStream);
        return Triple.of(choices, g_modded, resultOfflinePhaseOTs);
    }

    @Override
    public BigInteger[] executeOnlinePhase(Triple<boolean[], BigInteger[], Object> resultOfflinePhase, BigInteger[] elements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        boolean[] choices = resultOfflinePhase.getLeft();
        BigInteger[] g_modded = resultOfflinePhase.getMiddle();
        Object resultOfflinePhaseOTs = resultOfflinePhase.getRight();

        BigInteger[] R_s = precomputedObliviousTransferReceiver.executeOnlinePhase(resultOfflinePhaseOTs, choices, bitLengthRandomValues, inputStream, outputStream);

        BigInteger[] result = new BigInteger[elements.length];
        IntStream.range(0, elements.length).parallel().forEach(i -> {
            BigInteger g_modded_i = g_modded[i];
            BigInteger exponent = BigInteger.ONE;
            for (int j = 0; j < bitLength; j ++) exponent = exponent.multiply(R_s[i * bitLength + j]).mod(getQ());
            result[i] = g_modded_i.modPow(exponent, getP());
        });
        return result;
    }
}
