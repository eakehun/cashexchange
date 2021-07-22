package com.hourfun.cashexchange.controller;

import java.util.List;

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

import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.model.TradingMenu;
import com.hourfun.cashexchange.service.HistoryService;
import com.hourfun.cashexchange.service.TradingMenuService;

@Controller
@RequestMapping("/admin/tradingMenu")
public class AdminTradingMenuController {

	@Autowired
	private TradingMenuService tradingMenuService;

	@Autowired
	private HistoryService historyService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<TradingMenu>> findAll() {
		return new ResponseEntity<List<TradingMenu>>(tradingMenuService.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<TradingMenu> insertMenu(@RequestBody TradingMenu tradingMenu) {
		if (tradingMenu.getIdx() > 0l) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "데이터 저장을 할 수 없습니다.");
		}
		return new ResponseEntity<TradingMenu>(tradingMenuService.save(tradingMenu), HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<TradingMenu> updateMenu(@RequestBody TradingMenu tradingMenu) {
		if (tradingMenu.getIdx() < 1l) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "데이터 변경을 할 수 없습니다.");
		}
		return new ResponseEntity<TradingMenu>(tradingMenuService.save(tradingMenu), HttpStatus.OK);
	}

	@RequestMapping(value = "/service/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<History>> findServiceHistory(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable) {
		return new ResponseEntity<Page<History>>(
				historyService.selectTradingMenuUpdateHistoryByCreateDate(fromDate, toDate, pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/service/company/{company}/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<History>> findServiceHistoryByCompany(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable, @PathVariable String company) {
		return new ResponseEntity<Page<History>>(historyService.selectTradingMenuUpdateHistoryByCreateDateAndCompany(
				fromDate, toDate, pageable, company), HttpStatus.OK);
	}

	@RequestMapping(value = "/service/used/{used}/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<History>> findServiceHistoryByUsed(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable, @PathVariable String used) {
		return new ResponseEntity<Page<History>>(
				historyService.selectTradingMenuUpdateHistoryByCreateDateAndUsed(fromDate, toDate, pageable, used),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/service/company/{company}/used/{used}/fromDate/{fromDate}/toDate/{toDate}/", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Page<History>> findServiceHistoryByCompanyAndUsed(@PathVariable String fromDate,
			@PathVariable String toDate, Pageable pageable,
			@PathVariable String company, @PathVariable String used) {
		return new ResponseEntity<Page<History>>(
				historyService.selectTradingMenuUpdateHistoryByCreateDateAndCompanyAndUsed(fromDate, toDate, pageable, company, used), HttpStatus.OK);
	}
}
