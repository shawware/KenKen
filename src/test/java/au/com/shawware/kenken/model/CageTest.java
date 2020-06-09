/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.model;

import org.junit.Test;

import au.com.shawware.kenken.AbstractBaseTest;

import static au.com.shawware.kenken.model.Cage.PLUS;
import static au.com.shawware.kenken.model.Cage.TIMES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify the {@link Cage} class.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class CageTest extends AbstractBaseTest
{
    @Test
    public void testCage()
    {
        Cage cage = new Cage(TIMES, 20, buildSquares(new int[][] {{0, 0}, {0, 1}}));
        cage.setId(18);

        assertEquals(18, cage.getId());
        assertEquals(TIMES, cage.getOperation());
        assertEquals(20, cage.getValue());
        assertEquals(2, cage.getSize());
        assertTrue(cage.toString().contains("Plain")); // Verify cage type
    }

    @Test
    public void testRow()
    {
        Cage cage = new Row(3, 10, buildSquares(new int[][] {{0, 0}, {0, 1}}));

        assertEquals(3, cage.getId());
        assertEquals(PLUS, cage.getOperation());
        assertEquals(10, cage.getValue());
        assertEquals(2, cage.getSize());
        assertTrue(cage.toString().contains("Row")); // Verify cage type
    }

    @Test
    public void testColumn()
    {
        Cage cage = new Column(2, 6, buildSquares(new int[][] {{1, 0}, {1, 1}, {1, 2}}));

        assertEquals(2, cage.getId());
        assertEquals(PLUS, cage.getOperation());
        assertEquals(6, cage.getValue());
        assertEquals(3, cage.getSize());
        assertTrue(cage.toString().contains("Column")); // Verify cage type
    }

    @Test
    public void testCombo()
    {
        Cage c1 = buildCage(PLUS, 5, new int[][] {{ 0, 0 }, { 0, 1 }, {0, 2}});
        Cage c2 = buildCage(PLUS, 4, new int[][] {{ 1, 0 }, { 1, 1 }});
        Cage c3 = buildCage(PLUS, 7, new int[][] {{ 2, 0 }, { 2, 1 }, {2, 2}, {2 ,3}});

        c1.setId(7);
        c2.setId(12);
        c3.setId(3);

        Cage cage = new Combo(100, c1, c2, c3);

        assertEquals(100, cage.getId());
        assertEquals(PLUS, cage.getOperation());
        assertEquals(16, cage.getValue());
        assertEquals(9, cage.getSize());
        assertTrue(cage.toString().contains("Combo")); // Verify cage type
        assertTrue(cage.toString().contains("[3, 7, 12]")); // Verify cage IDs
        cage.getSquares().forEach(square -> {
            assertTrue(
                    c1.getSquares().stream().anyMatch(s -> s.equals(square)) ||
                    c2.getSquares().stream().anyMatch(s -> s.equals(square)) ||
                    c3.getSquares().stream().anyMatch(s -> s.equals(square))
           );
        });
    }
}
