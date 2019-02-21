package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Currency;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyService;
import net.bsoftlab.utility.UtilityFunctions;

import net.bsoftlab.resource.assembler.CurrencyResourceAssembler;
import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.validator.CurrencyResourceValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.lang.IllegalArgumentException;
import java.util.List;

@RestController
@RequestMapping(path = "resources/currencies")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRestController {

    private static final String CurrencyDeletedMessageCode = "currency.deleted";
    private static final String CurrencyInsertedMessageCode = "currency.inserted";
    private static final String CurrencyNotExistMessageCode = "currency.not.exist";
    private static final String CurrencyReadMessageCode = "currency.read";
    private static final String CurrencyUpdatedMessageCode = "currency.updated";
    private static final String CurrencyListEmptyMessageCode = "currencyList.empty";
    private static final String CurrencyListReadMessageCode = "currencyList.read";

    private static final String CurrencyDeletePermissionName = "permission.currency.delete";
    private static final String CurrencyInsertPermissionName = "permission.currency.insert";
    private static final String CurrencyReadPermissionName = "permission.currency.read";
    private static final String CurrencyUpdatePermissionName = "permission.currency.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterCodeInvalidMessageCode = "parameter.code.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private CurrencyResourceAssembler currencyResourceAssembler;
    private CurrencyResourceValidator currencyResourceValidator;
    private CurrencyService currencyService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public CurrencyRestController(
            CurrencyResourceAssembler currencyResourceAssembler,
            CurrencyResourceValidator currencyResourceValidator,
            CurrencyService currencyServiceImpl){

        this.currencyResourceAssembler = currencyResourceAssembler;
        this.currencyResourceValidator = currencyResourceValidator;
        this.currencyService = currencyServiceImpl;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Autowired
    public void setMessageFactory(
            MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }
    @Autowired
    public void setUtilityFunctions(
            UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Message> handleException(Throwable throwable) {
        Message message;
        if(throwable instanceof ServiceException) {
            ServiceException serviceException = (ServiceException)throwable;
            message = serviceException.getErrorMessage();
        } else if(throwable instanceof HttpMessageNotReadableException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else if(throwable instanceof HttpMessageNotWritableException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else if(throwable instanceof HttpMessageConversionException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else {
            String error = this.utilityFunctions.getPrintStackTrace(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.DELETE)
    @Secured(CurrencyDeletePermissionName)
    public ResponseEntity<Message> deleteCurrency(
            @PathVariable(value = "code") String code) throws ServiceException {

        this.currencyService.deleteCurrency(code);
        Message message = this.messageFactory.getMessage(CurrencyDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyInsertPermissionName)
    public ResponseEntity<Message> insertCurrency(
            @RequestBody CurrencyResource currencyResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(currencyResource, "currencyResource");
        ValidationUtils.invokeValidator(this.currencyResourceValidator,
                currencyResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Currency currency = this.conversionService
                .convert(currencyResource, Currency.class);
        this.currencyService.insertCurrency(currency);
        Message message = this.messageFactory.getMessage(CurrencyInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyUpdatePermissionName)
    public ResponseEntity<Message> updateCurrency(
            @PathVariable(value = "code") String code,
            @RequestBody CurrencyResource currencyResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(currencyResource, "currencyResource");
        ValidationUtils.invokeValidator(this.currencyResourceValidator,
                currencyResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Currency currency = this.conversionService
                .convert(currencyResource, Currency.class);
        if(!code.equals(currency.getCode())) {
            Message message = this.messageFactory.getMessage(ParameterCodeInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.currencyService.updateCurrency(currency);
        Message message = this.messageFactory.getMessage(CurrencyUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyReadPermissionName)
    public ResponseEntity<CurrencyResource> getCurrency(
            @PathVariable(value = "code") String code) throws ServiceException {

        Currency currency = this.currencyService.getCurrency(code);
        if(currency == null) {
            Message message = this.messageFactory.getMessage(CurrencyNotExistMessageCode);
            throw new ServiceException(message);
        }
        CurrencyResource currencyResource = this.currencyResourceAssembler.toResource(currency);
        List<Link> linkListCurrencyResource = currencyResource.getLinks();
        Message message = this.messageFactory.getMessage(CurrencyReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for(Link link : linkListCurrencyResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(currencyResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyReadPermissionName)
    public ResponseEntity<Resources<CurrencyResource>> getCurrencies(
            @RequestParam(value = "start", required = false) String startText,
            @RequestParam(value = "size", required = false) String sizeText)
            throws ServiceException {

        Integer start = 0, size = 0;
        if(startText != null && !startText.trim().isEmpty()) {
            try {
                start = this.conversionService.convert(startText, Integer.class);
            } catch  (IllegalArgumentException illegalArgumentException) {
                Message message = this.messageFactory.getMessage(ParameterStartInvalidMessageCode);
                throw new ServiceException(message);
            }
        }
        if(sizeText != null && !sizeText.trim().isEmpty()) {
            try {
                size = this.conversionService.convert(sizeText, Integer.class);
            } catch (IllegalArgumentException illegalArgumentException) {
                Message message = this.messageFactory.getMessage(ParameterSizeInvalidMessageCode);
                throw new ServiceException(message);
            }
        }

        List<Currency> currencyList =
                this.currencyService.getCurrencyList(start, size);
        if(currencyList == null || currencyList.isEmpty()) {
            Message message = this.messageFactory.getMessage(CurrencyListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        Link linkCurrencyResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<CurrencyResource> currencyResources =
                new Resources<>(currencyResourceList, linkCurrencyResourceList);

        Message message = this.messageFactory.getMessage(CurrencyListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkCurrencyResourceList.getRel(), linkCurrencyResourceList.getHref());
        return new ResponseEntity<>(currencyResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}