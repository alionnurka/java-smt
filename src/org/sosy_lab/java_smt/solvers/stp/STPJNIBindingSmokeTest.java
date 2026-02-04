package org.sosy_lab.java_smt.solvers.stp;

public final class STPJNIBindingSmokeTest {

    public static void main(String[] args) {
        System.out.println("=== STP validity / implication semantics test ===");

        /*
         * Case 1:
         *   Assertions:  x
         *   Query:       false
         *
         *   This checks: x ⇒ false
         *   Expected: INVALID (because x=true is a counterexample)
         */
        {
            long vc = stpJNI.vc_createValidityChecker();
            long bool = stpJNI.vc_boolType(vc);
            long x = stpJNI.vc_varExpr(vc, "x", bool);

            stpJNI.vc_assertFormula(vc, x);
            int result = stpJNI.vc_query(vc, stpJNI.vc_falseExpr(vc));

            System.out.println("Case 1: check x ⇒ false");
            assert result == 1 : "Expected INVALID (SAT), got " + result;

            stpJNI.vc_Destroy(vc);
        }

        /*
         * Case 2:
         *   Assertions:  x, ¬x
         *   Query:       false
         *
         *   This checks: (x ∧ ¬x) ⇒ false
         *   Expected: VALID (because assertions are contradictory)
         */
        {
            long vc = stpJNI.vc_createValidityChecker();
            long bool = stpJNI.vc_boolType(vc);
            long x = stpJNI.vc_varExpr(vc, "x", bool);

            stpJNI.vc_assertFormula(vc, x);
            stpJNI.vc_assertFormula(vc, stpJNI.vc_notExpr(vc, x));

            int result = stpJNI.vc_query(vc, stpJNI.vc_falseExpr(vc));

            System.out.println("Case 2: check (x ∧ ¬x) ⇒ false");
            assert result == 0 : "Expected VALID (UNSAT), got " + result;

            stpJNI.vc_Destroy(vc);
        }

        /*
         * Case 3:
         *   Assertions:  x
         *   Query:       x
         *
         *   This checks: x ⇒ x
         *   Expected: VALID (tautological implication)
         */
        {
            long vc = stpJNI.vc_createValidityChecker();
            long bool = stpJNI.vc_boolType(vc);
            long x = stpJNI.vc_varExpr(vc, "x", bool);

            stpJNI.vc_assertFormula(vc, x);
            int result = stpJNI.vc_query(vc, x);

            System.out.println("Case 3: check x ⇒ x");
            assert result == 0 : "Expected VALID, got " + result;

            stpJNI.vc_Destroy(vc);
        }

        {
            long vc = stpJNI.vc_createValidityChecker();
            long bool = stpJNI.vc_boolType(vc);
            long x = stpJNI.vc_varExpr(vc, "x", bool);

            // Base context: no assertions
            int r0 = stpJNI.vc_query(vc, stpJNI.vc_falseExpr(vc));
            // Should be INVALID (SAT), because empty context is satisfiable
            assert r0 == 1;

            // Push
            stpJNI.vc_push(vc);

            // Add contradiction
            stpJNI.vc_assertFormula(vc, x);
            stpJNI.vc_assertFormula(vc, stpJNI.vc_notExpr(vc, x));

            int r1 = stpJNI.vc_query(vc, stpJNI.vc_falseExpr(vc));
            // Should be VALID (UNSAT)
            assert r1 == 0;

            // Pop
            stpJNI.vc_pop(vc);

            int r2 = stpJNI.vc_query(vc, stpJNI.vc_falseExpr(vc));
            // If pop works like SMT pop, this should be INVALID again
            assert r2 == 1;

            stpJNI.vc_Destroy(vc);
        }



        System.out.println("=== STP implication semantics verified ===");
    }

    private STPJNIBindingSmokeTest() {}
}
