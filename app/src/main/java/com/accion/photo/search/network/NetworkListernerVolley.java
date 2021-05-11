package com.accion.photo.search.network;

import org.json.JSONObject;

public interface NetworkListernerVolley extends NetworkListener {
    // Method to handle the Success Scenario
    public void onSuccess(String string);
}
