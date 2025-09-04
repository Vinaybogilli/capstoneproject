package com.vinay.wipro.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vinay.wipro.entity.Customer;

@FeignClient(name="CustomerService")
public interface AccountFeign {
	
	@GetMapping("/api/customer/getcustomerbyname/{name}")
    Customer getCustomerByName(@PathVariable("name") String name);
	
}
