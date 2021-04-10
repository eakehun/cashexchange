package com.hourfun.cashexchange.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.model.HistoryTrading;
import com.hourfun.cashexchange.service.HistoryTradingService;

@RestController
@RequestMapping("/admin/trading/history")
public class AdminTradingHistoryController {

	@Autowired
	private HistoryTradingService service;

//	@Autowired
//	private PinService pinService;

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetween(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(service.findByDateBetween(dateCategory, fromDate, toDate, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndIdx(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx, Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(
				service.findByDateBetweenAndIdx(dateCategory, fromDate, toDate, Long.valueOf(idx), pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndUserId(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(
				service.findByDateBetweenAndUserId(dateCategory, fromDate, toDate, userId, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndUserName(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(
				service.findByDateBetweenAndUserName(dateCategory, fromDate, toDate, userName, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status,
			Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(
				service.findByDateBetweenAndStatus(dateCategory, fromDate, toDate, status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndIdxAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(service.findByDateBetweenAndIdxAndStatus(dateCategory, fromDate,
				toDate, Long.valueOf(idx), status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndUserIdAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(
				service.findByDateBetweenAndUserIdAndStatus(dateCategory, fromDate, toDate, userId, status, pageable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/status/{status}/", method = RequestMethod.GET)
	public ResponseEntity<Page<HistoryTrading>> findByDateBetweenAndUserNameAndStatus(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			@PathVariable String status, Pageable pageable) {
		return new ResponseEntity<Page<HistoryTrading>>(service.findByDateBetweenAndUserNameAndStatus(dateCategory, fromDate,
				toDate, userName, status, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate) {


		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetween(dateCategory, fromDate, toDate);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndIdxDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx) {
		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndIdx(dateCategory, fromDate, toDate,
					Long.valueOf(idx));
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserIdDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndUserId(dateCategory, fromDate, toDate, userId);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserNameDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndUserName(dateCategory, fromDate, toDate, userName);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndStatus(dateCategory, fromDate, toDate, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndIdxAndStatusDownload(
			@PathVariable String dateCategory, @PathVariable String fromDate, @PathVariable String toDate,
			@PathVariable String idx, @PathVariable String status) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndIdxAndStatus(dateCategory, fromDate, toDate,
					Long.valueOf(idx), status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserIdAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndUserIdAndStatus(dateCategory, fromDate, toDate,
					userId, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/{dateCategory}/fromDate/{fromDate}/toDate/{toDate}/userName/{userName}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByDateBetweenAndUserNameAndStatusDownload(@PathVariable String dateCategory,
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userName,
			@PathVariable String status) {

		InputStreamResource resource;

		try {

			List<HistoryTrading> selectList = service.findByDateBetweenAndUserNameAndStatus(dateCategory, fromDate, toDate,
					userName, status);
			File file = service.excelDownload(selectList, fromDate, toDate);
			resource = new InputStreamResource(new FileInputStream(file));
			long contentLength = file.length();

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentLength(contentLength).contentType(MediaType.TEXT_PLAIN).body(resource);
		} catch (FileNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
