package com.hourfun.cashexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.service.UsersService;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

	@Autowired
	private UsersService service;

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(service.findByCreateDateBetween(fromDate, toDate, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/status/{status}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndAccountStatus(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status,
			Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndAccountStatus(fromDate, toDate, status, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndUserId(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userId, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndUserIdAndAccountStatus(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(service.findByCreateDateBetweenAndUserIdAndAccountStatus(fromDate,
					toDate, userId, status, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<Users> updateAccountStatus(@RequestBody Users users) {
		try {
			return new ResponseEntity<Users>(service.updateAccountStatus(users), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
