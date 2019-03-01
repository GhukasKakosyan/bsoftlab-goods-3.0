package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Permission;
import net.bsoftlab.resource.assembler.PermissionResourceAssembler;
import net.bsoftlab.resource.PermissionResource;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.PermissionService;
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

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.lang.IllegalArgumentException;
import java.util.List;

@RestController
@RequestMapping(path = "resources/permissions")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PermissionRestController {

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterIdInvalidMessageCode = "parameter.id.invalid";

    private static final String PermissionListEmptyMessageCode = "permissionList.empty";
    private static final String PermissionListReadMessageCode = "permissionList.read";
    private static final String PermissionNotExistMessageCode = "permission.not.exist";
    private static final String PermissionReadMessageCode = "permission.read";

    private PermissionResourceAssembler permissionResourceAssembler;
    private PermissionService permissionService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public PermissionRestController(
            PermissionResourceAssembler permissionResourceAssembler,
            PermissionService permissionServiceImpl) {
        this.permissionResourceAssembler = permissionResourceAssembler;
        this.permissionService = permissionServiceImpl;
    }

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

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

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

    @RequestMapping(value = "{ID}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PermissionResource> getPermission(
            @PathVariable(value = "ID") String IDText)
            throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }
        Permission permission = this.permissionService.getPermission(ID);
        if(permission == null) {
            Message message = this.messageFactory.getMessage(PermissionNotExistMessageCode);
            throw new ServiceException(message);
        }

        PermissionResource permissionResource =
                this.permissionResourceAssembler.toResource(permission);
        List<Link> linkListPermissionResource = permissionResource.getLinks();
        Message message = this.messageFactory.getMessage(PermissionReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListPermissionResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(permissionResource, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resources<PermissionResource>> getPermissions()
            throws ServiceException {

        List<Permission> permissionList = this.permissionService.getPermissionList();
        if (permissionList == null || permissionList.isEmpty()) {
            Message message = this.messageFactory.getMessage(PermissionListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<PermissionResource> permissionResourceList =
                this.permissionResourceAssembler.toResources(permissionList);
        Link linkPermissionResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<PermissionResource> permissionResources =
                new Resources<>(permissionResourceList, linkPermissionResourceList);

        Message message = this.messageFactory.getMessage(PermissionListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkPermissionResourceList.getRel(),
                linkPermissionResourceList.getHref());
        return new ResponseEntity<>(permissionResources, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }
}