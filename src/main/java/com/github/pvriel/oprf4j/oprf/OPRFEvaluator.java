package com.github.pvriel.oprf4j.oprf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public interface OPRFEvaluator {

    BigInteger evaluate(BigInteger element, int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
