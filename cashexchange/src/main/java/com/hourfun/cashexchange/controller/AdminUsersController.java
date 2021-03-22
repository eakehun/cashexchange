package com.hourfun.cashexchange.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.hourfun.cashexchange.common.AuthEnum;
import com.hourfun.cashexchange.model.Agreement;
import com.hourfun.cashexchange.model.Trading;
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/name/{name}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndUserName(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String name, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndName(fromDate, toDate, name, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/name/{name}/status/{status}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndNameAndAccountStatus(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String name,
			@PathVariable String status, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndNameAndAccountStatus(fromDate, toDate, name, status, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/tel/{tel}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndTel(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String tel, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndTel(fromDate, toDate, tel, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/tel/{tel}/status/{status}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndTelAndAccountStatus(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String tel,
			@PathVariable String status, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndTelAndAccountStatus(fromDate, toDate, tel, status, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndIdx(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String idx, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndIdx(fromDate, toDate, idx, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<Users>> findByCreateDateBetweenAndIdxAndAccountStatus(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx,
			@PathVariable String status, Pageable pageable) {
		try {
			return new ResponseEntity<Page<Users>>(
					service.findByCreateDateBetweenAndIdxAndAccountStatus(fromDate, toDate, idx, status, pageable),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/idx/{idx}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> findByIdx(@PathVariable long idx) {
		try {
			return new ResponseEntity<Users>(service.findByIdx(idx), HttpStatus.OK);
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

	@RequestMapping(value = "/info/agreement/{idx}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Agreement>> findAgreementByUserId(@PathVariable long idx) {
		try {
			return new ResponseEntity<List<Agreement>>(
					service.findAgreementByUserId(service.findByIdx(idx).getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/userId/{userId}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Users> findByUserId(@PathVariable String userId) {
		try {
			return new ResponseEntity<Users>(service.findByUserId(userId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenDownload(@PathVariable String fromDate,
			@PathVariable String toDate) {
		try {

			List<Users> selectList = service.findByCreateDateBetween(fromDate, toDate);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndAccountStatusDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String status) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndAccountStatus(fromDate, toDate, status);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndUserIdDownload(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String userId) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndUserId(fromDate, toDate, userId);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/userId/{userId}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndUserIdAndAccountStatusDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String userId,
			@PathVariable String status) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndUserIdAndAccountStatus(fromDate, toDate, userId,
					status);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/name/{name}/download/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<InputStreamResource> findByCreateDateBetweenAndUserNameDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String name) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndName(fromDate, toDate, name);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/name/{name}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndNameAndAccountStatusDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String name,
			@PathVariable String status) {

		try {

			List<Users> selectList = service.findByCreateDateBetweenAndNameAndAccountStatus(fromDate, toDate, name,
					status);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/tel/{tel}/download/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<InputStreamResource> findByCreateDateBetweenAndTelDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String tel) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndTel(fromDate, toDate, tel);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/tel/{tel}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndTelAndAccountStatusDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String tel,
			@PathVariable String status) {

		try {

			List<Users> selectList = service.findByCreateDateBetweenAndTelAndAccountStatus(fromDate, toDate, tel,
					status);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndIdxDownload(@PathVariable String fromDate,
			@PathVariable String toDate, @PathVariable String idx) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndIdx(fromDate, toDate, idx);
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

	@RequestMapping(value = "/fromDate/{fromDate}/toDate/{toDate}/idx/{idx}/status/{status}/download/", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findByCreateDateBetweenAndIdxAndAccountStatusDownload(
			@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String idx,
			@PathVariable String status) {
		try {

			List<Users> selectList = service.findByCreateDateBetweenAndIdxAndAccountStatus(fromDate, toDate, idx,
					status);
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
