package com.hourfun.cashexchange.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.Trading;
import com.hourfun.cashexchange.service.PinService;
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/admin/trading")
public class AdminTradingController {

	@Autowired
	private TradingService service;
	
	@Autowired
	private PinService pinService;

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByCreateDateBetween(fromDate, toDate, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndIdx(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String idx, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndIdx(fromDate, toDate, Long.valueOf(idx), pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndUserId(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userId, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndUserId(fromDate, toDate, userId, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndUserName(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userName, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndUserName(fromDate, toDate, userName, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndStatus(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndStatus(fromDate, toDate, status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndIdxAndStatus(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String idx, @PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndIdxAndStatus(fromDate, toDate, Long.valueOf(idx), status, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndUserIdAndStatus(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userId, @PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndUserIdAndStatus(fromDate, toDate, userId, status, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetweenAndUserNameAndStatus(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userName, @PathVariable String status,
			Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByCreateDateBetweenAndUserNameAndStatus(fromDate, toDate, userName, status, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/users/month/", method = RequestMethod.GET)
	public ResponseEntity<Integer> tradingUsersPerMonth() {

		return new ResponseEntity<Integer>(service.findByCreateDateBetweenAndGroupByUserId().size(), HttpStatus.OK);

	}

	@RequestMapping(value = "/month/", method = RequestMethod.GET)
	public ResponseEntity<Long> tradingPerMonth() {

		return new ResponseEntity<Long>(service.countByCreateDateBetween(), HttpStatus.OK);

	}

	@RequestMapping(value = "/pincode/month/", method = RequestMethod.GET)
	public ResponseEntity<Long> pinCodePerMonth() {

		return new ResponseEntity<Long>(pinService.countByCreateDateBetween(), HttpStatus.OK);

	}

	@RequestMapping(value = "/price/month/", method = RequestMethod.GET)
	public ResponseEntity<Map> pricePerMonth() {

		return new ResponseEntity<Map>(service.findByCreateDateBetweenAndSumPrice(), HttpStatus.OK);

	}
	
	public ResponseEntity<Map> recentSummary() {

		return new ResponseEntity<Map>(null, HttpStatus.OK);

	}
	
	

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> excelDownload(@PathVariable String fromDate,
			@PathVariable String toDate) {

		HttpHeaders httpHeaders = new HttpHeaders();

		InputStreamResource resource;

		try {
			File file = service.excelDownload(fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
