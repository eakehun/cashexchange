package com.hourfun.cashexchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.PinCode;
import com.hourfun.cashexchange.service.CaptchaService;
import com.hourfun.cashexchange.service.ObserverService;
import com.hourfun.cashexchange.service.PinService;

@RestController
@RequestMapping("/macro")
public class MacroController {

	@Autowired
	private PinService service;

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private ObserverService observerService;


	@RequestMapping(value = "/{company}/", method = RequestMethod.GET)
	public ResponseEntity<List<String>> selectPin(@PathVariable String company) {
		try {
			return new ResponseEntity<List<String>>(service.getPinCode(company), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public ResponseEntity<List<PinCode>> updatePin(@RequestBody List<PinCode> pinCodes) {
		try {
			return new ResponseEntity<List<PinCode>>(service.update(pinCodes), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/captcha/", method = RequestMethod.POST)
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {

		try {
			return new ResponseEntity<String>(captchaService.captchaImageSave(file), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/observer/{message}", method = RequestMethod.GET)
	public ResponseEntity<String> observerMessage(@PathVariable String message) {
		try {
			observerService.observerMessageLogging(message);

			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/pinCode/recycle/{company}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> recyclePin(@PathVariable String company) {
		try {
			return new ResponseEntity<List<String>>(service.recyclePin(company), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
