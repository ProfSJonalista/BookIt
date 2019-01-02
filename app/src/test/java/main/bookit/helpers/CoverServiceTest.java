package main.bookit.helpers;

import org.junit.Test;

import main.bookit.R;

import static org.junit.Assert.*;

public class CoverServiceTest {

    @Test
    public void getCover() {
        CoverService coverService = new CoverService();
        int coverId = coverService.getCover("85rrGvXHafx5pc49VL9Fvca");

        assertEquals(coverId, R.drawable.cb_the_elephant_vanishes_haruki_murakami);
    }
}