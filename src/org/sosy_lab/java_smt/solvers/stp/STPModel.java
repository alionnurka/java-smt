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

    /**
     * Iterate over all values present in the model. Note that iterating multiple times may be
     * inefficient for some solvers, it is recommended to use {@link
     * BasicProverEnvironment#getModelAssignments()} instead in this case.
     *
     * <p>The iteration includes value assignments for...
     *
     * <ul>
     *   <li>all relevant free variables of simple type. If a variable is irrelevant for
     *       satisfiability, it can be <code>null</code> or missing in the iteration.
     *   <li>(nested) arrays with all accesses. If an array access is applied within a quantified
     *       context, some value assignments can be missing in the iteration. Please use a direct
     *       evaluation query to get the evaluation in such a case.
     *   <li>uninterpreted functions with all applications. If an uninterpreted function is applied
     *       within a quantified context, some value assignments can be missing in the iteration.
     *       Please use a direct evaluation query to get the evaluation in such a case.
     * </ul>
     */
    @Override
    public Iterator<ValueAssignment> iterator() {
        return super.iterator();
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Actions are performed in the order of iteration, if that
     * order is specified.  Exceptions thrown by the action are relayed to the
     * caller.
     * <p>
     * The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super ValueAssignment> action) {
        super.forEach(action);
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @implSpec The default implementation creates an
     * <em><a href="../util/Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     * @implNote The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     * @since 1.8
     */
    @Override
    public Spliterator<ValueAssignment> spliterator() {
        return super.spliterator();
    }

    /**
     * Returns a list of model assignments that remains valid after the model is closed (via {@link
     * Model#close()}).
     *
     * <p>The returned {@link ImmutableList} contains the same {@link ValueAssignment} elements that
     * {@link #iterator()} would provide, but it is a materialized copy such that the list and its
     * elements can still be accessed safely after the model has been closed. Methods that rely on
     * live solver state such as {@link #iterator()} or {@link #evaluate(Formula)} should not be used
     * after {@link #close()}, whereas the returned list can always be used, until the underlying
     * solver context is closed ({@link SolverContext#close()}).
     *
     * <p>This representation is primarily intended for model inspection and debugging. For precise
     * evaluation of individual formulas prefer targeted calls to {@link #evaluate(Formula)}.
     */
    @Override
    public ImmutableList<ValueAssignment> asList() {
        return null;
    }

    /**
     * Simplify the given formula and replace all symbols with their model values. If a symbol is not
     * set in the model and evaluation aborts, return <code>null</code>.
     *
     * @param formula
     */
    @Override
    protected @Nullable Long evalImpl(Long formula) {
        return 0L;
    }
}
