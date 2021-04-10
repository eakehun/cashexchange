package com.hourfun.cashexchange.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetween(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByDateBetween(dateCategory, fromDate, toDate, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndIdx(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByDateBetweenAndIdx(dateCategory, fromDate, toDate, Long.valueOf(idx), pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndUserId(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByDateBetweenAndUserId(dateCategory, fromDate, toDate, userId, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndUserName(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByDateBetweenAndUserName(dateCategory, fromDate, toDate, userName, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status,
			Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByDateBetweenAndStatus(dateCategory, fromDate, toDate, status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndIdxAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByDateBetweenAndIdxAndStatus(dateCategory, fromDate,
				toDate, Long.valueOf(idx), status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndUserIdAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(
				service.findByDateBetweenAndUserIdAndStatus(dateCategory, fromDate, toDate, userId, status, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByDateBetweenAndUserNameAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByDateBetweenAndUserNameAndStatus(dateCategory, fromDate,
				toDate, userName, status, pageable), HttpStatus.OK);
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
	public ResponseEntity<Map<String, Object>> pricePerMonth() {

		return new ResponseEntity<Map<String, Object>>(service.findByCreateDateBetweenAndSumPrice(), HttpStatus.OK);

	}

	@RequestMapping(value = "/recent/", method = RequestMethod.GET)
	public ResponseEntity<List<Object>> recentSummary() {

		return new ResponseEntity<List<Object>>(service.recentSummary(), HttpStatus.OK);

	}

	@RequestMapping(value = "/limit/{userId}/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Integer>> getTradingLimit(@PathVariable String userId) {
		return new ResponseEntity<Map<String, Integer>>(service.getTradingLimit(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate) {

		try {

			List<Trading> selectList = service.findByDateBetween(dateCategory, fromDate, toDate);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndIdxDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndIdx(dateCategory, fromDate, toDate,
					Long.valueOf(idx));
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserIdDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndUserId(dateCategory, fromDate, toDate, userId);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserNameDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndUserName(dateCategory, fromDate, toDate, userName);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndStatus(dateCategory, fromDate, toDate, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndIdxAndStatusDownload(
			@PathVariable String dateCategory, @PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String idx, @PathVariable String status) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndIdxAndStatus(dateCategory, fromDate, toDate,
					Long.valueOf(idx), status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserIdAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndUserIdAndStatus(dateCategory, fromDate, toDate,
					userId, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserNameAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			@PathVariable String status) {

		try {

			List<Trading> selectList = service.findByDateBetweenAndUserNameAndStatus(dateCategory, fromDate, toDate,
					userName, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/csv; charset=MS949");
			responseHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseHeaders.add("Content-Length", String.valueOf(contentLength));
			return new ResponseEntity<InputStreamResource>(resource, responseHeaders, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
