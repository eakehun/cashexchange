package com.hourfun.cashexchange.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.Users;
import com.hourfun.cashexchange.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersService service;

	@RequestMapping(value = "/login/id/{id}/pwd/{pwd}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> login(@PathVariable String id, @PathVariable String pwd,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			return new ResponseEntity<Users>(service.customLogin(id, pwd, AuthEnum.ROLE_USER, request, response),
					HttpStatus.OK);
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Bad credentials. Please check username, password");
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Bad credentials. Please check username, password");
		}
	}

	@RequestMapping(value = "/findId/{tel}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> findId(@PathVariable String tel) {
		try {
			return new ResponseEntity<Users>(service.findByTel(tel), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/findPassword/id/{id}/tel/{tel}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> findPassword(@PathVariable String id, @PathVariable String tel) {
		try {
			return new ResponseEntity<Users>(service.findByUserIdAndTel(id, tel), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/signin/", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Users> signin(@RequestBody Users users) {
		try {
			return new ResponseEntity<Users>(service.signIn(users, AuthEnum.ROLE_USER), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/checkCurrentAuth/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> checkCurrentAuth(Authentication auth) {
		try {
			return new ResponseEntity<String>(service.findByUserId(auth.getName()).getName(), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
