package net.ianrabt.wpa.models;
import org.junit.Test;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HabitCellModelTest {
    private HabitCellModel habitCell;
    private Date habitDate;

    @Test
    public void ConstructorTest(){
        habitDate = new Date();
        habitCell = new HabitCellModel("Workout", habitDate);

        assertThat(habitCell.getHabitName(), is("Workout"));
    }

    @Test
    public void GetHabitDateTest() {
        long secondsSinceEpoch = 1539814946;
        habitDate = new Date(secondsSinceEpoch*1000);
        habitCell = new HabitCellModel("Go to CS 121", habitDate);

        Date dateReturn = habitCell.getHabitDate();
        LocalDate localDate = dateReturn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        assertThat(year, is(2018));
        assertThat(month, is(10));
        assertThat(day, is(17));
    }

    @Test
    public void GetStreakTest() {
        habitDate = new Date();
        habitCell = new HabitCellModel("Drink Water", habitDate);

        assertThat(habitCell.getStreakCounter(), is(0));
    }


}