package net.ianrabt.wpa;

import net.ianrabt.wpa.models.HabitModel;

import java.util.ArrayList;

public interface FBRepositoryDelegate {

    public void handleHabitResponse(ArrayList<HabitModel> habitResponse);
    public void render();
}
