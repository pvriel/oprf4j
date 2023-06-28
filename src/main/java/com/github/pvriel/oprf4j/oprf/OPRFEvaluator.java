package com.github.pvriel.oprf4j.oprf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 *  Interface representing nodes that work together with {@link OPRFProvider} nodes to realize the OPRF functionality.
 *  The evaluators are responsible for providing the elements to apply the OPRF to.
 */
public interface OPRFEvaluator {

    /**
     * Method to execute the OPRF functionality.
     * @param   elements
     *          The (not-null) elements to apply the PRF to.
     * @param   bitLength
     *          The bit length of the elements. Should be strictly positive, and should match the value used by the {@link OPRFProvider} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link OPRFProvider} node.
     *          <br>This stream will not be closed after this process.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link OPRFProvider} node.
     *          <br>This stream will not be closed after this process.
     * @return  The (not-null) OPRF (element) results.
     * @throws  IOException
     *          If a problem occurred during the execution of this process.
     */
    BigInteger[] evaluate(BigInteger[] elements, int bitLength, InputStream inputStream, OutputStream outputStream) throws Exception;

    /**
     * Method to execute the OPRF functionality.
     * @param   element
     *          The (not-null) element to apply the PRF to.
     * @param   bitLength
     *          The bit length of the element. Should be strictly positive, and should match the value used by the {@link OPRFProvider} node.
     * @param   inputStream
     *          The (not-null) input stream to receive data from the {@link OPRFProvider} node.
     *          <br>This stream will not be closed after this process.
     * @param   outputStream
     *          The (not-null) output stream to send data to the {@link OPRFProvider} node.
     *          <br>This stream will not be closed after this process.
     * @return  The (not-null) OPRF (element) result.
     * @throws  IOException
     *          If a problem occurred during the execution of this process.
     */
    default BigInteger evaluate(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws Exception {
        return evaluate(new BigInteger[] {element}, bitLength, inputStream, outputStream)[0];
    }
}
