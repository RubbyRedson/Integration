package ru.riskgap.integration.parsing;

import org.junit.Test;
import ru.riskgap.integration.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for parser
 * Created by Nikita on 03.07.2015.
 */

public class RequestParserTest {
    //unit test, no spring autowiring
    RequestParser requestParser = new RequestParser();

    private void test(String input, Task expected) throws IOException {
        Task actual = requestParser.parse(input);
        assertEquals("", expected, actual);
    }

    @Test
    public void testOneRequest() {

    }

}
