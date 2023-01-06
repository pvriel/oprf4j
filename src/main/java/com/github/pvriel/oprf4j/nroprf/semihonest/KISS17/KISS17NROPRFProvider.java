package com.github.pvriel.oprf4j.nroprf.semihonest.KISS17;

import com.github.pvriel.oblivioustransfer4j.precomputed.PrecomputedObliviousTransferSender;
import com.github.pvriel.oblivioustransfer4j.utils.StreamUtils;
import com.github.pvriel.oprf4j.nroprf.NROPRFProvider;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFProvider;
import com.github.pvriel.oprf4j.utils.MultiplicativeGroup;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class KISS17NROPRFProvider extends NROPRFProvider implements PrecomputedOPRFProvider<Pair<Object, BigInteger[][]>> {

    final static Random random = new SecureRandom();

    private final PrecomputedObliviousTransferSender<Object> precomputedObliviousTransferSender;
    private final int bitLengthRandomValues;

    protected <T> KISS17NROPRFProvider(int bitLengthRandomValues, PrecomputedObliviousTransferSender<T> precomputedObliviousTransferSender,
                                   BigInteger[] a0, BigInteger[] a1, BigInteger p, BigInteger q, BigInteger g) {
        super(null, a0, a1, p, q, g);

        this.bitLengthRandomValues = bitLengthRandomValues;
        this.precomputedObliviousTransferSender = (PrecomputedObliviousTransferSender<Object>) precomputedObliviousTransferSender;
    }

    @Override
    public Pair<Object, BigInteger[][]> executeOfflinePhase(int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        MultiplicativeGroup multiplicativeGroupQ = new MultiplicativeGroup(getQ());
        BigInteger[] r = multiplicativeGroupQ.getRandomElements(bitLength);

        BigInteger[][] R = new BigInteger[bitLength][2];
        BigInteger r_inv = BigInteger.ONE;
        for (int i = 0; i < R.length; i ++) {
            r_inv = r_inv.multiply(r[i]).mod(getQ());
            R[i][0] = r[i].multiply(getA0()[i]).mod(getQ());
            R[i][1] = r[i].multiply(getA1()[i]).mod(getQ());
        }
        r_inv = r_inv.modInverse(getQ());
        BigInteger g_modded = getG().modPow(r_inv, getP());
        StreamUtils.writeBigIntegerToOutputStream(g_modded, outputStream);

        Object resultOfflinePhaseOTs = precomputedObliviousTransferSender.executeOfflinePhase(R, bitLengthRandomValues, inputStream, outputStream);

        return Pair.of(resultOfflinePhaseOTs, R);
    }

    @Override
    public void executeOnlinePhase(Pair<Object, BigInteger[][]> resultOfflinePhase, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhaseOTs = resultOfflinePhase.getLeft();
        var R = resultOfflinePhase.getRight();

        precomputedObliviousTransferSender.executeOnlinePhase(resultOfflinePhaseOTs, R, bitLengthRandomValues, inputStream, outputStream);
    }
}
