package com.hourfun.cashexchange.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

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
import com.hourfun.cashexchange.service.TradingService;

@RestController
@RequestMapping("/admin/trading")
public class AdminTradingController {

	@Autowired
	private TradingService service;

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public ResponseEntity<Page<Trading>> findByCreateDateBetween(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<Trading>>(service.findByCreateDateBetween(fromDate, toDate, pageable),
				HttpStatus.OK);
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
