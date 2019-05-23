package com.example.admincik.pedometer.sensor;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SensorFilterTest {

    private float[] array = {1, 2, 3};
    private float[] array2 = {3, 2, 1};

    @Test
    public void sum() {
        assertEquals(6, SensorFilter.sum(array), 0);
    }

    @Test
    public void norm() {
        assertEquals(3.7416574954986572, SensorFilter.norm(array), 0);
    }

    @Test
    public void dot() {
        assertEquals(10, SensorFilter.dot(array, array2), 0);
    }

}