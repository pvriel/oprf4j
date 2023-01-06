package com.github.pvriel.oprf4j.nroprf.semihonest.KISS17;

import com.github.pvriel.oblivioustransfer4j.precomputed.PrecomputedObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.utils.StreamUtils;
import com.github.pvriel.oprf4j.nroprf.NROPRFEvaluator;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFEvaluator;
import org.apache.commons.lang3.tuple.Triple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class KISS17NROPRFEvaluator extends NROPRFEvaluator implements PrecomputedOPRFEvaluator<Triple<boolean[], BigInteger, Object>> {

    private final PrecomputedObliviousTransferReceiver<Object> precomputedObliviousTransferReceiver;
    private final int bitLengthRandomValues;

    public <T> KISS17NROPRFEvaluator(int bitLengthRandomValues, PrecomputedObliviousTransferReceiver<T> precomputedObliviousTransferReceiver,
                                 BigInteger p, BigInteger q) {
        super(p, q);

        this.bitLengthRandomValues = bitLengthRandomValues;
        this.precomputedObliviousTransferReceiver = (PrecomputedObliviousTransferReceiver<Object>) precomputedObliviousTransferReceiver;
    }

    @Override
    public Triple<boolean[], BigInteger, Object> executeOfflinePhase(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        boolean[] choices = new boolean[bitLength];
        for (int i = 0; i < bitLength; i ++) choices[i] = element.testBit(i);

        BigInteger g_modded = StreamUtils.readBigIntegerFromInputStream(inputStream);
        var resultOfflinePhaseOTs =  precomputedObliviousTransferReceiver.executeOfflinePhase(choices, bitLengthRandomValues, inputStream, outputStream);
        return Triple.of(choices, g_modded, resultOfflinePhaseOTs);
    }

    @Override
    public BigInteger executeOnlinePhase(Triple<boolean[], BigInteger, Object> resultOfflinePhase, BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        boolean[] choices = resultOfflinePhase.getLeft();
        BigInteger g_modded = resultOfflinePhase.getMiddle();
        Object resultOfflinePhaseOTs = resultOfflinePhase.getRight();

        BigInteger[] R_s = precomputedObliviousTransferReceiver.executeOnlinePhase(resultOfflinePhaseOTs, choices, bitLengthRandomValues, inputStream, outputStream);

        BigInteger exponent = BigInteger.ONE;
        for (BigInteger r : R_s) exponent = exponent.multiply(r).mod(getQ());
        return g_modded.modPow(exponent, getP());
    }
}
