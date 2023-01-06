package com.github.pvriel.oprf4j.nroprf.semihonest.KISS17;

import com.github.pvriel.oblivioustransfer4j.ot.semihonest.alsz13.ALSZ13ObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.ot.semihonest.alsz13.ALSZ13ObliviousTransferSender;
import com.github.pvriel.oblivioustransfer4j.precomputed.semihonest.bea95.BEA95PrecomputedObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.precomputed.semihonest.bea95.BEA95PrecomputedObliviousTransferSender;
import com.github.pvriel.oprf4j.AbstractOPRFTest;
import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;
import com.github.pvriel.oprf4j.oprf.OPRFProvider;
import com.github.pvriel.ot.semihonest.alsz13.ALSZ13ObliviousTransferDefaultValues;
import com.github.pvriel.ot.semihonest.alsz13.MultiplicativeGroup;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KISS17NROPRFTest extends AbstractOPRFTest {

    @Override
    protected List<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> setup() {
        ArrayList<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> returnValue = new ArrayList<>();
        returnValue.add(generateTestCaseOne());
        returnValue.add(generateTestCaseTwo());
        return returnValue;
    }

    private static Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger> generateTestCaseOne() {
        BigInteger otG = new MultiplicativeGroup(ALSZ13ObliviousTransferDefaultValues.p.getValue()).getElementOfOrder(ALSZ13ObliviousTransferDefaultValues.q.getValue());
        BEA95PrecomputedObliviousTransferSender OTSender = new BEA95PrecomputedObliviousTransferSender(new ALSZ13ObliviousTransferSender(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), otG));
        BEA95PrecomputedObliviousTransferReceiver OTReceiver = new BEA95PrecomputedObliviousTransferReceiver(new ALSZ13ObliviousTransferReceiver(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), otG));

        BigInteger a1[] = new BigInteger[]{BigInteger.ONE, BigInteger.TWO, BigInteger.ONE};
        BigInteger a0[] = new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
        BigInteger p = BigInteger.valueOf(7);
        BigInteger q = BigInteger.valueOf(3);
        BigInteger g = BigInteger.valueOf(4);
        BigInteger input = BigInteger.valueOf(5);
        BigInteger expectedOutput = BigInteger.valueOf(4);

        int bitLengthRandomValues = 2048;
        KISS17NROPRFProvider provider = new KISS17NROPRFProvider(bitLengthRandomValues, OTSender, a0, a1, p, q, g);
        KISS17NROPRFEvaluator evaluator = new KISS17NROPRFEvaluator(bitLengthRandomValues, OTReceiver, p, q);

        return Triple.of(Pair.of(Pair.of(3, provider), Pair.of(3, evaluator)), input, expectedOutput);
    }

    private static Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger> generateTestCaseTwo() {
        BigInteger otG = new MultiplicativeGroup(ALSZ13ObliviousTransferDefaultValues.p.getValue()).getElementOfOrder(ALSZ13ObliviousTransferDefaultValues.q.getValue());
        BEA95PrecomputedObliviousTransferSender OTSender = new BEA95PrecomputedObliviousTransferSender(new ALSZ13ObliviousTransferSender(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), otG));
        BEA95PrecomputedObliviousTransferReceiver OTReceiver = new BEA95PrecomputedObliviousTransferReceiver(new ALSZ13ObliviousTransferReceiver(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), otG));

        BigInteger a1[] = new BigInteger[]{BigInteger.ONE, BigInteger.TWO, BigInteger.ONE};
        BigInteger a0[] = new BigInteger[]{BigInteger.ONE, BigInteger.ONE, BigInteger.ONE};
        BigInteger p = BigInteger.valueOf(7);
        BigInteger q = BigInteger.valueOf(3);
        BigInteger g = BigInteger.valueOf(4);
        BigInteger input = BigInteger.valueOf(6);
        BigInteger expectedOutput = BigInteger.valueOf(2);

        int bitLengthRandomValues = 2048;
        KISS17NROPRFProvider provider = new KISS17NROPRFProvider(bitLengthRandomValues, OTSender, a0, a1, p, q, g);
        KISS17NROPRFEvaluator evaluator = new KISS17NROPRFEvaluator(bitLengthRandomValues, OTReceiver, p, q);

        return Triple.of(Pair.of(Pair.of(3, provider), Pair.of(3, evaluator)), input, expectedOutput);
    }
}