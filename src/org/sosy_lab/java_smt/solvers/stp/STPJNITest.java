/*
 * This file is part of JavaSMT,
 * an API wrapper for a collection of SMT solvers:
 * https://github.com/sosy-lab/java-smt
 *
 * SPDX-FileCopyrightText: 2024 Dirk Beyer <https://www.sosy-lab.org>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.sosy_lab.java_smt.solvers.stp;

public class STPJNITest {

    public static void main(String[] args) {
        long vc = STPJNI.createVC();
        if (vc == 0) {
            throw new AssertionError("VC creation failed");
        }
        System.out.println("STP VC pointer: " + vc);
        STPJNI.destroyVC(vc);
    }
}
