package com.nexuslogistic.transport.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nexuslogistic.transport.model.Customer;
import com.nexuslogistic.transport.model.Shipment;
import com.nexuslogistic.transport.model.ShipmentStatus;
import com.nexuslogistic.transport.repository.CustomerRepository;
import com.nexuslogistic.transport.repository.ShipmentRepository;

@Configuration
public class DemoDataConfiguration {

	@Bean
	ApplicationRunner loadDemoData(CustomerRepository customers, ShipmentRepository shipments) {
		return args -> {
			if (customers.count() > 0 || shipments.count() > 0) {
				return;
			}

			Customer norte = customers.save(new Customer("Comercializadora del Norte", "compras@norte.mx", "81 5555 0142"));
			Customer verde = customers.save(new Customer("Alimentos Campo Verde", "logistica@campoverde.mx", "33 5555 0278"));
			Customer industrial = customers.save(new Customer("Industrial del Bajío", "trafico@industrialbajio.mx", "477 555 0316"));

			shipments.save(new Shipment("NL-26041", "Tarimas de producto empacado", "Monterrey, NL",
					"San Luis Potosí, SLP", 22.1565, -100.9855, ShipmentStatus.IN_TRANSIT, norte));
			shipments.save(new Shipment("NL-26042", "Insumos para producción", "Guadalajara, JAL",
					"León, GTO", 21.1220, -101.6830, ShipmentStatus.DELAYED, verde));
			shipments.save(new Shipment("NL-26043", "Equipo industrial", "Querétaro, QRO",
					"Aguascalientes, AGS", 20.5888, -100.3899, ShipmentStatus.PREPARING, industrial));
			shipments.save(new Shipment("NL-26044", "Alimentos no perecederos", "Saltillo, COAH",
					"Monterrey, NL", 25.6866, -100.3161, ShipmentStatus.DELIVERED, norte));
		};
	}
}
