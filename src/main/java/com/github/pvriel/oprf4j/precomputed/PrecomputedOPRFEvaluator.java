package com.github.pvriel.oprf4j.precomputed;

import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public interface PrecomputedOPRFEvaluator<ReturnTypeOfflinePhase> extends OPRFEvaluator {

    @Override
    default BigInteger evaluate(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhase = executeOfflinePhase(element, bitLength, inputStream, outputStream);
        return executeOnlinePhase(resultOfflinePhase, element, bitLength, inputStream, outputStream);
    }

    ReturnTypeOfflinePhase executeOfflinePhase(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;

    BigInteger executeOnlinePhase(ReturnTypeOfflinePhase resultOfflinePhase, BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
