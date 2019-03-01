package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.resource.assembler.SalePriceResourceAssembler;
import net.bsoftlab.resource.SalePriceResource;
import net.bsoftlab.resource.validator.SalePriceResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.SalePriceService;
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
@RequestMapping(path = "resources/saleprices")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SalePriceRestController {

    private static final String SalePriceDeletedMessageCode = "salePrice.deleted";
    private static final String SalePriceInsertedMessageCode = "salePrice.inserted";
    private static final String SalePriceNotExistMessageCode = "salePrice.not.exist";
    private static final String SalePriceReadMessageCode = "salePrice.read";
    private static final String SalePriceUpdatedMessageCode = "salePrice.updated";
    private static final String SalePriceListEmptyMessageCode = "salePriceList.empty";
    private static final String SalePriceListReadMessageCode = "salePriceList.read";

    private static final String SalePriceDeletePermissionName = "permission.salePrice.delete";
    private static final String SalePriceInsertPermissionName = "permission.salePrice.insert";
    private static final String SalePriceReadPermissionName = "permission.salePrice.read";
    private static final String SalePriceUpdatePermissionName = "permission.salePrice.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterIdInvalidMessageCode = "parameter.id.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private SalePriceResourceAssembler salePriceResourceAssembler;
    private SalePriceResourceValidator salePriceResourceValidator;
    private SalePriceService salePriceService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public SalePriceRestController(
            SalePriceResourceAssembler salePriceResourceAssembler,
            SalePriceResourceValidator salePriceResourceValidator,
            SalePriceService salePriceServiceImpl) {

        this.salePriceResourceAssembler = salePriceResourceAssembler;
        this.salePriceResourceValidator = salePriceResourceValidator;
        this.salePriceService = salePriceServiceImpl;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

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
            String error = Functions.getPrintStackTrace(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.DELETE)
    @Secured(SalePriceDeletePermissionName)
    public ResponseEntity<Message> deleteSalePrice(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.salePriceService.deleteSalePrice(ID);
        Message message = this.messageFactory.getMessage(SalePriceDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(SalePriceInsertPermissionName)
    public ResponseEntity<Message> insertSalePrice(
            @RequestBody SalePriceResource salePriceResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(salePriceResource, "salePriceResource");
        ValidationUtils.invokeValidator(this.salePriceResourceValidator,
                salePriceResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        SalePrice salePrice = this.conversionService
                .convert(salePriceResource, SalePrice.class);
        this.salePriceService.insertSalePrice(salePrice);
        Message message = this.messageFactory.getMessage(SalePriceInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(SalePriceUpdatePermissionName)
    public ResponseEntity<Message> updateSalePrice(
            @PathVariable(value = "ID") String IDText,
            @RequestBody SalePriceResource salePriceResource)
            throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }
        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(salePriceResource, "salePriceResource");
        ValidationUtils.invokeValidator(this.salePriceResourceValidator,
                salePriceResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }
        SalePrice salePrice = this.conversionService
                .convert(salePriceResource, SalePrice.class);
        if(!ID.equals(salePrice.getID())) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.salePriceService.updateSalePrice(salePrice);
        Message message = this.messageFactory.getMessage(SalePriceUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(SalePriceReadPermissionName)
    public ResponseEntity<SalePriceResource> getSalePrice(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        SalePrice salePrice = this.salePriceService.getSalePrice(ID);
        if(salePrice == null) {
            Message message = this.messageFactory.getMessage(SalePriceNotExistMessageCode);
            throw new ServiceException(message);
        }
        SalePriceResource salePriceResource =
                this.salePriceResourceAssembler.toResource(salePrice);
        List<Link> linkListSalePriceResource = salePriceResource.getLinks();
        Message message = this.messageFactory.getMessage(SalePriceReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListSalePriceResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(salePriceResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(SalePriceReadPermissionName)
    public ResponseEntity<Resources<SalePriceResource>> getSalePrices(
            @RequestParam(value = "departmentCode", required = false) String departmentCode,
            @RequestParam(value = "matvalueCode", required = false) String matvalueCode,
            @RequestParam(value = "currencyCode", required = false) String currencyCode,
            @RequestParam(value = "start", required = false) String startText,
            @RequestParam(value = "size", required = false) String sizeText)
            throws ServiceException {

        if (departmentCode != null && !departmentCode.trim().isEmpty()) {
            departmentCode = departmentCode.trim();
        }
        if (matvalueCode != null && !matvalueCode.trim().isEmpty()) {
            matvalueCode = matvalueCode.trim();
        }
        if (currencyCode != null && !currencyCode.trim().isEmpty()) {
            currencyCode = currencyCode.trim();
        }
        Integer start = 0, size = 0;
        if(startText != null && !startText.trim().isEmpty()) {
            try {
                start = this.conversionService.convert(startText, Integer.class);
            } catch (IllegalArgumentException illegalArgumentException) {
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

        List<SalePrice> salePriceList = this.salePriceService.getSalePriceList(
                departmentCode, matvalueCode, currencyCode, start, size);
        if(salePriceList == null || salePriceList.isEmpty()) {
            Message message = this.messageFactory.getMessage(SalePriceListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<SalePriceResource> salePriceResourceList =
                this.salePriceResourceAssembler.toResources(salePriceList);
        Link linkSalePriceResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<SalePriceResource> salePriceResources =
                new Resources<>(salePriceResourceList, linkSalePriceResourceList);

        Message message = this.messageFactory.getMessage(SalePriceListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkSalePriceResourceList.getRel(), linkSalePriceResourceList.getHref());
        return new ResponseEntity<>(salePriceResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}