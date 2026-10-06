package com.nexuslogistic.transport.web;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.nexuslogistic.transport.model.Customer;
import com.nexuslogistic.transport.model.Shipment;
import com.nexuslogistic.transport.model.ShipmentStatus;
import com.nexuslogistic.transport.repository.CustomerRepository;
import com.nexuslogistic.transport.repository.ShipmentRepository;

import jakarta.validation.Valid;

@Controller
public class DashboardController {

	private final CustomerRepository customerRepository;
	private final ShipmentRepository shipmentRepository;

	public DashboardController(CustomerRepository customerRepository, ShipmentRepository shipmentRepository) {
		this.customerRepository = customerRepository;
		this.shipmentRepository = shipmentRepository;
	}

	@GetMapping("/")
	public String dashboard(Model model) {
		List<Shipment> shipments = shipmentRepository.findAllByOrderByUpdatedAtDesc();
		model.addAttribute("shipments", shipments);
		model.addAttribute("customers", customerRepository.findAll(Sort.by("name")));
		model.addAttribute("customerForm", new Customer());
		model.addAttribute("shipmentForm", new Shipment());
		model.addAttribute("statuses", ShipmentStatus.values());
		model.addAttribute("customerCount", customerRepository.count());
		model.addAttribute("shipmentCount", shipmentRepository.count());
		model.addAttribute("transitCount", shipmentRepository.countByStatus(ShipmentStatus.IN_TRANSIT));
		model.addAttribute("delayedCount", shipmentRepository.countByStatus(ShipmentStatus.DELAYED));
		return "dashboard";
	}

	@PostMapping("/clientes")
	public String createCustomer(@Valid Customer customerForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (bindingResult.hasErrors()) {
			return dashboardWithErrors(model, bindingResult, "customer");
		}
		customerRepository.save(customerForm);
		redirectAttributes.addFlashAttribute("successMessage", "Cliente agregado correctamente.");
		return "redirect:/#clientes";
	}

	@PostMapping("/cargas")
	public String createShipment(@Valid Shipment shipmentForm, BindingResult bindingResult,
			@RequestParam("customerId") Long customerId, RedirectAttributes redirectAttributes, Model model) {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null) {
			bindingResult.rejectValue("customer", "customer.invalid", "Selecciona un cliente válido");
		} else {
			shipmentForm.setCustomer(customer);
		}
		if (bindingResult.hasErrors()) {
			return dashboardWithErrors(model, bindingResult, "shipment");
		}
		shipmentRepository.save(shipmentForm);
		redirectAttributes.addFlashAttribute("successMessage", "Carga agregada correctamente.");
		return "redirect:/#cargas";
	}

	@PostMapping("/cargas/{id}/estatus")
	public String updateStatus(@PathVariable Long id, @RequestParam ShipmentStatus status,
			RedirectAttributes redirectAttributes) {
		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la carga solicitada."));
		shipment.setStatus(status);
		shipmentRepository.save(shipment);
		redirectAttributes.addFlashAttribute("successMessage", "Estatus de carga actualizado.");
		return "redirect:/#cargas";
	}

	private String dashboardWithErrors(Model model, BindingResult bindingResult, String formName) {
		dashboard(model);
		model.addAttribute(formName + "Errors", bindingResult.getFieldErrors());
		model.addAttribute(formName + "Form", bindingResult.getTarget());
		return "dashboard";
	}
}
