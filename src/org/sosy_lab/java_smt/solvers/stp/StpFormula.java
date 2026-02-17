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

import org.sosy_lab.java_smt.api.Formula;

public class StpFormula implements Formula {
    private long expr;

    public StpFormula(long expr) {
        this.expr = expr;
    }

    public long getExpr() {
        return expr;
    }
}
