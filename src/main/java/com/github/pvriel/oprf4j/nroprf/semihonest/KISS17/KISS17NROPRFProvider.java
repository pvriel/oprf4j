package com.github.pvriel.oprf4j.nroprf.semihonest.KISS17;

import com.github.pvriel.mpcutils4j.math.MultiplicativeGroup;
import com.github.pvriel.mpcutils4j.stream.StreamUtils;
import com.github.pvriel.oblivioustransfer4j.precomputed.PrecomputedObliviousTransferSender;
import com.github.pvriel.oprf4j.nroprf.NROPRFProvider;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFProvider;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Class representing the <a href="https://www.semanticscholar.org/paper/Private-Set-Intersection-for-Unequal-Set-Sizes-with-Kiss-Liu/ef148d510ce6fd445b73584a238f6244660efbe6">KISS17</a> variant of the
 * {@link NROPRFProvider} protocol.
 */
public class KISS17NROPRFProvider extends NROPRFProvider implements PrecomputedOPRFProvider<Pair<Object, BigInteger[][]>> {

    private final PrecomputedObliviousTransferSender<Object> precomputedObliviousTransferSender;
    private final int bitLengthRandomValues;

    /**
     * Constructor for the {@link KISS17NROPRFProvider} class.
     * @param   bitLengthRandomValues
     *          The bit length of the random values used in the protocol.
     *          <br>This value should be strictly positive, and should match the value used at the evaluator side.
     * @param   precomputedObliviousTransferSender
     *          The (not-null) {@link PrecomputedObliviousTransferSender} instance used to perform the oblivious transfer.
     * @param   a0
     *          The (not-null) keys that are used for the active bits.
     * @param   a1
     *          The (not-null) keys that are used for the inactive bits.
     * @param   p
     *          The (not-null) modulus of the multiplicative group.
     * @param   q
     *          The (not-null) element to perform the modulus operation with for the exponents.
     * @param   g
     *          The (not-null) generator of the multiplicative group.
     * @param   <T>
     *          The type of the elements in the {@link PrecomputedObliviousTransferSender}.
     */
    public <T> KISS17NROPRFProvider(int bitLengthRandomValues, PrecomputedObliviousTransferSender<T> precomputedObliviousTransferSender,
                                   BigInteger[] a0, BigInteger[] a1, BigInteger p, BigInteger q, BigInteger g) {
        super(null, a0, a1, p, q, g);

        this.bitLengthRandomValues = bitLengthRandomValues;
        this.precomputedObliviousTransferSender = (PrecomputedObliviousTransferSender<Object>) precomputedObliviousTransferSender; // This is safe: check other parts code.
    }

    @Override
    public Pair<Object, BigInteger[][]> executeOfflinePhase(int amountOfElements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        MultiplicativeGroup multiplicativeGroupQ = new MultiplicativeGroup(getQ());
        BigInteger[] r = multiplicativeGroupQ.getRandomElements(amountOfElements * bitLength);

        BigInteger[][] R = new BigInteger[amountOfElements * bitLength][2];
        BigInteger[] g_modded = new BigInteger[amountOfElements];
        IntStream.range(0, amountOfElements).parallel().forEach(i -> {
            BigInteger r_inv_i = BigInteger.ONE;
            for (int j = 0; j < bitLength; j ++) {
                r_inv_i = r_inv_i.multiply(r[i * bitLength + j]).mod(getQ());
                R[i * bitLength + j][0] = r[i * bitLength + j].multiply(getA0()[j]).mod(getQ());
                R[i * bitLength + j][1] = r[i * bitLength + j].multiply(getA1()[j]).mod(getQ());
            }
            r_inv_i = r_inv_i.modInverse(getQ());
            g_modded[i] = getG().modPow(r_inv_i, getP());
        });

        for (BigInteger bigInteger : g_modded) StreamUtils.writeBigIntegerToOutputStream(bigInteger, outputStream);
        outputStream.flush();

        Object resultOfflinePhaseOTs = precomputedObliviousTransferSender.executeOfflinePhase(R, bitLengthRandomValues, inputStream, outputStream);

        return Pair.of(resultOfflinePhaseOTs, R);
    }

    @Override
    public void executeOnlinePhase(Pair<Object, BigInteger[][]> resultOfflinePhase, int amountOfElements, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhaseOTs = resultOfflinePhase.getLeft();
        var R = resultOfflinePhase.getRight();

        precomputedObliviousTransferSender.executeOnlinePhase(resultOfflinePhaseOTs, R, bitLengthRandomValues, inputStream, outputStream);
    }
}
