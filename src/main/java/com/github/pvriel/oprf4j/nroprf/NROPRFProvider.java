package com.github.pvriel.oprf4j.nroprf;

import com.github.pvriel.oprf4j.oprf.OPRFProvider;

import java.math.BigInteger;

public abstract class NROPRFProvider implements OPRFProvider {

    private final BigInteger[] a0;
    private final BigInteger[] a1;
    private final BigInteger initialKey;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger g;

    protected NROPRFProvider(BigInteger initialKey, BigInteger[] a0, BigInteger[] a1, BigInteger p, BigInteger q, BigInteger g) {
        this.a0 = a0;
        this.a1 = a1;
        this.initialKey = initialKey;
        this.p = p;
        this.q = q;
        this.g = g;
    }

    protected BigInteger getG() {
        return g;
    }

    protected BigInteger[] getA1() {
        return a1;
    }

    protected BigInteger getInitialKey() {
        return initialKey;
    }

    protected BigInteger getP() {
        return p;
    }

    protected BigInteger getQ() {
        return q;
    }

    protected BigInteger[] getA0() {
        return a0;
    }
}
