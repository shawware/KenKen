/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import au.com.shawware.kenken.model.Grid;
import au.com.shawware.kenken.model.GridSpecification;

/**
 * KenKen Solver API.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface IKenKenSolver
{
    Grid solve(GridSpecification specification);

    Grid solve(GridSpecification specification, IKenKenSolverObserver observer);
}
