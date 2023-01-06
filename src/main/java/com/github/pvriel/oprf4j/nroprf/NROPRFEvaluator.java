package com.github.pvriel.oprf4j.nroprf;

import java.math.BigInteger;

public abstract class NROPRFEvaluator {

    private final BigInteger p;
    private final BigInteger q;

    protected NROPRFEvaluator(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
    }

    protected BigInteger getP() {
        return p;
    }

    protected BigInteger getQ() {
        return q;
    }
}
