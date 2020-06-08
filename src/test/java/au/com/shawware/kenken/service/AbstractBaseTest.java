/*
 * Copyright (C) 2020 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.kenken.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;

import au.com.shawware.kenken.model.GridSpecification;

/**
 * Base class for tests.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
abstract class AbstractBaseTest
{
    private GridSpecificationLoader loader;
    private ClassLoader classLoader;

    @Before
    public void commonSetUp()
    {
        loader = new GridSpecificationLoader();
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    protected final GridSpecification loadGridSpecification(String filename) throws IOException
    {
        try (InputStream stream = classLoader.getResourceAsStream(filename))
        {
            Reader reader = new InputStreamReader(stream);
            return loader.loadGridSpecification(reader);
        }
        catch (IOException e)
        {
            throw e;
        }
    }
    
    protected final GridSpecification readGridSpecification(String specification) throws IOException
    {
        try (Reader reader = new StringReader(specification))
        {
            return loader.loadGridSpecification(reader);
        }
        catch (IOException e)
        {
            throw e;
        }
    }
}
