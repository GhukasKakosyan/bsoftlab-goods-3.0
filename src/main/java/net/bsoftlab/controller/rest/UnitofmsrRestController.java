package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.resource.assembler.UnitofmsrResourceAssembler;
import net.bsoftlab.resource.UnitofmsrResource;
import net.bsoftlab.resource.validator.UnitofmsrResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.UnitofmsrService;
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
@RequestMapping(path = "resources/unitsofmsrs")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrRestController {

    private static final String UnitofmsrDeletedMessageCode = "unitofmsr.deleted";
    private static final String UnitofmsrInsertedMessageCode = "unitofmsr.inserted";
    private static final String UnitofmsrNotExistMessageCode = "unitofmsr.not.exist";
    private static final String UnitofmsrReadMessageCode = "unitofmsr.read";
    private static final String UnitofmsrUpdatedMessageCode = "unitofmsr.updated";
    private static final String UnitofmsrListEmptyMessageCode = "unitofmsrList.empty";
    private static final String UnitofmsrListReadMessageCode = "unitofmsrList.read";

    private static final String UnitofmsrDeletePermissionName = "permission.unitofmsr.delete";
    private static final String UnitofmsrInsertPermissionName = "permission.unitofmsr.insert";
    private static final String UnitofmsrReadPermissionName = "permission.unitofmsr.read";
    private static final String UnitofmsrUpdatePermissionName = "permission.unitofmsr.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterCodeInvalidMessageCode = "parameter.code.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private UnitofmsrResourceAssembler unitofmsrResourceAssembler;
    private UnitofmsrResourceValidator unitofmsrResourceValidator;
    private UnitofmsrService unitofmsrService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public UnitofmsrRestController(
            UnitofmsrResourceAssembler unitofmsrResourceAssembler,
            UnitofmsrResourceValidator unitofmsrResourceValidator,
            UnitofmsrService unitofmsrServiceImpl){

        this.unitofmsrResourceAssembler = unitofmsrResourceAssembler;
        this.unitofmsrResourceValidator = unitofmsrResourceValidator;
        this.unitofmsrService = unitofmsrServiceImpl;
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

    @RequestMapping(value = "{code}", method = RequestMethod.DELETE)
    @Secured(UnitofmsrDeletePermissionName)
    public ResponseEntity<Message> deleteUnitofmsr(
            @PathVariable(value = "code") String code) throws ServiceException {

        this.unitofmsrService.deleteUnitofmsr(code);
        Message message = this.messageFactory.getMessage(UnitofmsrDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(UnitofmsrInsertPermissionName)
    public ResponseEntity<Message> insertUnitofmsr(
            @RequestBody UnitofmsrResource unitofmsrResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(unitofmsrResource, "unitofmsrResource");
        ValidationUtils.invokeValidator(this.unitofmsrResourceValidator,
                unitofmsrResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Unitofmsr unitofmsr = this.conversionService
                .convert(unitofmsrResource, Unitofmsr.class);
        this.unitofmsrService.insertUnitofmsr(unitofmsr);
        Message message = this.messageFactory.getMessage(UnitofmsrInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(UnitofmsrUpdatePermissionName)
    public ResponseEntity<Message> updateUnitofmsr(
            @PathVariable(value = "code") String code,
            @RequestBody UnitofmsrResource unitofmsrResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(unitofmsrResource, "unitofmsrResource");
        ValidationUtils.invokeValidator(this.unitofmsrResourceValidator,
                unitofmsrResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }
        Unitofmsr unitofmsr = this.conversionService
                .convert(unitofmsrResource, Unitofmsr.class);
        if(!code.equals(unitofmsr.getCode())) {
            Message message = this.messageFactory.getMessage(ParameterCodeInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.unitofmsrService.updateUnitofmsr(unitofmsr);
        Message message = this.messageFactory.getMessage(UnitofmsrUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(UnitofmsrReadPermissionName)
    public ResponseEntity<UnitofmsrResource> getUnitofmsr(
            @PathVariable(value = "code") String code) throws ServiceException {

        Unitofmsr unitofmsr = this.unitofmsrService.getUnitofmsr(code);
        if(unitofmsr == null) {
            Message message = this.messageFactory.getMessage(UnitofmsrNotExistMessageCode);
            throw new ServiceException(message);
        }
        UnitofmsrResource unitofmsrResource =
                this.unitofmsrResourceAssembler.toResource(unitofmsr);
        List<Link> linkListUnitofmsrResource = unitofmsrResource.getLinks();
        Message message = this.messageFactory.getMessage(UnitofmsrReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListUnitofmsrResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(unitofmsrResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(UnitofmsrReadPermissionName)
    public ResponseEntity<Resources<UnitofmsrResource>> getUnitofmsrs(
            @RequestParam(value = "start", required = false) String startText,
            @RequestParam(value = "size", required = false) String sizeText)
            throws ServiceException {

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

        List<Unitofmsr> unitofmsrList =
                this.unitofmsrService.getUnitofmsrList(start, size);
        if(unitofmsrList == null || unitofmsrList.isEmpty()) {
            Message message = this.messageFactory.getMessage(UnitofmsrListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<UnitofmsrResource> unitofmsrResourceList =
                this.unitofmsrResourceAssembler.toResources(unitofmsrList);
        Link linkUnitofmsrResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<UnitofmsrResource> unitofmsrResources =
                new Resources<>(unitofmsrResourceList, linkUnitofmsrResourceList);

        Message message = this.messageFactory.getMessage(UnitofmsrListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkUnitofmsrResourceList.getRel(),
                linkUnitofmsrResourceList.getHref());
        return new ResponseEntity<>(unitofmsrResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}