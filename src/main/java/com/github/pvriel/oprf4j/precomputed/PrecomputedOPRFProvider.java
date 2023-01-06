package com.github.pvriel.oprf4j.precomputed;

import com.github.pvriel.oprf4j.oprf.OPRFProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PrecomputedOPRFProvider<ReturnTypeOfflinePhase> extends OPRFProvider  {

    @Override
    default void execute(int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhase = executeOfflinePhase(bitLength, inputStream, outputStream);
        executeOnlinePhase(resultOfflinePhase, bitLength, inputStream, outputStream);
    }

    ReturnTypeOfflinePhase executeOfflinePhase(int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;

    void executeOnlinePhase(ReturnTypeOfflinePhase resultOfflinePhase, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
