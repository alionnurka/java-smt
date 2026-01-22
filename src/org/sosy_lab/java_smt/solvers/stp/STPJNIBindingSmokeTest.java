package org.sosy_lab.java_smt.solvers.stp;

public final class STPJNIBindingSmokeTest {

    public static void main(String[] args) {
        System.out.println("=== STP JNI Binding Smoke Test ===");

        // 1. Create validity checker
        long vc = stpJNI.vc_createValidityChecker();
        if (vc == 0) {
            throw new AssertionError("vc_createValidityChecker returned 0");
        }
        System.out.println("Created VC: " + vc);

        // 2. Create boolean type
        long boolType = stpJNI.vc_boolType(vc);
        System.out.println("Boolean type: " + boolType);

        // 3. Create boolean variable x
        long x = stpJNI.vc_varExpr(vc, "x", boolType);
        System.out.println("Variable x: " + x);

        // 4. Create constant 'true'
        long tru = stpJNI.vc_trueExpr(vc);
        System.out.println("True constant: " + tru);

        // 5. Build constraint: x = true
        long eq = stpJNI.vc_eqExpr(vc, x, tru);
        System.out.println("Constraint (x = true): " + eq);

        // 6. Assert constraint
        stpJNI.vc_assertFormula(vc, eq);
        System.out.println("Constraint asserted");

        // 7. Query: is x satisfiable?
        int result = stpJNI.vc_query(vc, x);
        System.out.println("Query result: " + result);

        /*
         * STP convention:
         *   0 = VALID
         *   1 = INVALID
         *   2 = UNKNOWN
         *
         * Querying 'x' under x = true should be VALID.
         */
        if (result != 0) {
            throw new AssertionError("Unexpected solver result: " + result);
        }

        System.out.println("Result OK (VALID)");

        // 8. Cleanup
        stpJNI.vc_Destroy(vc);
        System.out.println("VC destroyed");

        System.out.println("=== JNI binding works correctly ===");
    }

    private STPJNIBindingSmokeTest() {}
}
