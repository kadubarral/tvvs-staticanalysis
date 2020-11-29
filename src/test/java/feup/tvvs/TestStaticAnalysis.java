package feup.tvvs;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStaticAnalysis{

    @Test
    public void testInsertAndPrint() {
        assertEquals(Integer.valueOf(10), staticanalysis.insertAndPrint());
    }
}