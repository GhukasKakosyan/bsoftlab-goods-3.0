package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.resource.WorkmanResource;
import net.bsoftlab.resource.assembler.RoleResourceAssembler;
import net.bsoftlab.resource.container.RoleResourceListContainer;
import net.bsoftlab.resource.validator.WorkmanResourceValidator;
import net.bsoftlab.service.RoleService;
import net.bsoftlab.service.WorkmanService;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanWebController {

    private static final String WorkmanInsertedMessageCode = "workman.inserted";

    private RoleResourceAssembler roleResourceAssembler = null;
    private RoleService roleService = null;

    private WorkmanResourceValidator workmanResourceValidator;
    private WorkmanService workmanService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public WorkmanWebController(
            WorkmanResourceValidator workmanResourceValidator,
            WorkmanService workmanServiceImpl) {
        this.workmanResourceValidator = workmanResourceValidator;
        this.workmanService = workmanServiceImpl;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    public void setRoleResourceAssembler(
            RoleResourceAssembler roleResourceAssembler) {
        this.roleResourceAssembler = roleResourceAssembler;
    }
    @Autowired
    public void setRoleService(
            RoleService roleService) {
        this.roleService = roleService;
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

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Message> handleException(Throwable throwable) {
        String error = Functions.ThrowableToString.apply(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = {"/", "/index"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public String getIndex() {
        return "index";
    }

    @RequestMapping(path = "/signin", method = {RequestMethod.GET, RequestMethod.POST})
    public String signinWorkman() {
        return "signin";
    }

    @RequestMapping(path = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String registerWorkman(Model model) {
        List<Role> roleList = this.roleService.getRoleList();
        List<RoleResource> roleResourceList = this.roleResourceAssembler.toResources(roleList);
        RoleResourceListContainer roleResourceListContainer = new RoleResourceListContainer();
        roleResourceListContainer.setRoleResourceList(roleResourceList);
        RoleResource roleResource = roleResourceList.get(0);

        WorkmanResource workmanResource = new WorkmanResource();
        workmanResource.setInitialValues();
        workmanResource.setRoleResource(roleResource);
        model.addAttribute("roleResourceListContainer", roleResourceListContainer);
        model.addAttribute("workmanResource", workmanResource);
        return "register";
    }

    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    public String insertWorkman(
            @ModelAttribute("workmanResource")
            WorkmanResource workmanResource, Model model) {
        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(workmanResource, "workmanResource");
            ValidationUtils.invokeValidator(this.workmanResourceValidator,
                    workmanResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if (objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Workman workman = this.conversionService
                    .convert(workmanResource, Workman.class);
            this.workmanService.insertWorkman(workman);
            workmanResource = new WorkmanResource();
            workmanResource.setInitialValues();
            message = this.messageFactory.getMessage(WorkmanInsertedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<Role> roleList = this.roleService.getRoleList();
        List<RoleResource> roleResourceList =
                this.roleResourceAssembler.toResources(roleList);
        RoleResourceListContainer roleResourceListContainer = new RoleResourceListContainer();
        roleResourceListContainer.setRoleResourceList(roleResourceList);

        model.addAttribute("message", message);
        model.addAttribute("roleResourceListContainer", roleResourceListContainer);
        model.addAttribute("workmanResource", workmanResource);
        return "register";
    }
}