package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Department;
import net.bsoftlab.resource.assembler.DepartmentResourceAssembler;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.validator.DepartmentResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.DepartmentService;
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
@RequestMapping(path = "resources/departments")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentRestController {

    private static final String DepartmentDeletedMessageCode = "department.deleted";
    private static final String DepartmentInsertedMessageCode = "department.inserted";
    private static final String DepartmentNotExistMessageCode = "department.not.exist";
    private static final String DepartmentReadMessageCode = "department.read";
    private static final String DepartmentUpdatedMessageCode = "department.updated";
    private static final String DepartmentListEmptyMessageCode = "departmentList.empty";
    private static final String DepartmentListReadMessageCode = "departmentList.read";

    private static final String DepartmentDeletePermissionName = "permission.department.delete";
    private static final String DepartmentInsertPermissionName = "permission.department.insert";
    private static final String DepartmentReadPermissionName = "permission.department.read";
    private static final String DepartmentUpdatePermissionName = "permission.department.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterCodeInvalidMessageCode = "parameter.code.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private DepartmentResourceAssembler departmentResourceAssembler;
    private DepartmentResourceValidator departmentResourceValidator;
    private DepartmentService departmentService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public DepartmentRestController(
            DepartmentResourceAssembler departmentResourceAssembler,
            DepartmentResourceValidator departmentResourceValidator,
            DepartmentService departmentServiceImpl){

        this.departmentResourceAssembler = departmentResourceAssembler;
        this.departmentResourceValidator = departmentResourceValidator;
        this.departmentService = departmentServiceImpl;
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
            String error = Functions.ThrowableToString.apply(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.DELETE)
    @Secured(DepartmentDeletePermissionName)
    public ResponseEntity<Message> deleteDepartment(
            @PathVariable(value = "code") String code) throws ServiceException {

        this.departmentService.deleteDepartment(code);
        Message message = this.messageFactory.getMessage(DepartmentDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(DepartmentInsertPermissionName)
    public ResponseEntity<Message> insertDepartment(
            @RequestBody DepartmentResource departmentResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(departmentResource, "departmentResource");
        ValidationUtils.invokeValidator(this.departmentResourceValidator,
                departmentResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Department department = this.conversionService
                .convert(departmentResource, Department.class);
        this.departmentService.insertDepartment(department);
        Message message = this.messageFactory.getMessage(DepartmentInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(DepartmentUpdatePermissionName)
    public ResponseEntity<Message> updateDepartment(
            @PathVariable(value = "code") String code,
            @RequestBody DepartmentResource departmentResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(departmentResource, "departmentResource");
        ValidationUtils.invokeValidator(this.departmentResourceValidator,
                departmentResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && !objectErrorList.isEmpty()) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }
        Department department = this.conversionService
                .convert(departmentResource, Department.class);
        if(!code.equals(department.getCode())) {
            Message message = this.messageFactory.getMessage(ParameterCodeInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.departmentService.updateDepartment(department);
        Message message = this.messageFactory.getMessage(DepartmentUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(DepartmentReadPermissionName)
    public ResponseEntity<DepartmentResource> getDepartment(
            @PathVariable(value = "code") String code)
            throws ServiceException {

        Department department = this.departmentService.getDepartment(code);
        if(department == null) {
            Message message = this.messageFactory.getMessage(DepartmentNotExistMessageCode);
            throw new ServiceException(message);
        }
        DepartmentResource departmentResource =
                this.departmentResourceAssembler.toResource(department);
        List<Link> linkListDepartmentResource = departmentResource.getLinks();
        Message message = this.messageFactory.getMessage(DepartmentReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for(Link link : linkListDepartmentResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(departmentResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(DepartmentReadPermissionName)
    public ResponseEntity<Resources<DepartmentResource>> getDepartments(
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

        List<Department> departmentList =
                this.departmentService.getDepartmentList(start, size);
        if(departmentList == null || departmentList.isEmpty()) {
            Message message = this.messageFactory.getMessage(DepartmentListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<DepartmentResource> departmentResourceList =
                this.departmentResourceAssembler.toResources(departmentList);
        Link linkDepartmentResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<DepartmentResource> departmentResources =
                new Resources<>(departmentResourceList, linkDepartmentResourceList);

        Message message = this.messageFactory.getMessage(DepartmentListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkDepartmentResourceList.getRel(),
                linkDepartmentResourceList.getHref());
        return new ResponseEntity<>(departmentResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}