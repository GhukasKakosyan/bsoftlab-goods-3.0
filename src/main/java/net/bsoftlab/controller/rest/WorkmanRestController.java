package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Workman;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.WorkmanService;
import net.bsoftlab.resource.assembler.WorkmanResourceAssembler;
import net.bsoftlab.resource.WorkmanResource;
import net.bsoftlab.resource.validator.WorkmanResourceValidator;
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

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.lang.IllegalArgumentException;
import java.util.List;

@RestController
@RequestMapping(path = "resources/workmans")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanRestController {

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterIdInvalidMessageCode = "parameter.id.invalid";

    private static final String WorkmanDeletedMessageCode = "workman.deleted";
    private static final String WorkmanInsertedMessageCode = "workman.inserted";
    private static final String WorkmanNotExistMessageCode = "workman.not.exist";
    private static final String WorkmanReadMessageCode = "workman.read";
    private static final String WorkmanUpdatedMessageCode = "workman.updated";
    private static final String WorkmanListEmptyMessageCode = "workmanList.empty";
    private static final String WorkmanListReadMessageCode = "workmanList.read";

    private WorkmanResourceAssembler workmanResourceAssembler;
    private WorkmanResourceValidator workmanResourceValidator;
    private WorkmanService workmanService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public WorkmanRestController(
            WorkmanResourceAssembler workmanResourceAssembler,
            WorkmanResourceValidator workmanResourceValidator,
            WorkmanService workmanServiceImpl) {

        this.workmanResourceAssembler = workmanResourceAssembler;
        this.workmanResourceValidator = workmanResourceValidator;
        this.workmanService = workmanServiceImpl;
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
    public ResponseEntity<Message> deleteWorkman(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.workmanService.deleteWorkman(ID);
        Message message = this.messageFactory.getMessage(WorkmanDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));

    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Message> insertWorkman(
            @RequestBody WorkmanResource workmanResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(workmanResource, "workmanResource");
        ValidationUtils.invokeValidator(this.workmanResourceValidator,
                workmanResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Workman workman = this.conversionService
                .convert(workmanResource, Workman.class);
        this.workmanService.insertWorkman(workman);
        Message message = this.messageFactory.getMessage(WorkmanInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Message> updateWorkman(
            @PathVariable(value = "ID") String IDText,
            @RequestBody WorkmanResource workmanResource)
            throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(workmanResource, "workmanResource");
        ValidationUtils.invokeValidator(this.workmanResourceValidator,
                workmanResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Workman workman = this.conversionService
                .convert(workmanResource, Workman.class);
        if(!ID.equals(workman.getID())) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.workmanService.updateWorkman(workman);
        Message message = this.messageFactory.getMessage(WorkmanUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkmanResource> getWorkman(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        Workman workman = this.workmanService.getWorkman(ID);
        if(workman == null) {
            Message message = this.messageFactory.getMessage(WorkmanNotExistMessageCode);
            throw new ServiceException(message);
        }

        WorkmanResource workmanResource =
                this.workmanResourceAssembler.toResource(workman);
        List<Link> linkListWorkmanResource = workmanResource.getLinks();
        Message message = this.messageFactory.getMessage(WorkmanReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListWorkmanResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(workmanResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resources<WorkmanResource>> getWorkmans()
            throws ServiceException {

        List<Workman> workmanList = this.workmanService.getWorkmanList();
        if(workmanList == null || workmanList.isEmpty()) {
            Message message = this.messageFactory.getMessage(WorkmanListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<WorkmanResource> workmanResourceList =
                this.workmanResourceAssembler.toResources(workmanList);
        Link linkWorkmanResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<WorkmanResource> workmanResources =
                new Resources<>(workmanResourceList, linkWorkmanResourceList);

        Message message = this.messageFactory.getMessage(WorkmanListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkWorkmanResourceList.getRel(), linkWorkmanResourceList.getHref());
        return new ResponseEntity<>(workmanResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}