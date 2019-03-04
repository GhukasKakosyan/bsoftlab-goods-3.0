package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Group;
import net.bsoftlab.resource.assembler.GroupResourceAssembler;
import net.bsoftlab.resource.container.GroupResourceListContainer;
import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.validator.GroupResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.GroupService;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "groups")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupWebController {

    private static final String GroupDeletedMessageCode = "group.deleted";
    private static final String GroupInsertedMessageCode = "group.inserted";
    private static final String GroupNotExistMessageCode = "group.not.exist";
    private static final String GroupNotSelectedMessageCode = "group.not.selected";
    private static final String GroupNotSingleSelectedMessageCode = "group.not.single.selected";
    private static final String GroupReadMessageCode = "group.read";
    private static final String GroupUpdatedMessageCode = "group.updated";

    private static final String GroupDeletePermissionName = "permission.group.delete";
    private static final String GroupInsertPermissionName = "permission.group.insert";
    private static final String GroupReadPermissionName = "permission.group.read";
    private static final String GroupUpdatePermissionName = "permission.group.update";

    private static final String GroupAddWebViewerName = "groupAddWebViewer";
    private static final String GroupEditWebViewerName = "groupEditWebViewer";
    private static final String GroupListWebViewerName = "groupListWebViewer";

    private GroupResourceAssembler groupResourceAssembler;
    private GroupResourceValidator groupResourceValidator;
    private GroupService groupService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public GroupWebController(
            GroupResourceAssembler groupResourceAssembler,
            GroupResourceValidator groupResourceValidator,
            GroupService groupServiceImpl) {

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
        String error = Functions.getPrintStackTrace(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = GroupInsertPermissionName)
    public ModelAndView addGroup() {
        GroupResource groupResource = new GroupResource();
        groupResource.setInitialValues();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResource", groupResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(GroupAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = GroupUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editGroup(
            @ModelAttribute("groupResourceListContainer")
            GroupResourceListContainer groupResourceListContainer) {

        List<GroupResource> groupResourceList =
                groupResourceListContainer.getGroupResourceList();
        List<GroupResource> selectedGroupResourceList;
        List<Group> selectedGroupList = null;
        if (groupResourceList != null) {
            selectedGroupResourceList = groupResourceList.stream()
                    .filter(GroupResource::getSelected).collect(Collectors.toList());
            selectedGroupList = (List<Group>) this.conversionService
                    .convert(selectedGroupResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(GroupResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Group.class)));
        }
        try {
            if (selectedGroupList == null || selectedGroupList.isEmpty()) {
                Message message = this.messageFactory.getMessage(GroupNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedGroupList.size() > 1) {
                Message message = this.messageFactory.getMessage(GroupNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Group selectedGroup = selectedGroupList.get(0);
            Group group = this.groupService.getGroup(selectedGroup.getCode());
            if(group == null) {
                Message message = this.messageFactory.getMessage(GroupNotExistMessageCode);
                throw new ServiceException(message);
            }
            GroupResource groupResource = this.groupResourceAssembler.toResource(group);

            Message message = this.messageFactory.getMessage(GroupReadMessageCode);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResource", groupResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(GroupEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Group> groupList = this.groupService.getGroupList();
            groupResourceList = this.groupResourceAssembler.toResources(groupList);
            groupResourceListContainer.setGroupResourceList(groupResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(GroupListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = GroupDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteGroup (
            @ModelAttribute("groupResourceListContainer")
            GroupResourceListContainer groupResourceListContainer) {

        Message message;
        List<GroupResource> groupResourceList =
                groupResourceListContainer.getGroupResourceList();
        List<GroupResource> selectedGroupResourceList;
        List<Group> selectedGroupList = null;
        if (groupResourceList != null) {
            selectedGroupResourceList = groupResourceList.stream()
                    .filter(GroupResource::getSelected).collect(Collectors.toList());
            selectedGroupList = (List<Group>) this.conversionService
                    .convert(selectedGroupResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(GroupResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Group.class)));
        }

        try {
            if (selectedGroupList == null || selectedGroupList.isEmpty()) {
                message = this.messageFactory.getMessage(GroupNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedGroupList.size() > 1) {
                message = this.messageFactory.getMessage(GroupNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Group selectedGroup = selectedGroupList.get(0);
            this.groupService.deleteGroup(selectedGroup.getCode());
            message = this.messageFactory.getMessage(GroupDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }
        List<Group> groupList = this.groupService.getGroupList();
        groupResourceList = this.groupResourceAssembler.toResources(groupList);
        groupResourceListContainer.setGroupResourceList(groupResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setViewName(GroupListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = GroupInsertPermissionName)
    public ModelAndView insertGroup (
            @ModelAttribute("groupResource") GroupResource groupResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(groupResource, "groupResource");
            ValidationUtils.invokeValidator(this.groupResourceValidator,
                    groupResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Group group = this.conversionService.convert(groupResource, Group.class);
            this.groupService.insertGroup(group);
            groupResource = new GroupResource();
            groupResource.setInitialValues();
            message = this.messageFactory.getMessage(GroupInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResource", groupResource);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(GroupAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = GroupUpdatePermissionName)
    public ModelAndView updateGroup (
            @ModelAttribute("groupResource") GroupResource groupResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(groupResource, "groupResource");
            ValidationUtils.invokeValidator(this.groupResourceValidator,
                    groupResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Group group = this.conversionService.convert(groupResource, Group.class);
            this.groupService.updateGroup(group);
            List<Group> groupList = this.groupService.getGroupList();
            List<GroupResource> groupResourceList =
                    this.groupResourceAssembler.toResources(groupList);
            GroupResourceListContainer
                    groupResourceListContainer = new GroupResourceListContainer();
            groupResourceListContainer.setGroupResourceList(groupResourceList);

            Message message = this.messageFactory.getMessage(GroupUpdatedMessageCode);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(GroupListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResource", groupResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(GroupEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = GroupReadPermissionName)
    public ModelAndView getGroups() {
        List<Group> groupList = this.groupService.getGroupList();
        List<GroupResource> groupResourceList = this.groupResourceAssembler.toResources(groupList);
        GroupResourceListContainer groupResourceListContainer = new GroupResourceListContainer();
        groupResourceListContainer.setGroupResourceList(groupResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
        modelAndView.setViewName(GroupListWebViewerName);
        return modelAndView;
    }
}