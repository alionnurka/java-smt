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

import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.java_smt.api.BooleanFormulaManager;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.solvers.boolector.BoolectorFormulaCreator;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class STPTheoremProver extends STPAbstractProver<Void> implements ProverEnvironment {
    protected STPTheoremProver(Set<SolverContext.ProverOptions> pOptions, BooleanFormulaManager pBmgr, ShutdownNotifier pShutdownNotifier) {
        super(pOptions, pBmgr, pShutdownNotifier);
    }
}
