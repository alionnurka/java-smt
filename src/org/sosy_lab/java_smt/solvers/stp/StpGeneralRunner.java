package org.sosy_lab.java_smt.solvers.stp;

import java.io.IOException;
import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.ArrayFormulaManager;
import org.sosy_lab.java_smt.api.BitvectorFormulaManager;
import org.sosy_lab.java_smt.api.BitvectorFormula;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverException;

public class StpGeneralRunner {

    public static void main(String[] args)
            throws InvalidConfigurationException, SolverException, InterruptedException, IOException {
        Configuration config = Configuration.defaultConfiguration();
        LogManager logger = BasicLogManager.create(config);
        ShutdownNotifier notifier = ShutdownNotifier.createDummy();

        try (SolverContext context =
                     SolverContextFactory.createSolverContext(config, logger, notifier, Solvers.STP)) {
            System.out.println("Using solver: " + context.getSolverName());
            System.out.println("Version: " + context.getVersion());

            FormulaManager fm = context.getFormulaManager();
            BooleanFormulaManager bmgr = fm.getBooleanFormulaManager();
            BitvectorFormulaManager bvmgr = fm.getBitvectorFormulaManager();
            ArrayFormulaManager amgr = fm.getArrayFormulaManager();

            BitvectorFormula x = bvmgr.makeVariable(8, "x");
            BitvectorFormula y = bvmgr.makeVariable(8, "y");
            BitvectorFormula one = bvmgr.makeBitvector(8, 1);
            BitvectorFormula two = bvmgr.makeBitvector(8, 2);
            BitvectorFormula zero = bvmgr.makeBitvector(8, 0);

            // Example 1: SAT constraints
            BooleanFormula yEqXPlus1 = bvmgr.equal(y, bvmgr.add(x, one));
            BooleanFormula xIs0 = bvmgr.equal(x, zero);
            BooleanFormula yIs1 = bvmgr.equal(y, one);

            try (ProverEnvironment prover = context.newProverEnvironment()) {
                // Base: no assertions
                System.out.println("Base (no assertions): isUnsat = " + prover.isUnsat());

                // Example 1: SAT constraints
                prover.push();
                prover.addConstraint(yEqXPlus1);
                prover.addConstraint(xIs0);
                prover.addConstraint(yIs1);
                System.out.println("SAT example: isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 2: UNSAT constraints (y = x+1 and y = x+2 cannot both hold)
                BooleanFormula yEqXPlus2 = bvmgr.equal(y, bvmgr.add(x, two));
                prover.push();
                prover.addConstraint(yEqXPlus1);
                prover.addConstraint(yEqXPlus2);
                System.out.println("UNSAT example: isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 3: bitvector extract + concat
                BitvectorFormula hi = bvmgr.extract(x, 7, 4);
                BitvectorFormula lo = bvmgr.extract(x, 3, 0);
                BitvectorFormula swapped = bvmgr.concat(lo, hi);
                BooleanFormula swappedEqX = bvmgr.equal(swapped, x);
                prover.push();
                prover.addConstraint(swappedEqX);
                System.out.println("Nibble-swap constraint: isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 4: bitwise ops (always true): (x XOR x) == 0, so its negation is UNSAT.
                BooleanFormula xorSelfIsZero = bvmgr.equal(bvmgr.xor(x, x), zero);
                prover.push();
                prover.addConstraint(bmgr.not(xorSelfIsZero));
                System.out.println("Bitwise identity (negated): isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 5: shifts (variable shift amount is also a bitvector)
                BitvectorFormula shift1 = bvmgr.makeBitvector(8, 1);
                BitvectorFormula xShl1 = bvmgr.shiftLeft(x, shift1);
                BitvectorFormula xShr1u = bvmgr.shiftRight(x, shift1, false);
                BitvectorFormula xShr1s = bvmgr.shiftRight(x, shift1, true);
                prover.push();
                prover.addConstraint(bvmgr.equal(x, zero));
                prover.addConstraint(bvmgr.equal(xShl1, zero));
                prover.addConstraint(bvmgr.equal(xShr1u, zero));
                prover.addConstraint(bvmgr.equal(xShr1s, zero));
                System.out.println("Shift sanity (x=0 => shifts are 0): isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 6: extend + extract should preserve low bits (always true), so negation is UNSAT.
                BitvectorFormula xZext16 = bvmgr.extend(x, 8, false); // 8 -> 16
                BitvectorFormula low8 = bvmgr.extract(xZext16, 7, 0);
                BooleanFormula lowBitsPreserved = bvmgr.equal(low8, x);
                prover.push();
                prover.addConstraint(bmgr.not(lowBitsPreserved));
                System.out.println("Zero-extend preserves low bits (negated): isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 7: arithmetic: (x - x) == 0 (always true), so negation is UNSAT.
                BooleanFormula subSelfIsZero = bvmgr.equal(bvmgr.subtract(x, x), zero);
                prover.push();
                prover.addConstraint(bmgr.not(subSelfIsZero));
                System.out.println("Arithmetic identity (negated): isUnsat = " + prover.isUnsat());
                prover.pop();

                // Example 8: arrays: select(store(A,i,v), i) == v (always true), so negation is UNSAT.
                FormulaType.BitvectorType idxType = FormulaType.getBitvectorTypeWithSize(8);
                FormulaType.BitvectorType elemType = FormulaType.getBitvectorTypeWithSize(8);
                ArrayFormula<BitvectorFormula, BitvectorFormula> a = amgr.makeArray("A", idxType, elemType);
                BitvectorFormula i = bvmgr.makeVariable(8, "i");
                ArrayFormula<BitvectorFormula, BitvectorFormula> a2 = amgr.store(a, i, x);
                BitvectorFormula sel = amgr.select(a2, i);
                BooleanFormula readAfterWrite = bvmgr.equal(sel, x);
                prover.push();
                prover.addConstraint(bmgr.not(readAfterWrite));
                System.out.println("Array read-after-write (negated): isUnsat = " + prover.isUnsat());
                prover.pop();
            }

            // Show an SMT-LIB dump for one constraint (STP backend supports printing).
            StringBuilder sb = new StringBuilder();
            fm.dumpFormula(yEqXPlus1).appendTo(sb);
            System.out.println("\nSMT-LIB (constraint y = x + 1):\n" + sb);
        }
    }
}
