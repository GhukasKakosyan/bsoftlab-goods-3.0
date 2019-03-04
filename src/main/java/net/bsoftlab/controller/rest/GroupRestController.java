package net.bsoftlab.controller.rest;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Group;
import net.bsoftlab.resource.assembler.GroupResourceAssembler;
import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.validator.GroupResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.GroupService;
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
@RequestMapping(path = "resources/groups")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupRestController {

    private static final String GroupDeletedMessageCode = "group.deleted";
    private static final String GroupInsertedMessageCode = "group.inserted";
    private static final String GroupNotExistMessageCode = "group.not.exist";
    private static final String GroupReadMessageCode = "group.read";
    private static final String GroupUpdatedMessageCode = "group.updated";
    private static final String GroupListEmptyMessageCode = "groupList.empty";
    private static final String GroupListReadMessageCode = "groupList.read";

    private static final String GroupDeletePermissionName = "permission.group.delete";
    private static final String GroupInsertPermissionName = "permission.group.insert";
    private static final String GroupReadPermissionName = "permission.group.read";
    private static final String GroupUpdatePermissionName = "permission.group.update";

    private static final String ParameterHttpBodyNotConvertedMessageCode = "parameter.http.body.not.converted";
    private static final String ParameterCodeInvalidMessageCode = "parameter.code.invalid";
    private static final String ParameterStartInvalidMessageCode = "parameter.start.invalid";
    private static final String ParameterSizeInvalidMessageCode = "parameter.size.invalid";

    private GroupResourceAssembler groupResourceAssembler;
    private GroupResourceValidator groupResourceValidator;
    private GroupService groupService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public GroupRestController(
            GroupResourceAssembler groupResourceAssembler,
            GroupResourceValidator groupResourceValidator,
            GroupService groupServiceImpl){

        this.groupResourceAssembler = groupResourceAssembler;
        this.groupResourceValidator = groupResourceValidator;
        this.groupService = groupServiceImpl;
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
            String error = Functions.getPrintStackTrace(throwable);
            message = this.messageFactory.getInternalServerErrorMessage(error);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.DELETE)
    @Secured(GroupDeletePermissionName)
    public ResponseEntity<Message> deleteGroup(
            @PathVariable(value = "code") String ID) throws ServiceException {

        this.groupService.deleteGroup(ID);
        Message message = this.messageFactory.getMessage(GroupDeletedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(GroupInsertPermissionName)
    public ResponseEntity<Message> insertGroup(
            @RequestBody GroupResource groupResource) throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(groupResource, "groupResource");
        ValidationUtils.invokeValidator(this.groupResourceValidator,
                groupResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }

        Group group = this.conversionService.convert(groupResource, Group.class);
        this.groupService.insertGroup(group);
        Message message = this.messageFactory.getMessage(GroupInsertedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(GroupUpdatePermissionName)
    public ResponseEntity<Message> updateGroup(
            @PathVariable(value = "code") String code,
            @RequestBody GroupResource groupResource)
            throws ServiceException {

        BeanPropertyBindingResult beanPropertyBindingResult =
                new BeanPropertyBindingResult(groupResource, "groupResource");
        ValidationUtils.invokeValidator(this.groupResourceValidator,
                groupResource, beanPropertyBindingResult);
        List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
        if(objectErrorList != null && objectErrorList.size() > 0) {
            String codeMessage = objectErrorList.get(0).getCode();
            Message message = this.messageFactory.getMessage(codeMessage);
            throw new ServiceException(message);
        }
        Group group = this.conversionService.convert(groupResource, Group.class);
        if(!code.equals(group.getCode())) {
            Message message = this.messageFactory.getMessage(ParameterCodeInvalidMessageCode);
            throw new ServiceException(message);
        }

        this.groupService.updateGroup(group);
        Message message = this.messageFactory.getMessage(GroupUpdatedMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        return new ResponseEntity<>(message,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(value = "{code}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(GroupReadPermissionName)
    public ResponseEntity<GroupResource> getGroup(
            @PathVariable(value = "code") String code) throws ServiceException {

        Group group = this.groupService.getGroup(code);
        if(group == null) {
            Message message = this.messageFactory.getMessage(GroupNotExistMessageCode);
            throw new ServiceException(message);
        }
        GroupResource groupResource = this.groupResourceAssembler.toResource(group);
        List<Link> linkListGroupResource = groupResource.getLinks();
        Message message = this.messageFactory.getMessage(GroupReadMessageCode);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        for(Link link : linkListGroupResource)
            httpHeaders.add(link.getRel(), link.getHref());
        return new ResponseEntity<>(groupResource,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured(GroupReadPermissionName)
    public ResponseEntity<Resources<GroupResource>> getGroups(
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

        List<Group> groupList = this.groupService.getGroupList(start, size);
        if(groupList == null || groupList.isEmpty()) {
            Message message = this.messageFactory.getMessage(GroupListEmptyMessageCode);
            throw new ServiceException(message);
        }
        List<GroupResource> groupResourceList =
                this.groupResourceAssembler.toResources(groupList);
        Link linkGroupResourceList =
                ControllerLinkBuilder.linkTo(this.getClass()).withSelfRel();
        Resources<GroupResource> groupResources =
                new Resources<>(groupResourceList, linkGroupResourceList);

        Message message = this.messageFactory.getMessage(GroupListReadMessageCode);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("message", message.getEnglishText());
        httpHeaders.add(linkGroupResourceList.getRel(), linkGroupResourceList.getHref());
        return new ResponseEntity<>(groupResources,
                httpHeaders, HttpStatus.valueOf(message.getHttpStatus()));
    }
}