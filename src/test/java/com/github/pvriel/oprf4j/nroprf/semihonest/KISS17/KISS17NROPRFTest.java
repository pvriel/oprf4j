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
import com.github.pvriel.prf4j.PRF;
import com.github.pvriel.prf4j.naorreingold.NaorReingoldPRF;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class KISS17NROPRFTest extends AbstractOPRFTest {

    @Override
    protected List<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> setup() {
        ArrayList<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> returnValue = new ArrayList<>();
        returnValue.add(generateTestCaseOne());
        returnValue.add(generateTestCaseTwo());
        returnValue.addAll(generateRandomTestCases());
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

    private static List<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> generateRandomTestCases() {
        List<Triple<Pair<Pair<Integer, OPRFProvider>, Pair<Integer, OPRFEvaluator>>, BigInteger, BigInteger>> returnValue = new ArrayList<>();

        int bitLength = 128;

        BigInteger p = new BigInteger("32317006071311007300714876688669951960444102669715484032130345427524655138867890893197201411522913463688717960921898019494119559150490921095088152386448283120630877367300996091750197750389652106796057638384067568276792218642619756161838094338476170470581645852036305042887575891541065808607552399123930385521914333389668342420684974786564569494856176035326322058077805659331026192708460314150258592864177116725943603718461857357598351152301645904403697613233287231227125684710820209725157101726931323469678542580656697935045997268352998638215525193403303896028543209689578721838988682461578457274025662014413066681559");
        BigInteger q = new BigInteger("26959946667150639794667015087019630673637144422540572481103610249951");
        BigInteger g = new com.github.pvriel.oprf4j.utils.MultiplicativeGroup(p).getElementOfOrder(q);
        com.github.pvriel.oprf4j.utils.MultiplicativeGroup multiplicativeGroupQ = new com.github.pvriel.oprf4j.utils.MultiplicativeGroup(q);
        BigInteger a0[] = multiplicativeGroupQ.getRandomElements(bitLength);
        BigInteger a1[] = multiplicativeGroupQ.getRandomElements(bitLength);
        int bitLengthRandomValues = 128;

        BEA95PrecomputedObliviousTransferSender OTSender = new BEA95PrecomputedObliviousTransferSender(new ALSZ13ObliviousTransferSender(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), g));
        BEA95PrecomputedObliviousTransferReceiver OTReceiver = new BEA95PrecomputedObliviousTransferReceiver(new ALSZ13ObliviousTransferReceiver(ALSZ13ObliviousTransferDefaultValues.p.getValue(), ALSZ13ObliviousTransferDefaultValues.q.getValue(), g));
        KISS17NROPRFProvider provider = new KISS17NROPRFProvider(bitLengthRandomValues, OTSender, a0, a1, p, q, g);
        KISS17NROPRFEvaluator evaluator = new KISS17NROPRFEvaluator(bitLengthRandomValues, OTReceiver, p, q);

        PRF prf = new NaorReingoldPRF(BigInteger.ONE, a1, a0, p, q, g);
        for (int i = 0; i < 100; i++) {
            BigInteger input = new BigInteger(bitLength, new Random());
            BigInteger expectedOutput = prf.compute(input);
            returnValue.add(Triple.of(Pair.of(Pair.of(bitLength, provider), Pair.of(bitLength, evaluator)), input, expectedOutput));
        }

        return returnValue;
    }
}