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

    }


    @Test
    public void GetStreakTest() {

        assertThat(habitCell.getStreakCounter(), is(0));
    }


}