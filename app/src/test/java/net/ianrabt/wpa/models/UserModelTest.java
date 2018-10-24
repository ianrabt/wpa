package net.ianrabt.wpa.models;
import org.junit.Test;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UserModelTest {
    private UserModel testUser;
    private HabitModel habit;
    private List<HabitModel> habits;

    @Test
    public void ConstructorAndGetUsernameTest(){
        testUser = new UserModel("lazyboi");

        assertThat(testUser.getUsername(), is("lazyboi"));
    }

    @Test
    public void getHabitsTestEmpty(){
        testUser = new UserModel("lazyboi");

        assertThat(testUser.getHabits().size(), is(0));
    }

    @Test
    public void getHabitsAndAddHabitTestOneElement() {
        testUser = new UserModel("lazyboi");
        habit = new HabitModel("Workout");

        testUser.addHabit(habit);

        habits = testUser.getHabits();

        assertThat(habits.size(), is(1));
    }
}