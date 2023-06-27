package com.github.pvriel.oprf4j.precomputed;

import com.github.pvriel.oprf4j.oprf.OPRFProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface representing {@link OPRFProvider} nodes that perform a part of their computations offline.
 * @param   <ReturnTypeOfflinePhase>
 *          The type of the intermediate result from the offline phase, that can be used to execute the online phase with.
 */
public interface PrecomputedOPRFProvider<ReturnTypeOfflinePhase> extends OPRFProvider  {

    @Override
    default void execute(int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException {
        var resultOfflinePhase = executeOfflinePhase(amountOfValues, bitLength, inputStream, outputStream);
        executeOnlinePhase(resultOfflinePhase, amountOfValues, bitLength, inputStream, outputStream);
    }

    /**
     * Method to execute the offline phase of the OPRF protocol.
     * @param   amountOfValues
     *          The amount of values to apply the PRF to (from the evaluator side).
     *          This value should be strictly positive, and should match the value used by the {@link PrecomputedOPRFEvaluator} node.
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
    ReturnTypeOfflinePhase executeOfflinePhase(int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;

    /**
     * Method to execute the online phase of the OPRF protocol.
     * @param   resultOfflinePhase
     *          The result from executing the executeOfflinePhase(...) method.
     * @param   bitLength
     *          The bit length of the element. Should be strictly positive, and should match the value used by the {@link PrecomputedOPRFProvider} node.
     * @param   amountOfValues
     *          The amount of values to apply the PRF to (from the evaluator side).
     *          This value should be strictly positive, and should match the value used by the {@link PrecomputedOPRFEvaluator} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link PrecomputedOPRFProvider} node.
     *          <br>This stream will not be closed after executing the protocol.
     * @throws  IOException
     *          If an IO-related problem occurred while executing the protocol.
     */
    void executeOnlinePhase(ReturnTypeOfflinePhase resultOfflinePhase, int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
