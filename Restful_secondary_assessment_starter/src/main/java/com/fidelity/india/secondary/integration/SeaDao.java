package com.fidelity.india.secondary.integration;

import com.fidelity.india.secondary.models.Sea;

public interface SeaDao {

	Sea getPortName(String vessel);
}
