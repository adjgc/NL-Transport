package com.nexuslogistic.transport.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "shipments")
public class Shipment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El número de guía es obligatorio")
	@Size(max = 40)
	private String trackingNumber;

	@NotBlank(message = "La descripción es obligatoria")
	@Size(max = 160)
	private String description;

	@NotBlank(message = "El origen es obligatorio")
	@Size(max = 100)
	private String origin;

	@NotBlank(message = "El destino es obligatorio")
	@Size(max = 100)
	private String destination;

	@NotNull(message = "La latitud es obligatoria")
	@DecimalMin(value = "-90.0")
	@DecimalMax(value = "90.0")
	private Double latitude;

	@NotNull(message = "La longitud es obligatoria")
	@DecimalMin(value = "-180.0")
	@DecimalMax(value = "180.0")
	private Double longitude;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ShipmentStatus status = ShipmentStatus.PREPARING;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	private LocalDateTime updatedAt;

	public Shipment() {
	}

	public Shipment(String trackingNumber, String description, String origin, String destination,
			Double latitude, Double longitude, ShipmentStatus status, Customer customer) {
		this.trackingNumber = trackingNumber;
		this.description = description;
		this.origin = origin;
		this.destination = destination;
		this.latitude = latitude;
		this.longitude = longitude;
		this.status = status;
		this.customer = customer;
	}

	@PrePersist
	@PreUpdate
	void updateTimestamp() {
		updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
