package com.github.pvriel.oprf4j.precomputed;

import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Interface representing {@link OPRFEvaluator} nodes that perform a part of their computations offline.
 * @param   <ReturnTypeOfflinePhase>
 *          The type of the intermediate result from the offline phase, that can be used to execute the online phase with.
 */
public interface PrecomputedOPRFEvaluator<ReturnTypeOfflinePhase> extends OPRFEvaluator {

    @Override
    default BigInteger evaluate(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhase = executeOfflinePhase(element, bitLength, inputStream, outputStream);
        return executeOnlinePhase(resultOfflinePhase, element, bitLength, inputStream, outputStream);
    }

    /**
     * Method to execute the offline phase of the OPRF protocol.
     * @param   element
     *          The (not-null) element to apply the PRF to.
     * @param   bitLength
     *          The bit length of the element. Should be strictly positive, and should match the value used by the {@link PrecomputedOPRFProvider} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @return  An intermediate result, that can be used to execute the online phase of this protocol with.
     * @throws  IOException
     *          If an IO-related problem occurred while executing the protocol.
     */
    ReturnTypeOfflinePhase executeOfflinePhase(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;

    /**
     * Method to execute the online phase of the OPRF protocol.
     * @param   resultOfflinePhase
     *          The result from executing the executeOfflinePhase(...) method.
     * @param   element
     *      *          The (not-null) element to apply the PRF to.
     * @param   bitLength
     *          The bit length of the element. Should be strictly positive, and should match the value used by the {@link PrecomputedOPRFProvider} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @return  The not-null OPRF(element) result.
     * @throws  IOException
     *          If an IO-related problem occurred while executing the protocol.
     */
    BigInteger executeOnlinePhase(ReturnTypeOfflinePhase resultOfflinePhase, BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
