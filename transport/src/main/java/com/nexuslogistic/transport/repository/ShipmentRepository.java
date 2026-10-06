package com.nexuslogistic.transport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nexuslogistic.transport.model.Shipment;
import com.nexuslogistic.transport.model.ShipmentStatus;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
	long countByStatus(ShipmentStatus status);

	@EntityGraph(attributePaths = "customer")
	List<Shipment> findAllByOrderByUpdatedAtDesc();
}
