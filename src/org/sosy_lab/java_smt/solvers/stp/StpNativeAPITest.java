package org.sosy_lab.java_smt.solvers.stp;

import static com.google.common.truth.Truth.assertThat;

import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sosy_lab.common.NativeLibraries;

public class StpNativeAPITest {
    @BeforeClass
public static void load() {
  try {
    NativeLibraries.loadLibrary("javasmtstp");
  } catch (UnsatisfiedLinkError e) {
    throw new AssumptionViolatedException("STP is not available", e);
  }
}

@Test
public void basicSmokeTest() {
  long vc = StpJNI.vc_createValidityChecker();
  long boolType = StpJNI.vc_boolType(vc);
  long x = StpJNI.vc_varExpr(vc, "x", boolType);
  long truth = StpJNI.vc_trueExpr(vc);
  StpJNI.vc_assertFormula(vc, StpJNI.vc_eqExpr(vc, x, truth));
  int result = StpJNI.vc_query(vc, StpJNI.vc_eqExpr(vc, x, truth));
  StpJNI.vc_Destroy(vc);
  assertThat(result).isEqualTo(1); // 1 = VALID
}
}