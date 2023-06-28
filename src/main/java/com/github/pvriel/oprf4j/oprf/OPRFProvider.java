package com.github.pvriel.oprf4j.oprf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface representing nodes that provide OPRF functionality to {@link OPRFEvaluator} nodes.
 * The providers are responsible for managing the underlying key(s).
 */
public interface OPRFProvider {

    /**
     * Method to provide the OPRF functionality.
     * @param   amountOfValues
     *          The amount of values to apply the PRF to (from the evaluator side).
     *          This value should match the amount of values provided by the {@link OPRFEvaluator} node.
     * @param   bitLength
     *          The bit length of the messages to apply the PRF to. Should be strictly positive, and should match the value used by the {@link OPRFEvaluator} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link OPRFEvaluator} node.
     *          <br>The stream will not be closed after this process.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link OPRFEvaluator} node.
     *          <br>The stream will not be closed after this process.
     * @throws  Exception
     *          If a problem occurred during the execution of this process.
     */
    void execute(int amountOfValues, int bitLength, InputStream inputStream, OutputStream outputStream) throws Exception;

    /**
     * Method to provide the OPRF functionality.
     * @param   bitLength
     *          The bit length of the messages to apply the PRF to. Should be strictly positive, and should match the value used by the {@link OPRFEvaluator} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link OPRFEvaluator} node.
     *          <br>The stream will not be closed after this process.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link OPRFEvaluator} node.
     *          <br>The stream will not be closed after this process.
     * @throws  Exception
     *          If a problem occurred during the execution of this process.
     */
    default void execute(int bitLength, InputStream inputStream, OutputStream outputStream) throws Exception {
        execute(1, bitLength, inputStream, outputStream);
    }
}
