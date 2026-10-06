package com.nexuslogistic.transport.model;

public enum ShipmentStatus {
	PREPARING("Preparando"),
	IN_TRANSIT("En tránsito"),
	DELAYED("Con demora"),
	DELIVERED("Entregada");

	private final String label;

	ShipmentStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
