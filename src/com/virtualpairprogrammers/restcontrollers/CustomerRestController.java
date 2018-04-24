package com.virtualpairprogrammers.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.virtualpairprogrammers.domain.Customer;
import com.virtualpairprogrammers.services.customers.CustomerManagementService;
import com.virtualpairprogrammers.services.customers.CustomerNotFoundException;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerManagementService customerService;
	
	//We want to support GETs to /customer/373737
	//@RequestMapping(value="/customer/{id}", headers = {"Accept=application/xml,application/json"})
	@RequestMapping(value="/customer/{id}")
	public Customer findCustomerById(@PathVariable String id) {
		
		Customer foundCustomer;
		try {
		foundCustomer =customerService.getFullCustomerDetail(id);
		}
		catch (CustomerNotFoundException e) {
			//e.printStackTrace();
			// TODO - improve this
			throw new RuntimeException(e);
		}
		// Return an object but this will be picked up by a httpmessageconverter
		//(if a suitable one exists) to fullfil the content type requested by the client
		return foundCustomer;
	}

}
