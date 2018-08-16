package com.compaby.task.module;

import com.company.task.common.io.InputReader;
import com.company.task.common.io.Writer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
public class BaseTestConfig {

    protected static final String UNSUPPORTED_COMMAND = "unsupported_command_";


    protected InputReader reader;

    protected Writer writer;

    @Before
    public void beforeTest() {
        reader = mock(InputReader.class);
    }

    @Test
    public void emptyTest() {
    }

}
