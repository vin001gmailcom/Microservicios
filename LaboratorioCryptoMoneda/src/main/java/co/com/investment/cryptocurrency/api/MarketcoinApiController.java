package co.com.investment.cryptocurrency.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.investment.cryptocurrency.model.CryptoCurrency;
import co.com.investment.cryptocurrency.model.Investment;
import co.com.investment.cryptocurrency.model.MarketCoin;
import co.com.investment.cryptocurrency.model.Wallet;
import co.com.investment.cryptocurrency.util.DataBaseSimulator;
import io.swagger.annotations.ApiParam;

@Controller
public class MarketcoinApiController implements MarketcoinApi {

	private static final String INVESTMENT_MAKE_IT = "Investment make it";

	private static final String INVESTMENT = "Investment";

	private static final String ALL_CRYPTOCURRENCIES_FROM_THIS_MARKET_COIN = "All Cryptocurrencies from this market coin";

	private static final String MARKET_COIN_NOT_ADDED_SUCCESSFULLY = "MarketCoin not added successfully";

	private static final String MARKET_COIN_ADDED_SUCCESSFULLY = "MarketCoin added successfully";

	private static final String CRYPTO_CURRENCY_ADDED_SUCCESSFULLY = "CryptoCurrency added successfully";

	private static final String CRYPTO_CURRENCY_NOT_ADDED_SUCCESSFULLY = "CryptoCurrency not added successfully";
	
	private static final String LSIT_OF_MARKET_COINS = "Lsit of market coins";

	private static final String MARKET_COINS = "MarketCoins";

	private static final String MARKET_COIN_FOUND = "MarketCoin found";

	private static final String MARKET_COIN = "MarketCoin";

	private static final String THIS_IS_THE_CRYPTO_CURRENCY_FOUND_IT = "This is the CryptoCurrency found it";

	private static final String CRYPTO_CURRENCY = "CryptoCurrency";

	private static final String MESSAGE_LIST_CRYPTOCURRENCIES_OK = "The list was found ok";

	private static final String LIST_OF_CRYPTO_CURRENCIES = "ListOfCryptoCurrencies";

	private static final int ONE_SECOND = 1000;

	private static final Logger log = LoggerFactory.getLogger(MarketcoinApiController.class);

	private static final String MARKET_COIN_NOT_FOUND = "MasterCoiin not found";

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	private List<CryptoCurrency> cryptoCurrencyList;

	private DataBaseSimulator dataBaseSimulator = DataBaseSimulator.getInstance();

	@org.springframework.beans.factory.annotation.Autowired
	public MarketcoinApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
		cryptoCurrencyList = dataBaseSimulator.getCryptoCurrencyList();
	}

	public ResponseEntity<CryptoCurrency> addCryptoCurrencyToMarketCoin(
			@ApiParam(value = "Market coin to add the cryptocurrency", required = true) @PathVariable("idMarketCoin") String idMarketCoin,
			@ApiParam(value = "Marketcoin to create") @Valid @RequestBody CryptoCurrency cryptoCurrency) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		ResponseEntity<CryptoCurrency> responseEntity = null;
		if(dataBaseSimulator.addCryptoCurrencyToMarketCoin(idMarketCoin, cryptoCurrency)) {
			httpHeaders.set(CRYPTO_CURRENCY, CRYPTO_CURRENCY_ADDED_SUCCESSFULLY);
			Link cryptoCurrencyLink = linkTo(CryptoCurrencyApi.class).slash(cryptoCurrency.getName()).withSelfRel();
			cryptoCurrency.add(cryptoCurrencyLink);
			responseEntity = new ResponseEntity<CryptoCurrency>(cryptoCurrency,httpHeaders,HttpStatus.OK);
		}else {
			httpHeaders.set(CRYPTO_CURRENCY, CRYPTO_CURRENCY_NOT_ADDED_SUCCESSFULLY);
			responseEntity = new ResponseEntity<CryptoCurrency>(httpHeaders,HttpStatus.NOT_MODIFIED);
		}
		return responseEntity;
	}

	public ResponseEntity<MarketCoin> addMarketCoin(
			@ApiParam(value = "Market coin to add", required = true) @PathVariable("idMarketCoin") String idMarketCoin,
			@ApiParam(value = "Marketcoin to create", required=true) @Valid @RequestBody(required=true) MarketCoin marketCoin) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		ResponseEntity<MarketCoin> responseEntity = null;
		MarketCoin marketCoinAdded = dataBaseSimulator.addMarketCoin(idMarketCoin, marketCoin);
		if(Objects.nonNull(marketCoinAdded)) {
			httpHeaders.set(MARKET_COIN, MARKET_COIN_ADDED_SUCCESSFULLY);
			marketCoinAdded.removeLinks();
			marketCoinAdded.add(linkTo(MarketcoinApi.class).slash(marketCoinAdded.getIdMarketCoin()).withSelfRel());
			ResponseEntity<List<CryptoCurrency>> linkBuilder = methodOn(MarketcoinApiController.class)
					.searchCryptoCurrencies(marketCoinAdded.getIdMarketCoin().toString(), 10, 10);
			Link allCryptoCurrenciesFromMarketCoin = linkTo(linkBuilder).withRel(ALL_CRYPTOCURRENCIES_FROM_THIS_MARKET_COIN);
			marketCoinAdded.add(allCryptoCurrenciesFromMarketCoin);
			responseEntity = new ResponseEntity<MarketCoin>(marketCoinAdded,httpHeaders,HttpStatus.OK);
		}else {
			httpHeaders.set(MARKET_COIN, MARKET_COIN_NOT_ADDED_SUCCESSFULLY);
			responseEntity = new ResponseEntity<MarketCoin>(marketCoin,httpHeaders,HttpStatus.NOT_MODIFIED);
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<List<MarketCoin>> searchAllMarketCoins() {
		List<MarketCoin> marketCoinList = dataBaseSimulator.getMarketCoinList();
		Iterator<MarketCoin> iterator = marketCoinList.iterator();
		while (iterator.hasNext()) {
			MarketCoin marketCoin = iterator.next();
			marketCoin.removeLinks();
			marketCoin.add(linkTo(MarketcoinApi.class).slash(marketCoin.getIdMarketCoin()).withSelfRel());
			ResponseEntity<List<CryptoCurrency>> linkBuilder = methodOn(MarketcoinApiController.class)
					.searchCryptoCurrencies(marketCoin.getIdMarketCoin().toString(), 10, 10);
			Link allCryptoCurrenciesFromMarketCoin = linkTo(linkBuilder).withRel(ALL_CRYPTOCURRENCIES_FROM_THIS_MARKET_COIN);
			marketCoin.add(allCryptoCurrenciesFromMarketCoin);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		httpHeaders.set(MARKET_COINS, LSIT_OF_MARKET_COINS);
		return new ResponseEntity<List<MarketCoin>>(marketCoinList, httpHeaders, HttpStatus.OK);
	}

	public ResponseEntity<List<CryptoCurrency>> searchCryptoCurrencies(
			@ApiParam(value = "pass a mandatory search id for looking up the marketcoin brand and his cryptocurrencies", required = true) @PathVariable("idMarketCoin") String idMarketCoin,
			@Min(0) @ApiParam(value = "number of records to skip for pagination") @Valid @RequestParam(value = "skip", required = false) Integer skip,
			@Min(0) @Max(50) @ApiParam(value = "maximum number of records to return") @Valid @RequestParam(value = "limit", required = false) Integer limit) {
		List<CryptoCurrency> cryptoCurrencyList = dataBaseSimulator.getCryptoCurrenciesFormMarketCoin(idMarketCoin);
		Iterator<CryptoCurrency> iterator = cryptoCurrencyList.iterator();
		while (iterator.hasNext()) {
			CryptoCurrency cryptoCurrency = iterator.next();
			Link cryptoCurrencyLink = linkTo(CryptoCurrencyApi.class).slash(cryptoCurrency.getName()).withSelfRel();
			cryptoCurrency.removeLinks();
			cryptoCurrency.add(cryptoCurrencyLink);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		return new ResponseEntity<List<CryptoCurrency>>(cryptoCurrencyList, httpHeaders, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<MarketCoin> searchMarketCoin(
			@ApiParam(value = "pass a mandatory search id for looking up the marketcoin brand", required = true) @PathVariable("idMarketCoin") String idMarketCoin) {
		MarketCoin marketCoin = dataBaseSimulator.getMarketCoin(idMarketCoin);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		if (Objects.isNull(marketCoin)) {
			httpHeaders.set(MARKET_COIN, MARKET_COIN_NOT_FOUND);
			return new ResponseEntity<MarketCoin>(marketCoin, httpHeaders, HttpStatus.NO_CONTENT);
		}
		marketCoin.removeLinks();
		marketCoin.add(linkTo(MarketcoinApi.class).slash(marketCoin.getIdMarketCoin()).withSelfRel());
		httpHeaders.set(MARKET_COIN, MARKET_COIN_FOUND);
		return new ResponseEntity<MarketCoin>(marketCoin, httpHeaders, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CryptoCurrency> searchCryptoCurrency(String cryptoCurrencyName) {
		Iterator<CryptoCurrency> iterator = cryptoCurrencyList.iterator();
		ResponseEntity<CryptoCurrency> responseEntity = null;
		while (iterator.hasNext()) {
			CryptoCurrency cryptoCurrency = iterator.next();
			if (cryptoCurrency.getName().equals(cryptoCurrencyName)) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setExpires(ONE_SECOND);
				httpHeaders.set(CRYPTO_CURRENCY, THIS_IS_THE_CRYPTO_CURRENCY_FOUND_IT);
				responseEntity = new ResponseEntity<CryptoCurrency>(cryptoCurrency, httpHeaders, HttpStatus.OK);
			}
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<Wallet> buyCryptoCurrency(@RequestBody Investment investment) {
		Wallet wallet = dataBaseSimulator.buyCryptoCurrency(investment.getIdMarketCoinSeller(), investment.getIdMarketCoinBuyer(), investment.getWalletSellerOwner(), investment.getWalletBuyerOwner(), investment.getCryptoCurrencyName(), new BigDecimal(investment.getAmmountToBuy()));
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setExpires(ONE_SECOND);
		httpHeaders.set(INVESTMENT, INVESTMENT_MAKE_IT);
		ResponseEntity<Wallet> responseEntity = new ResponseEntity<>(wallet,httpHeaders ,HttpStatus.OK);
		return responseEntity;
	}

}
