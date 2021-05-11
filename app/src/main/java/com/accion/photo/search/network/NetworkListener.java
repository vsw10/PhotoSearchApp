package com.accion.photo.search.network;

import org.json.JSONObject;

/**
 * This class will be used for all network operations.
 * It will handle both Success and failure scenariosss
 */
public interface NetworkListener {
    // Method to handle the failure Scenario
    public void onFailure(String error);

    // Method to handle the Success Scenario
    public void onSuccess(JSONObject string);
}
