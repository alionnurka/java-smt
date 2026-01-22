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

import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.sosy_lab.java_smt.basicimpl.AbstractModel;
import org.sosy_lab.java_smt.basicimpl.AbstractProver;
import org.sosy_lab.java_smt.basicimpl.FormulaCreator;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class STPModel extends AbstractModel<Long, Long, Long> {
    protected STPModel(AbstractProver<?> prover, FormulaCreator<Long, Long, Long, ?> creator) {
        super(prover, creator);
    }

    @Override
    protected @Nullable Long evalImpl(Long formula) {
        return 0L;
    }


    @Override
    public ImmutableList<ValueAssignment> asList() {
        return null;
    }
}
