package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.PropertyValidator;
import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class GigScheduleTest extends EntityTest<GigSchedule> {

    @Override
    public void testDefaultConstructor() {
        GigSchedule schedule = getEmptyInstance();
        assertNull("schedule should have no gig", schedule.getGig());
        assertNull("Showup time should be empty", schedule.getShowupTime());
        assertNull("Time for sound check should be empty", schedule.getTimeForSoundcheck());
        assertNull("Opening time should be empty", schedule.getOpeningTime());
        assertNull("Time for show should be empty", schedule.getTimeForShow());
        assertNull("Closing time should be empty", schedule.getClosingTime());
    }

    @Override
    public void testConstructorWithParameters() {
        GigSchedule schedule = getPopulatedInstance();
        assertNotNull("Schedule should have a gig", schedule.getGig());
        assertEquals("Time for sound check should be 14:00", "14:00", schedule.getShowupTime());
        assertEquals("Opening time should be 18:00", "18:00", schedule.getTimeForSoundcheck());
        assertEquals("Opening time show should be 19:00", "19:00", schedule.getOpeningTime());
        assertEquals("Time for show should be 20:00", "20:00", schedule.getTimeForShow());
        assertEquals("Closing time show should be 02:00", "02:00", schedule.getClosingTime());
    }

    @Test
    public void testEmptyTimeAllowed() {
        GigSchedule schedule = getPopulatedInstance();
        schedule.setTimeForShow("");
        schedule.setClosingTime("");
        Set<ConstraintViolation<GigSchedule>> violations = PropertyValidator.getValidator().validateProperty(schedule, "timeForShow");
        assertTrue("Empty time and/or accidental space should still work", violations.isEmpty());
    }

    @Test
    public void testMalformedTime1() {
        GigSchedule schedule = getPopulatedInstance();
        schedule.setClosingTime("2200");
        Set<ConstraintViolation<GigSchedule>> violations = PropertyValidator.getValidator().validateProperty(schedule, "closingTime");
        assertTrue("There should be 1 violation", violations.size() == 1);
    }

    @Test
    public void testMalformedTime2() {
        GigSchedule schedule = getPopulatedInstance();
        schedule.setClosingTime("22");
        Set<ConstraintViolation<GigSchedule>> violations = PropertyValidator.getValidator().validateProperty(schedule, "closingTime");
        assertTrue("There should be 1 violation", violations.size() == 1);
    }

    @Test
    public void testMalformedTime3() {
        GigSchedule schedule = getPopulatedInstance();
        schedule.setClosingTime("hh:22");
        Set<ConstraintViolation<GigSchedule>> violations = PropertyValidator.getValidator().validateProperty(schedule, "closingTime");
        assertTrue("There should be 1 violation", violations.size() == 1);
    }

    @Override
    public void testRelations() {
        GigSchedule schedule = getPopulatedInstance();
        schedule.setGig(null);
        Gig gig = TestFixture.getValidGig("Nirvana");
        gig.setSchedule(schedule);
        gig.setRelations();

        assertNotNull("schedule should now be assigned a gig", schedule.getGig());
        assertTrue("schedules assigned gig is the same as the gig", schedule.getGig().equals(gig));
    }

    @Override
    public void testCopyData() {
        GigSchedule populatedSchedule = getPopulatedInstance();
        populatedSchedule.setId(666L);

        GigSchedule emptySchedule = getEmptyInstance();
        emptySchedule.copyDataFromEntity(populatedSchedule);

        assertTrue("Empty Schedule gig should now be same as populated schedules gig", emptySchedule.getGig().equals(populatedSchedule.getGig()));
        assertEquals("Empty Schedule Time for sound check should show be 14:00", "14:00", emptySchedule.getShowupTime());
        assertEquals("Empty Schedule Opening time should show be 18:00", "18:00", emptySchedule.getTimeForSoundcheck());
        assertEquals("Empty Schedule Opening time show should show be 19:00", "19:00", emptySchedule.getOpeningTime());
        assertEquals("Empty Schedule Time for show should show be " + TestFixture.FAKE_TIME, TestFixture.FAKE_TIME, emptySchedule.getTimeForShow());
        assertEquals("Empty Schedule Closing time show should be 02:00", "02:00", emptySchedule.getClosingTime());
    }

    @Override
    protected GigSchedule getEmptyInstance() {
        return new GigSchedule();
    }

    @Override
    protected GigSchedule getPopulatedInstance() {
        return TestFixture.getValidSchedule(TestFixture.FAKE_TIME);
    }

}
