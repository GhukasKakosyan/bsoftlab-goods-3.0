package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.resource.assembler.CurrencyRateResourceAssembler;
import net.bsoftlab.resource.CurrencyRateResource;
import net.bsoftlab.resource.validator.CurrencyRateResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyRateService;
import net.bsoftlab.utility.Functions;

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
@RequestMapping(path = "resources/currenciesrates")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateRestController {

    private static final String CurrencyRateDeletedMessageCode = "currencyRate.deleted";
    private static final String CurrencyRateInsertedMessageCode = "currencyRate.inserted";
    private static final String CurrencyRateNotExistMessageCode = "currencyRate.not.exist";
    private static final String CurrencyRateReadMessageCode = "currencyRate.read";
    private static final String CurrencyRateUpdatedMessageCode = "currencyRate.updated";
    private static final String CurrencyRateListEmptyMessageCode = "currencyRateList.empty";
    private static final String CurrencyRateListReadMessageCode = "currencyRateList.read";

    private static final String CurrencyRateDeletePermissionName = "permission.currencyRate.delete";
    private static final String CurrencyRateInsertPermissionName = "permission.currencyRate.insert";
    private static final String CurrencyRateReadPermissionName = "permission.currencyRate.read";
    private static final String CurrencyRateUpdatePermissionName = "permission.currencyRate.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterIdInvalidMessageCode = "parameter.id.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private CurrencyRateResourceAssembler currencyRateResourceAssembler;
    private CurrencyRateResourceValidator currencyRateResourceValidator;
    private CurrencyRateService currencyRateService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public CurrencyRateRestController(
            CurrencyRateResourceAssembler curencyRateResourceAssembler,
            CurrencyRateResourceValidator currencyRateResourceValidator,
            CurrencyRateService currencyRateServiceImpl){

        this.currencyRateResourceAssembler = curencyRateResourceAssembler;
        this.currencyRateResourceValidator = currencyRateResourceValidator;
        this.currencyRateService = currencyRateServiceImpl;
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

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Message> handleException(Throwable throwable) {
        Message message;
        if (throwable instanceof ServiceException) {
            ServiceException serviceException = (ServiceException)throwable;
            message = serviceException.getErrorMessage();
        } else if(throwable instanceof HttpMessageNotReadableException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else if(throwable instanceof HttpMessageNotWritableException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else if(throwable instanceof HttpMessageConversionException) {
            message = this.messageFactory.getMessage(ParameterHttpBodyNotConvertedMessageCode);
        } else {
            String error = Functions.ThrowableToString.apply(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.DELETE)
    @Secured(CurrencyRateDeletePermissionName)
    public ResponseEntity<Message> deleteCurrencyRate(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }
        this.currencyRateService.deleteCurrencyRate(ID);
        Message message = this.messageFactory.getMessage(CurrencyRateDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyRateInsertPermissionName)
    public ResponseEntity<Message> insertCurrencyRate(
            @RequestBody CurrencyRateResource currencyRateResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(currencyRateResource, "currencyRateResource");
        ValidationUtils.invokeValidator(this.currencyRateResourceValidator,
                currencyRateResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if (objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        CurrencyRate currencyRate = this.conversionService
                .convert(currencyRateResource, CurrencyRate.class);
        this.currencyRateService.insertCurrencyRate(currencyRate);
        Message message = this.messageFactory.getMessage(CurrencyRateInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyRateUpdatePermissionName)
    public ResponseEntity<Message> updateCurrencyRate(
            @PathVariable(value = "ID") String IDText,
            @RequestBody CurrencyRateResource currencyRateResource)
            throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(currencyRateResource, "currencyRateResource");
        ValidationUtils.invokeValidator(this.currencyRateResourceValidator,
                currencyRateResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if (objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        CurrencyRate currencyRate = this.conversionService
                .convert(currencyRateResource, CurrencyRate.class);
        if (!ID.equals(currencyRate.getID())) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.currencyRateService.updateCurrencyRate(currencyRate);
        Message message = this.messageFactory.getMessage(CurrencyRateUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyRateReadPermissionName)
    public ResponseEntity<CurrencyRateResource> getCurrencyRate(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        CurrencyRate currencyRate = this.currencyRateService.getCurrencyRate(ID);
        if (currencyRate == null) {
            Message message = this.messageFactory.getMessage(CurrencyRateNotExistMessageCode);
            throw new ServiceException(message);
        }
        CurrencyRateResource currencyRateResource =
                this.currencyRateResourceAssembler.toResource(currencyRate);
        List<Link> linkListCurrencyRateResource = currencyRateResource.getLinks();

        Message message = this.messageFactory.getMessage(CurrencyRateReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListCurrencyRateResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(currencyRateResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(CurrencyRateReadPermissionName)
    public ResponseEntity<Resources<CurrencyRateResource>> getCurrencyRates (
            @RequestParam(value = "currencyCode", required = false) String currencyCode,
            @RequestParam(value = "start", required = false) String startText,
            @RequestParam(value = "size", required = false) String sizeText)
            throws ServiceException {

        if (currencyCode != null && !currencyCode.trim().isEmpty()) {
            currencyCode = currencyCode.trim();
        }
        Integer start = 0, size = 0;
        if (startText != null && !startText.trim().isEmpty()) {
            try {
                start = this.conversionService.convert(startText, Integer.class);
            } catch (IllegalArgumentException illegalArgumentException) {
                Message message = this.messageFactory.getMessage(ParameterStartInvalidMessageCode);
                throw new ServiceException(message);
            }
        }
        if (sizeText != null && !sizeText.trim().isEmpty()) {
            try {
                size = this.conversionService.convert(sizeText, Integer.class);
            } catch (IllegalArgumentException illegalArgumentException) {
                Message message = this.messageFactory.getMessage(ParameterSizeInvalidMessageCode);
                throw new ServiceException(message);
            }
        }

        List<CurrencyRate> currencyRateList =
                this.currencyRateService.getCurrencyRateList(currencyCode, start, size);
        if (currencyRateList == null || currencyRateList.isEmpty()) {
            Message message = this.messageFactory.getMessage(CurrencyRateListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<CurrencyRateResource> currencyRateResourceList =
                this.currencyRateResourceAssembler.toResources(currencyRateList);
        Link linkCurrencyRateResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<CurrencyRateResource> currencyRateResources =
                new Resources<>(currencyRateResourceList, linkCurrencyRateResourceList);

        Message message = this.messageFactory.getMessage(CurrencyRateListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkCurrencyRateResourceList.getRel(),
                linkCurrencyRateResourceList.getHref());
        return new ResponseEntity<>(currencyRateResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}