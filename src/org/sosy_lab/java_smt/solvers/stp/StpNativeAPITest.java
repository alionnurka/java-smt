package org.sosy_lab.java_smt.solvers.stp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.math.BigInteger;
import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sosy_lab.common.NativeLibraries;
import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.BasicLogManager;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.java_smt.SolverContextFactory;
import org.sosy_lab.java_smt.SolverContextFactory.Solvers;
import org.sosy_lab.java_smt.api.ArrayFormula;
import org.sosy_lab.java_smt.api.ArrayFormulaManager;
import org.sosy_lab.java_smt.api.BitvectorFormula;
import org.sosy_lab.java_smt.api.BitvectorFormulaManager;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.FormulaManager;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;

public class StpNativeAPITest {
  @BeforeClass
  public static void load() {
    try {
      NativeLibraries.loadLibrary("javasmtstp");
    } catch (UnsatisfiedLinkError e) {
      throw new AssumptionViolatedException("STP is not available", e);
    }
  }

  private static SolverContext newStpContext() throws InvalidConfigurationException {
    Configuration config = Configuration.defaultConfiguration();
    LogManager logger = BasicLogManager.create(config);
    ShutdownNotifier notifier = ShutdownNotifier.createDummy();
    return SolverContextFactory.createSolverContext(config, logger, notifier, Solvers.STP);
  }

  @Test
  public void nativeBasicSmokeTest() {
    long vc = StpJNI.vc_createValidityChecker();
    long boolType = StpJNI.vc_boolType(vc);
    long x = StpJNI.vc_varExpr(vc, "x", boolType);
    long truth = StpJNI.vc_trueExpr(vc);
    StpJNI.vc_assertFormula(vc, StpJNI.vc_eqExpr(vc, x, truth));
    int result = StpJNI.vc_query(vc, StpJNI.vc_eqExpr(vc, x, truth));
    StpJNI.vc_Destroy(vc);
    assertThat(result).isEqualTo(1); // 1 = VALID
  }

  @Test
  public void booleanManagerXorSelfIsUnsat() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
      BooleanFormula b = bmgr.makeVariable("b");

      prover.push();
      prover.addConstraint(bmgr.xor(b, b)); // always false
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void booleanManagerIfThenElseMatchesCondition() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
      BooleanFormula c = bmgr.makeVariable("c");

      // ite(c, true, false) <-> c is a tautology, so its negation is UNSAT.
      BooleanFormula ite = bmgr.ifThenElse(c, bmgr.makeTrue(), bmgr.makeFalse());
      BooleanFormula eq = bmgr.equivalence(ite, c);

      prover.push();
      prover.addConstraint(bmgr.not(eq));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void booleanManagerDeMorganIsTautology() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
      BooleanFormula p = bmgr.makeVariable("p");
      BooleanFormula q = bmgr.makeVariable("q");

      // !(p & q) <-> (!p | !q) is a tautology, so its negation is UNSAT.
      BooleanFormula lhs = bmgr.not(bmgr.and(p, q));
      BooleanFormula rhs = bmgr.or(bmgr.not(p), bmgr.not(q));
      BooleanFormula deMorgan = bmgr.equivalence(lhs, rhs);

      prover.push();
      prover.addConstraint(bmgr.not(deMorgan));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void bitvectorArithmeticAndBitwiseIdentities() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
      BitvectorFormulaManager bvmgr = fmgr.getBitvectorFormulaManager();

      BitvectorFormula x = bvmgr.makeVariable(8, "x");
      BitvectorFormula zero = bvmgr.makeBitvector(8, 0);
      BitvectorFormula allOnes = bvmgr.makeBitvector(8, 255);

      // (x - x) == 0 is always true, so negation is UNSAT.
      prover.push();
      prover.addConstraint(bmgr.not(bvmgr.equal(bvmgr.subtract(x, x), zero)));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();

      // (x XOR x) == 0 is always true, so negation is UNSAT.
      prover.push();
      prover.addConstraint(bmgr.not(bvmgr.equal(bvmgr.xor(x, x), zero)));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();

      // x & ~x == 0 is always true, so negation is UNSAT.
      prover.push();
      prover.addConstraint(bmgr.not(bvmgr.equal(bvmgr.and(x, bvmgr.not(x)), zero)));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();

      // x | ~x == allOnes is always true, so negation is UNSAT.
      prover.push();
      prover.addConstraint(bmgr.not(bvmgr.equal(bvmgr.or(x, bvmgr.not(x)), allOnes)));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void bitvectorConcreteArithmeticUnsigned() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BitvectorFormulaManager bvmgr = fmgr.getBitvectorFormulaManager();

      BitvectorFormula six = bvmgr.makeBitvector(8, 6);
      BitvectorFormula three = bvmgr.makeBitvector(8, 3);
      BitvectorFormula two = bvmgr.makeBitvector(8, 2);
      BitvectorFormula seven = bvmgr.makeBitvector(8, 7);
      BitvectorFormula one = bvmgr.makeBitvector(8, 1);
      BitvectorFormula twentyOne = bvmgr.makeBitvector(8, 21);

      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.divide(six, three, false), two));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.remainder(seven, three, false), one));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.smodulo(seven, three), one));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.multiply(seven, three), twentyOne));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();
    }
  }

  @Test
  public void bitvectorConcreteSignedDivisionAndComparison() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BitvectorFormulaManager bvmgr = fmgr.getBitvectorFormulaManager();

      BitvectorFormula negSix = bvmgr.makeBitvector(8, BigInteger.valueOf(-6));
      BitvectorFormula three = bvmgr.makeBitvector(8, 3);
      BitvectorFormula negTwo = bvmgr.makeBitvector(8, BigInteger.valueOf(-2));

      // (-6) /s 3 == -2
      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.divide(negSix, three, true), negTwo));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      // signed: (-1) < 1
      BitvectorFormula negOne = bvmgr.makeBitvector(8, BigInteger.valueOf(-1));
      BitvectorFormula one = bvmgr.makeBitvector(8, 1);
      prover.push();
      prover.addConstraint(bvmgr.lessThan(negOne, one, true));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      // unsigned: 255 < 1 is false, so asserting it is UNSAT.
      BitvectorFormula u255 = bvmgr.makeBitvector(8, 255);
      prover.push();
      prover.addConstraint(bvmgr.lessThan(u255, one, false));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void bitvectorConcatExtractExtendShift() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BitvectorFormulaManager bvmgr = fmgr.getBitvectorFormulaManager();

      BitvectorFormula hi = bvmgr.makeBitvector(4, 0xA);
      BitvectorFormula lo = bvmgr.makeBitvector(4, 0x5);
      BitvectorFormula x = bvmgr.concat(hi, lo); // 0xA5

      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.extract(x, 7, 4), hi));
      prover.addConstraint(bvmgr.equal(bvmgr.extract(x, 3, 0), lo));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      BitvectorFormula one = bvmgr.makeBitvector(8, 1);
      BitvectorFormula two = bvmgr.makeBitvector(8, 2);
      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.shiftLeft(one, one), two)); // 1 << 1 == 2
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      // shift-right with constants (unsigned and signed).
      BitvectorFormula u254 = bvmgr.makeBitvector(8, 254);
      BitvectorFormula u127 = bvmgr.makeBitvector(8, 127);
      BitvectorFormula negTwo = bvmgr.makeBitvector(8, BigInteger.valueOf(-2));
      BitvectorFormula negOne = bvmgr.makeBitvector(8, BigInteger.valueOf(-1));
      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.shiftRight(u254, one, false), u127)); // 254 >>u 1 == 127
      prover.addConstraint(bvmgr.equal(bvmgr.shiftRight(negTwo, one, true), negOne)); // -2 >>s 1 == -1
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      // zero-extend and extract low bits preserves original value.
      BitvectorFormula zext16 = bvmgr.extend(x, 8, false); // 8 -> 16
      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.extract(zext16, 7, 0), x));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();

      // sign-extend -1 keeps all upper bits set.
      BitvectorFormula neg1_8 = bvmgr.makeBitvector(8, BigInteger.valueOf(-1));
      BitvectorFormula sext = bvmgr.extend(neg1_8, 8, true); // 8 -> 16
      prover.push();
      prover.addConstraint(bvmgr.equal(bvmgr.extract(sext, 15, 8), neg1_8));
      assertThat(prover.isUnsat()).isFalse();
      prover.pop();
    }
  }

  @Test
  public void arrayReadAfterWriteIsTautology() throws Exception {
    try (SolverContext context = newStpContext();
        ProverEnvironment prover = context.newProverEnvironment()) {
      FormulaManager fmgr = context.getFormulaManager();
      BooleanFormulaManager bmgr = fmgr.getBooleanFormulaManager();
      BitvectorFormulaManager bvmgr = fmgr.getBitvectorFormulaManager();
      ArrayFormulaManager amgr = fmgr.getArrayFormulaManager();

      FormulaType.BitvectorType idxType = FormulaType.getBitvectorTypeWithSize(8);
      FormulaType.BitvectorType elemType = FormulaType.getBitvectorTypeWithSize(8);
      ArrayFormula<BitvectorFormula, BitvectorFormula> a = amgr.makeArray("A", idxType, elemType);
      BitvectorFormula i = bvmgr.makeVariable(8, "i");
      BitvectorFormula v = bvmgr.makeVariable(8, "v");

      ArrayFormula<BitvectorFormula, BitvectorFormula> a2 = amgr.store(a, i, v);
      BitvectorFormula sel = amgr.select(a2, i);
      BooleanFormula readAfterWrite = bvmgr.equal(sel, v);

      // Negation of read-after-write is UNSAT.
      prover.push();
      prover.addConstraint(bmgr.not(readAfterWrite));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();

      // Reflexivity of array equivalence: (a2 == a2) is always true.
      prover.push();
      prover.addConstraint(bmgr.not(amgr.equivalence(a2, a2)));
      assertThat(prover.isUnsat()).isTrue();
      prover.pop();
    }
  }

  @Test
  public void unsupportedToIntegerTheoryIsRejected() throws InvalidConfigurationException {
    try (SolverContext context = newStpContext()) {
      BitvectorFormulaManager bvmgr = context.getFormulaManager().getBitvectorFormulaManager();
      BitvectorFormula x = bvmgr.makeVariable(8, "x");
      assertThrows(UnsupportedOperationException.class, () -> bvmgr.toIntegerFormula(x, false));
    }
  }

  @Test
  public void constantArraysAreNotSupported() throws InvalidConfigurationException {
    try (SolverContext context = newStpContext()) {
      ArrayFormulaManager amgr = context.getFormulaManager().getArrayFormulaManager();
      BitvectorFormulaManager bvmgr = context.getFormulaManager().getBitvectorFormulaManager();
      FormulaType.BitvectorType idxType = FormulaType.getBitvectorTypeWithSize(8);
      FormulaType.BitvectorType elemType = FormulaType.getBitvectorTypeWithSize(8);
      BitvectorFormula defaultElem = bvmgr.makeBitvector(8, 0);

      assertThrows(
          UnsupportedOperationException.class, () -> amgr.makeArray(idxType, elemType, defaultElem));
    }
  }
}