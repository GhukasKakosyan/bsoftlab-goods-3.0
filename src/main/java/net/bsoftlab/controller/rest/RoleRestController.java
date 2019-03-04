package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Role;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.RoleService;
import net.bsoftlab.resource.assembler.RoleResourceAssembler;
import net.bsoftlab.resource.RoleResource;
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
@RequestMapping(path = "resources/roles")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleRestController {

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterIdInvalidMessageCode = "parameter.id.invalid";

    private static final String RoleListEmptyMessageCode = "roleList.empty";
    private static final String RoleListReadMessageCode = "roleList.read";
    private static final String RoleNotExistMessageCode = "role.not.exist";
    private static final String RoleReadMessageCode = "role.read";

    private RoleResourceAssembler roleResourceAssembler;
    private RoleService roleService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public RoleRestController(
            RoleResourceAssembler roleResourceAssembler,
            RoleService roleServiceImpl) {
        this.roleResourceAssembler = roleResourceAssembler;
        this.roleService = roleServiceImpl;
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
            String error = Functions.ThrowableToString.apply(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{ID}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RoleResource> getRole(
            @PathVariable(value = "ID") String IDText) throws ServiceException {

        Integer ID;
        try {
            ID = this.conversionService.convert(IDText, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            Message message = this.messageFactory.getMessage(ParameterIdInvalidMessageCode);
            throw new ServiceException(message);
        }

        Role role = this.roleService.getRole(ID);
        if(role == null) {
            Message message = this.messageFactory.getMessage(RoleNotExistMessageCode);
            throw new ServiceException(message);
        }

        RoleResource roleResource = this.roleResourceAssembler.toResource(role);
        List<Link> linkListRoleResource = roleResource.getLinks();
        Message message = this.messageFactory.getMessage(RoleReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for (Link link : linkListRoleResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(roleResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resources<RoleResource>> getRoles() throws ServiceException {

        List<Role> roleList = this.roleService.getRoleList();
        if(roleList == null || roleList.isEmpty()) {
            Message message = this.messageFactory.getMessage(RoleListEmptyMessageCode);
            throw new ServiceException(message);
        }

        List<RoleResource> roleResourceList =
                this.roleResourceAssembler.toResources(roleList);
        Link linkRoleResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<RoleResource> roleResources =
                new Resources<>(roleResourceList, linkRoleResourceList);

        Message message = this.messageFactory.getMessage(RoleListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkRoleResourceList.getRel(), linkRoleResourceList.getHref());
        return new ResponseEntity<>(roleResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}