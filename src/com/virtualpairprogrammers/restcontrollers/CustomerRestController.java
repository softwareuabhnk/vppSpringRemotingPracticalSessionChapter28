package com.virtualpairprogrammers.restcontrollers;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping(value="/customer/{id}", method=RequestMethod.GET)
	public Customer findCustomerById(@PathVariable String id) throws CustomerNotFoundException {
		
		return customerService.getFullCustomerDetail(id);
	
	}
	
	
	/**
	 * Requirement: Only return customers.
	 * @return
	 * @throws CustomerNotFoundException 
	 */
	@RequestMapping(value="/customers", method=RequestMethod.GET)
	public CustomerCollectionRepresentation returnAllCustomers(@RequestParam(required=false) Integer first, 
															   @RequestParam(required=false) Integer last) throws CustomerNotFoundException{
		
		List<Customer> allCustomers = customerService.getAllCustomers();
		for (Customer next: allCustomers) {
			next.setCalls(null);
			
			
			Link link = linkTo(methodOn(CustomerRestController.class).findCustomerById(next.getCustomerId())).withSelfRel();
			next.add(link);
		}
		
		if (first !=null & last!=null) {		
			CustomerCollectionRepresentation page = new CustomerCollectionRepresentation (allCustomers.subList(first-1, last));
			page.add(linkTo(methodOn(CustomerRestController.class).returnAllCustomers(last + 1, last + 10)).withRel("next"));
			return page;
			
	 	}
		else {
			return new CustomerCollectionRepresentation (allCustomers);
	 	}
			
	}
	
	@RequestMapping(value="/customers", method=RequestMethod.POST)
	public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer newCustomer) throws CustomerNotFoundException {
			
		Customer createdCustomer =customerService.newCustomer(newCustomer);
		
		HttpHeaders headers = new HttpHeaders();
		
//      This works just fine		
//		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/customer/").path(createdCustomer.getCustomerId()).build().toUri();
//		headers.setLocation(uri);
		
//      This works also just fine.		
//		URI uri = MvcUriComponentsBuilder.fromMethodName(CustomerRestController.class, 
//														"findCustomerById", 
//														createdCustomer.getCustomerId()).build().toUri();
		
//		URI uri = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerRestController.class).findCustomerById("109")).toUri();
		URI uri = linkTo(methodOn(CustomerRestController.class).findCustomerById(createdCustomer.getCustomerId())).toUri();
		
		headers.setLocation(uri);
											
		
		return new ResponseEntity<Customer>(createdCustomer, headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/customer/{id}", method=RequestMethod.PUT)
	//@ResponseStatus(value=HttpStatus.OK)
	public void updateCustomer(@RequestBody Customer changedCustomer) throws CustomerNotFoundException{
		customerService.updateCustomer(changedCustomer);
	}
	

	@RequestMapping(value="/customer/{id}", method=RequestMethod.DELETE)
	//@ResponseStatus(value=HttpStatus.OK)
	public void deleteCustomer(@PathVariable String id) throws CustomerNotFoundException{
		
		customerService.deleteCustomer(customerService.findCustomerById(id));
		
	}
	

	
	
}
