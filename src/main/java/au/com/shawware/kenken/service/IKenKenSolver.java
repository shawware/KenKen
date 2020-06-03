/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

/**
 * KenKen Solver API.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface IKenKenSolver
{
    void solve();
    
    boolean gridIsSolved();
    
    int[][] solution();
}
