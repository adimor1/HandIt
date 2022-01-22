package com.example.myapplication;

import org.json.JSONException;

public interface TaskListener {
    public void taskComplete(boolean status) throws JSONException;
}