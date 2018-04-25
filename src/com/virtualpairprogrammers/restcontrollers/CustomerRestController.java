package com.virtualpairprogrammers.restcontrollers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtualpairprogrammers.domain.Customer;
import com.virtualpairprogrammers.services.customers.CustomerManagementService;
import com.virtualpairprogrammers.services.customers.CustomerNotFoundException;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerManagementService customerService;
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ClientErrorInformation> rulesForCustomerNotFound(HttpServletRequest req, Exception e) {
		
		// return a representation of the the error
		ClientErrorInformation error = new ClientErrorInformation(e.toString(), req.getRequestURI());
		return new ResponseEntity<ClientErrorInformation>(error, HttpStatus.NOT_FOUND);
		
	}
	
	//We want to support GETs to /customer/373737
	//@RequestMapping(value="/customer/{id}", headers = {"Accept=application/xml,application/json"})
	@RequestMapping(value="/customer/{id}")
	public Customer findCustomerById(@PathVariable String id) throws CustomerNotFoundException {
		
		return customerService.getFullCustomerDetail(id);
	
	}
	
	
	/**
	 * Requirement: Only return customers.
	 * @return
	 */
	@RequestMapping(value="/customers")
	public CustomerCollectionRepresentation returnAllCustomers(@RequestParam(required=false) Integer first, 
															   @RequestParam(required=false) Integer last){
		
		List<Customer> allCustomers = customerService.getAllCustomers();
		for (Customer next: allCustomers) {
			next.setCalls(null);
		}
		
		if (first !=null & last!=null) {		
			return new CustomerCollectionRepresentation (allCustomers.subList(first-1, last));
	 	}
		else {
			return new CustomerCollectionRepresentation (allCustomers);
	 	}
			
	}
}
