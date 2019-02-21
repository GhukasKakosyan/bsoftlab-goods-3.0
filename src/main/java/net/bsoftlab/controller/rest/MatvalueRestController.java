package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.MatvalueService;
import net.bsoftlab.utility.UtilityFunctions;

import net.bsoftlab.resource.assembler.MatvalueResourceAssembler;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.validator.MatvalueResourceValidator;

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
@RequestMapping(path = "resources/matvalues")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueRestController {

    private static final String MatvalueDeletedMessageCode = "matvalue.deleted";
    private static final String MatvalueInsertedMessageCode = "matvalue.inserted";
    private static final String MatvalueNotExistMessageCode = "matvalue.not.exist";
    private static final String MatvalueReadMessageCode = "matvalue.read";
    private static final String MatvalueUpdatedMessageCode = "matvalue.updated";
    private static final String MatvalueListEmptyMessageCode = "matvalueList.empty";
    private static final String MatvalueListReadMessageCode = "matvalueList.read";

    private static final String MatvalueDeletePermissionName = "permission.matvalue.delete";
    private static final String MatvalueInsertPermissionName = "permission.matvalue.insert";
    private static final String MatvalueReadPermissionName = "permission.matvalue.read";
    private static final String MatvalueUpdatePermissionName = "permission.matvalue.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterCodeInvalidMessageCode = "parameter.code.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private MatvalueResourceAssembler matvalueResourceAssembler;
    private MatvalueResourceValidator matvalueResourceValidator;
    private MatvalueService matvalueService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public MatvalueRestController(
            MatvalueResourceAssembler matvalueResourceAssembler,
            MatvalueResourceValidator matvalueResourceValidator,
            MatvalueService matvalueServiceImpl){

        this.matvalueResourceAssembler = matvalueResourceAssembler;
        this.matvalueResourceValidator = matvalueResourceValidator;
        this.matvalueService = matvalueServiceImpl;
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
    @Secured(MatvalueDeletePermissionName)
    public ResponseEntity<Message> deleteMatvalue(
            @PathVariable(value = "code") String code) throws ServiceException {

        this.matvalueService.deleteMatvalue(code);
        Message message = this.messageFactory.getMessage(MatvalueDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(MatvalueInsertPermissionName)
    public ResponseEntity<Message> insertMatvalue(
            @RequestBody MatvalueResource matvalueResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(matvalueResource, "matvalueResource");
        ValidationUtils.invokeValidator(this.matvalueResourceValidator,
                matvalueResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Matvalue matvalue = this.conversionService
                .convert(matvalueResource, Matvalue.class);
        this.matvalueService.insertMatvalue(matvalue);
        Message message = this.messageFactory.getMessage(MatvalueInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(MatvalueUpdatePermissionName)
    public ResponseEntity<Message> updateMatvalue(
            @PathVariable(value = "code") String code,
            @RequestBody MatvalueResource matvalueResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(matvalueResource, "matvalueResource");
        ValidationUtils.invokeValidator(this.matvalueResourceValidator,
                matvalueResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }
        Matvalue matvalue = this.conversionService
                .convert(matvalueResource, Matvalue.class);
        if(!code.equals(matvalue.getCode())) {
            Message message = this.messageFactory.getMessage(ParameterCodeInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.matvalueService.updateMatvalue(matvalue);
        Message message = this.messageFactory.getMessage(MatvalueUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(MatvalueReadPermissionName)
    public ResponseEntity<MatvalueResource> getMatvalue(
            @PathVariable(value = "code") String code)
            throws ServiceException {

        Matvalue matvalue = this.matvalueService.getMatvalue(code);
        if(matvalue == null) {
            Message message = this.messageFactory.getMessage(MatvalueNotExistMessageCode);
            throw new ServiceException(message);
        }
        MatvalueResource matvalueResource =
                this.matvalueResourceAssembler.toResource(matvalue);
        List<Link> linkListMatvalueResource = matvalueResource.getLinks();
        Message message = this.messageFactory.getMessage(MatvalueReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListMatvalueResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(matvalueResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(MatvalueReadPermissionName)
    public ResponseEntity<Resources<MatvalueResource>> getMatvalues(
            @RequestParam(value = "groupCode", required = false) String groupCode,
            @RequestParam(value = "unitofmsrCode", required = false) String unitofmsrCode,
            @RequestParam(value = "start", required = false) String startText,
            @RequestParam(value = "size", required = false) String sizeText)
            throws ServiceException {

        if (groupCode != null && !groupCode.trim().isEmpty()) {
            groupCode = groupCode.trim();
        }
        if (unitofmsrCode != null && !unitofmsrCode.trim().isEmpty()) {
            unitofmsrCode = unitofmsrCode.trim();
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

        List<Matvalue> matvalueList = this.matvalueService
                .getMatvalueList(groupCode, unitofmsrCode, start, size);
        if(matvalueList == null || matvalueList.isEmpty()) {
            Message message = this.messageFactory.getMessage(MatvalueListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<MatvalueResource> matvalueResourceList =
                this.matvalueResourceAssembler.toResources(matvalueList);
        Link linkMatvalueResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<MatvalueResource> matvalueResources =
                new Resources<>(matvalueResourceList, linkMatvalueResourceList);

        Message message = this.messageFactory.getMessage(MatvalueListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkMatvalueResourceList.getRel(), linkMatvalueResourceList.getHref());
        return new ResponseEntity<>(matvalueResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}