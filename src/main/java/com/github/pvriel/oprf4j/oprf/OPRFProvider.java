package com.github.pvriel.oprf4j.oprf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface OPRFProvider {

    void execute(int bitLength, InputStream inputStream, OutputStream outputStream) throws IOException;
}
