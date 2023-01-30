package com.github.pvriel.oprf4j.nroprf;

import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;

import java.math.BigInteger;

/**
 * Abstract class representing Naor-Reingold-based {@link OPRFEvaluator}s.
 * <br>The difference between this implementation and the definition of the NR-PRFs, is that two keys are used here.
 * The a0 keys are used for the inactive bits, while the a1 keys are used for the active bits.
 * The initialKey parameter for the constructor representing a_{0} from the Wikipedia page.
 */
public abstract class NROPRFEvaluator implements OPRFEvaluator {

    private final BigInteger p;
    private final BigInteger q;

    /**
     * Constructor for the {@link NROPRFEvaluator} class.
     * @param   p
     *          The (not-null) prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @param   q
     *          The (not-null) prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     */
    protected NROPRFEvaluator(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
    }

    /**
     * Getter for the prime number that is used for the group (the result of applying the PRF is always part of F_{p}).
     * @return  The prime number.
     */
    protected BigInteger getP() {
        return p;
    }

    /**
     * Getter for the prime number, for which q | p - 1 holds. This prime number is used to apply mod operations with the exponents.
     * @return  The prime number.
     */
    protected BigInteger getQ() {
        return q;
    }
}
