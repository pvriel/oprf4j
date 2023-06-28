package com.github.pvriel.oprf4j.nroprf.semihonest.KALES19;

import com.github.pvriel.mpcutils4j.array.ArrayUtils;
import com.github.pvriel.mpcutils4j.stream.StreamUtils;
import com.github.pvriel.oblivioustransfer4j.srot.RandomObliviousTransferSender;
import com.github.pvriel.oprf4j.nroprf.NROPRFProvider;
import com.github.pvriel.oprf4j.precomputed.PrecomputedOPRFProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Implementation of the {@link NROPRFProvider} class that uses the <a href="https://eprint.iacr.org/2019/517">KALES19 variant</a>.
 */
public class KALES19NROPRFProvider extends NROPRFProvider implements PrecomputedOPRFProvider<BigInteger[][]> {

    private final RandomObliviousTransferSender ROTSender;
    private final int bitLengthRandomValues;

    /**
     * Constructor for the {@link NROPRFProvider} class.
     *
     * @param initialKey The (not-null) key that is always used as exponent for the generator, no matter the element that is being applied for the OPRF.
     * @param a0         The (not-null) array of keys that are used for the active bits.
     * @param a1         The (not-null) array of keys that are used for the inactive bits.
     *                   However, this value is never used.
     * @param p          The (not-null) prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @param q          The (not-null) prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     * @param g          The (not-null) generator of the group F_{p}.
     * @param ROTSender  The (not-null) {@link RandomObliviousTransferSender} that is used to execute the random oblivious transfer protocol with.
     * @param bitLengthRandomValues The (not-null) bit length of the random values that are used in the random oblivious transfer protocol.
     */
    public KALES19NROPRFProvider(BigInteger initialKey, BigInteger[] a0, BigInteger[] a1, BigInteger p, BigInteger q,
                                    BigInteger g, RandomObliviousTransferSender ROTSender, int bitLengthRandomValues) {
        super(initialKey, a0, null, p, q, g);
        this.ROTSender = ROTSender;
        this.bitLengthRandomValues = bitLengthRandomValues;
    }

    @Override
    public BigInteger[][] executeOfflinePhase(int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        return ROTSender.execute(amountOfValues * bitLength, bitLengthRandomValues, inputStream, outputStream);
    }

    @Override
    public void executeOnlinePhase(BigInteger[][] resultOfflinePhase, int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        BigInteger bAsBigInteger = StreamUtils.readBigIntegerFromInputStream(inputStream);
        boolean[] b = ArrayUtils.convertFromBigInteger(bAsBigInteger, amountOfValues * bitLength);

        BigInteger[] xor_product = new BigInteger[amountOfValues * bitLength];
        BigInteger[] r_i_j = new BigInteger[amountOfValues * bitLength];
        IntStream.range(0, amountOfValues).parallel().forEach(i -> {
            for (int j = 0; j < bitLength; j ++) {
                int selection = b[i * bitLength + j]? 1 : 0;
                BigInteger selectedRValue = resultOfflinePhase[i * bitLength + j][selection];
                r_i_j[i * bitLength + j] = selectedRValue;
                BigInteger notSelectedRValue = resultOfflinePhase[i * bitLength + j][1 - selection];
                xor_product[i * bitLength + j] = notSelectedRValue.xor(selectedRValue.multiply(getA0()[j]));
            }
        });
        for (int i = 0; i < xor_product.length; i ++) StreamUtils.writeBigIntegerToOutputStream(xor_product[i], outputStream);
        outputStream.flush();

        BigInteger[] g_i = new BigInteger[amountOfValues];
        IntStream.range(0, amountOfValues).parallel().forEach(i -> {
            BigInteger r_inv = BigInteger.ONE;
            for (int j = 0; j < bitLength; j++) r_inv = r_inv.multiply(r_i_j[i * bitLength + j]);
            r_inv = r_inv.modInverse(getQ());

            g_i[i] = getG().modPow(getInitialKey().multiply(r_inv), getP());
        });
        for (int i = 0; i < amountOfValues; i++) {
            StreamUtils.writeBigIntegerToOutputStream(g_i[i], outputStream);
        }
        outputStream.flush();
    }
}
