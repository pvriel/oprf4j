package com.github.pvriel.oprf4j.nroprf;

import com.github.pvriel.oprf4j.oprf.OPRFProvider;

import java.math.BigInteger;

/**
 * Abstract class representing <a href="https://en.wikipedia.org/wiki/Naor–Reingold_pseudorandom_function">Naor-Reingold-based</a> {@link OPRFProvider}s.
 * <br>The difference between this implementation and the definition of the NR-PRFs, is that two keys are used here.
 * The a0 keys are used for the inactive bits, while the a1 keys are used for the active bits.
 * The initialKey parameter for the constructor representing a_{0} from the Wikipedia page.
 */
public abstract class NROPRFProvider implements OPRFProvider {

    private final BigInteger[] a0;
    private final BigInteger[] a1;
    private final BigInteger initialKey;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger g;

    /**
     * Constructor for the {@link NROPRFProvider} class.
     * @param   initialKey
     *          The (not-null) key that is always used as exponent for the generator, no matter the element that is being applied for the OPRF.
     * @param   a0
     *          The (not-null) array of keys that are used for the inactive bits.
     * @param   a1
     *          The (not-null) array of keys that are used for the active bits.
     * @param   p
     *          The (not-null) prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @param   q
     *          The (not-null) prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     * @param   g
     *          The (not-null) generator of the group F_{p}.
     */
    protected NROPRFProvider(BigInteger initialKey, BigInteger[] a0, BigInteger[] a1, BigInteger p, BigInteger q, BigInteger g) {
        this.a0 = a0;
        this.a1 = a1;
        this.initialKey = initialKey;
        this.p = p;
        this.q = q;
        this.g = g;
    }

    /**
     * Getter for the generator.
     * @return The generator of the group F_{p}.
     */
    protected BigInteger getG() {
        return g;
    }

    /**
     * Getter for the keys that are used for the active bits.
     * @return  The keys.
     */
    protected BigInteger[] getA1() {
        return a1;
    }

    /**
     * Getter for the initial key.
     * @return The initial key.
     */
    protected BigInteger getInitialKey() {
        return initialKey;
    }

    /**
     * Getter for the prime number p.
     * @return The prime number p.
     */
    protected BigInteger getP() {
        return p;
    }

    /**
     * Getter for the prime number q.
     * @return The prime number q.
     */
    protected BigInteger getQ() {
        return q;
    }

    /**
     * Getter for the keys that are used for the inactive bits.
     * @return The keys.
     */
    protected BigInteger[] getA0() {
        return a0;
    }
}
