package com.github.pvriel.oprf4j.nroprf.semihonest.KALES19;

import com.github.pvriel.mpcutils4j.math.MultiplicativeGroup;
import com.github.pvriel.oblivioustransfer4j.ot.semihonest.alsz13.ALSZ13ObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.ot.semihonest.alsz13.ALSZ13ObliviousTransferSender;
import com.github.pvriel.oblivioustransfer4j.ote.semihonest.alsz13.ALSZ13ObliviousTransferExtensionReceiver;
import com.github.pvriel.oblivioustransfer4j.ote.semihonest.alsz13.ALSZ13ObliviousTransferExtensionSender;
import com.github.pvriel.oblivioustransfer4j.precomputed.semihonest.bea95.BEA95PrecomputedObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.precomputed.semihonest.bea95.BEA95PrecomputedObliviousTransferSender;
import com.github.pvriel.oblivioustransfer4j.rot.semihonest.asharov17.ASHAROV17RandomObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.rot.semihonest.asharov17.ASHAROV17RandomObliviousTransferSender;
import com.github.pvriel.oblivioustransfer4j.rrot.RandomObliviousTransferReceiver;
import com.github.pvriel.oblivioustransfer4j.srot.RandomObliviousTransferSender;
import com.github.pvriel.oprf4j.AbstractOPRFTest;
import com.github.pvriel.oprf4j.nroprf.semihonest.KISS17.KISS17NROPRFEvaluator;
import com.github.pvriel.oprf4j.nroprf.semihonest.KISS17.KISS17NROPRFProvider;
import com.github.pvriel.oprf4j.oprf.OPRFEvaluator;
import com.github.pvriel.oprf4j.oprf.OPRFProvider;
import com.github.pvriel.prf4j.PRF;
import com.github.pvriel.prf4j.naorreingold.NaorReingoldPRF;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static com.github.pvriel.ot.semihonest.KISS17.KISS17ObliviousTransferDefaultValues.p;
import static com.github.pvriel.ot.semihonest.KISS17.KISS17ObliviousTransferDefaultValues.q;

class KALES19NROPRFTest extends AbstractOPRFTest {

    private final static int BIT_LENGTH_TEST_CASES = 128;
    private final static int AMOUNT_OF_TEST_CASES = 1000;
    private final static Random random = new Random();

    @Override
    protected Triple<Integer, Pair<BigInteger[], BigInteger[]>, Pair<OPRFProvider, OPRFEvaluator>> setup() {
        BigInteger g = new MultiplicativeGroup(p.getValue()).getElementOfOrder(q.getValue());
        MultiplicativeGroup multiplicativeGroupQ = new MultiplicativeGroup(q.getValue());
        BigInteger a0[] = multiplicativeGroupQ.getRandomElements(BIT_LENGTH_TEST_CASES);
        BigInteger a1[] = new BigInteger[BIT_LENGTH_TEST_CASES];
        BigInteger initialKey = BigInteger.ONE;
        Arrays.fill(a1, BigInteger.ONE);
        System.out.println("Generating test cases...");

        ALSZ13ObliviousTransferSender baseOTSender = new ALSZ13ObliviousTransferSender(p.getValue(), q.getValue(), g);
        ALSZ13ObliviousTransferReceiver baseOTReceiver = new ALSZ13ObliviousTransferReceiver(p.getValue(), q.getValue(), g);
        ALSZ13ObliviousTransferExtensionSender OTeSender = new ALSZ13ObliviousTransferExtensionSender(10, baseOTReceiver);
        ALSZ13ObliviousTransferExtensionReceiver OTeReceiver = new ALSZ13ObliviousTransferExtensionReceiver(10, baseOTSender);
        ASHAROV17RandomObliviousTransferSender ROTSender = new ASHAROV17RandomObliviousTransferSender(10, OTeReceiver);
        ASHAROV17RandomObliviousTransferReceiver ROTReceiver = new ASHAROV17RandomObliviousTransferReceiver(10, OTeSender);
        KALES19NROPRFProvider provider = new KALES19NROPRFProvider(initialKey, a0, null, p.getValue(), q.getValue(), g, ROTSender, q.getValue().bitLength());
        KALES19NROPRFEvaluator evaluator = new KALES19NROPRFEvaluator(p.getValue(), q.getValue(), ROTReceiver, q.getValue().bitLength());

        BigInteger inputs[] = new BigInteger[AMOUNT_OF_TEST_CASES];
        PRF prf = new NaorReingoldPRF(initialKey, a0, a1, p.getValue(), q.getValue(), g);
        for (int i = 0; i < AMOUNT_OF_TEST_CASES; i++) {
            // if (i % (Math.max(1, AMOUNT_OF_TEST_CASES / 10)) == 1) System.out.println("Generating test case " + i + " of " + AMOUNT_OF_TEST_CASES);
            inputs[i] = new BigInteger(BIT_LENGTH_TEST_CASES, random);
        }
        BigInteger expectedOutputs[] = prf.compute(inputs);

        return Triple.of(BIT_LENGTH_TEST_CASES, Pair.of(inputs, expectedOutputs), Pair.of(provider, evaluator));
    }
}