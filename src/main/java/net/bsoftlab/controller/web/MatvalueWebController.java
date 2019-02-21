package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;

import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.GroupService;
import net.bsoftlab.service.MatvalueService;
import net.bsoftlab.service.UnitofmsrService;
import net.bsoftlab.utility.UtilityFunctions;

import net.bsoftlab.resource.assembler.GroupResourceAssembler;
import net.bsoftlab.resource.assembler.MatvalueResourceAssembler;
import net.bsoftlab.resource.assembler.UnitofmsrResourceAssembler;
import net.bsoftlab.resource.container.GroupResourceListContainer;
import net.bsoftlab.resource.container.MatvalueResourceListContainer;
import net.bsoftlab.resource.container.UnitofmsrResourceListContainer;
import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.UnitofmsrResource;
import net.bsoftlab.resource.validator.MatvalueResourceValidator;

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
@RequestMapping(path = "matvalues")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueWebController {

    private static final String MatvalueDeletedMessageCode = "matvalue.deleted";
    private static final String MatvalueInsertedMessageCode = "matvalue.inserted";
    private static final String MatvalueNotExistMessageCode = "matvalue.not.exist";
    private static final String MatvalueNotSelectedMessageCode = "matvalue.not.selected";
    private static final String MatvalueNotSingleSelectedMessageCode = "matvalue.not.single.selected";
    private static final String MatvalueReadMessageCode = "matvalue.read";
    private static final String MatvalueUpdatedMessageCode = "matvalue.updated";

    private static final String MatvalueDeletePermissionName = "permission.matvalue.delete";
    private static final String MatvalueInsertPermissionName = "permission.matvalue.insert";
    private static final String MatvalueReadPermissionName = "permission.matvalue.read";
    private static final String MatvalueUpdatePermissionName = "permission.matvalue.update";

    private static final String MatvalueAddWebViewerName = "matvalueAddWebViewer";
    private static final String MatvalueEditWebViewerName = "matvalueEditWebViewer";
    private static final String MatvalueListWebViewerName = "matvalueListWebViewer";

    private MatvalueResourceAssembler matvalueResourceAssembler;
    private MatvalueResourceValidator matvalueResourceValidator;
    private MatvalueService matvalueService;

    private GroupResourceAssembler groupResourceAssembler = null;
    private UnitofmsrResourceAssembler unitofmsrResourceAssembler = null;

    private GroupService groupService = null;
    private UnitofmsrService unitofmsrService = null;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public MatvalueWebController(
            MatvalueResourceAssembler matvalueResourceAssembler,
            MatvalueResourceValidator matvalueResourceValidator,
            MatvalueService matvalueServiceImpl) {

        this.matvalueResourceAssembler = matvalueResourceAssembler;
        this.matvalueResourceValidator = matvalueResourceValidator;
        this.matvalueService = matvalueServiceImpl;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    public void setGroupResourceAssembler(
            GroupResourceAssembler groupResourceAssembler) {
        this.groupResourceAssembler = groupResourceAssembler;
    }
    @Autowired
    public void setUnitofmsrResourceAssembler(
            UnitofmsrResourceAssembler unitofmsrResourceAssembler) {
        this.unitofmsrResourceAssembler = unitofmsrResourceAssembler;
    }

    @Autowired
    public void setGroupService(
            GroupService groupService) {
        this.groupService = groupService;
    }
    @Autowired
    public void setUnitofmsrService(
            UnitofmsrService unitofmsrService) {
        this.unitofmsrService = unitofmsrService;
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
    @Autowired
    public void setUtilityFunctions(
            UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Message> handleException(Throwable throwable) {
        String error = this.utilityFunctions.getPrintStackTrace(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = MatvalueInsertPermissionName)
    public ModelAndView addMatvalue() {

        List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
        List<UnitofmsrResource> unitofmsrResourceList =
                this.unitofmsrResourceAssembler.toResources(unitofmsrList);
        UnitofmsrResourceListContainer
                unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
        unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

        List<Group> groupList = this.groupService.getGroupList();
        List<GroupResource> groupResourceList =
                this.groupResourceAssembler.toResources(groupList);
        GroupResourceListContainer
                groupResourceListContainer = new GroupResourceListContainer();
        groupResourceListContainer.setGroupResourceList(groupResourceList);

        MatvalueResource matvalueResource = new MatvalueResource();
        matvalueResource.setInitialValues();
        if (unitofmsrResourceList != null && !unitofmsrResourceList.isEmpty()) {
            matvalueResource.setUnitofmsrResource(unitofmsrResourceList.get(0));
        }
        if (groupResourceList != null && !groupResourceList.isEmpty()) {
            matvalueResource.setGroupResource(groupResourceList.get(0));
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
        modelAndView.addObject("unitofmsrResourceListContainer", unitofmsrResourceListContainer);
        modelAndView.addObject("matvalueResource", matvalueResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(MatvalueAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = MatvalueUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editMatvalue (
            @ModelAttribute("matvalueResourceListContainer")
            MatvalueResourceListContainer matvalueResourceListContainer) {

        List<MatvalueResource> matvalueResourceList =
                matvalueResourceListContainer.getMatvalueResourceList();
        List<MatvalueResource> selectedMatvalueResourceList;
        List<Matvalue> selectedMatvalueList = null;
        if (matvalueResourceList != null) {
            selectedMatvalueResourceList = matvalueResourceList.stream()
                    .filter(MatvalueResource::getSelected).collect(Collectors.toList());
            selectedMatvalueList = (List<Matvalue>)
                    this.conversionService.convert(selectedMatvalueResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(MatvalueResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Matvalue.class)));
        }

        try {
            if (selectedMatvalueList == null || selectedMatvalueList.isEmpty()) {
                Message message = this.messageFactory.getMessage(MatvalueNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedMatvalueList.size() > 1) {
                Message message = this.messageFactory.getMessage(MatvalueNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Matvalue selectedMatvalue = selectedMatvalueList.get(0);
            Matvalue matvalue = this.matvalueService.getMatvalue(selectedMatvalue.getCode());
            if(matvalue == null) {
                Message message = this.messageFactory.getMessage(MatvalueNotExistMessageCode);
                throw new ServiceException(message);
            }
            MatvalueResource matvalueResource =
                    this.matvalueResourceAssembler.toResource(matvalue);

            List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
            List<UnitofmsrResource> unitofmsrResourceList =
                    this.unitofmsrResourceAssembler.toResources(unitofmsrList);
            UnitofmsrResourceListContainer
                    unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
            unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

            List<Group> groupList = this.groupService.getGroupList();
            List<GroupResource> groupResourceList =
                    this.groupResourceAssembler.toResources(groupList);
            GroupResourceListContainer
                    groupResourceListContainer = new GroupResourceListContainer();
            groupResourceListContainer.setGroupResourceList(groupResourceList);

            Message message = this.messageFactory.getMessage(MatvalueReadMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
            modelAndView.addObject("matvalueResource", matvalueResource);
            modelAndView.addObject("message", message);
            modelAndView.addObject("unitofmsrResourceListContainer", unitofmsrResourceListContainer);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(MatvalueEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
            matvalueResourceList =
                    this.matvalueResourceAssembler.toResources(matvalueList);
            matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(MatvalueListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = MatvalueDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteMatvalue (
            @ModelAttribute("matvalueResourceListContainer")
            MatvalueResourceListContainer matvalueResourceListContainer) {

        Message message;
        List<MatvalueResource> matvalueResourceList =
                matvalueResourceListContainer.getMatvalueResourceList();
        List<MatvalueResource> selectedMatvalueResourceList;
        List<Matvalue> selectedMatvalueList = null;
        if (matvalueResourceList != null) {
            selectedMatvalueResourceList = matvalueResourceList.stream()
                    .filter(MatvalueResource::getSelected).collect(Collectors.toList());
            selectedMatvalueList = (List<Matvalue>) this.conversionService
                    .convert(selectedMatvalueResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(MatvalueResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Matvalue.class)));
        }

        try {
            if (selectedMatvalueList == null || selectedMatvalueList.isEmpty()) {
                message = this.messageFactory.getMessage(MatvalueNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedMatvalueList.size() > 1) {
                message = this.messageFactory.getMessage(MatvalueNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Matvalue selectedMatvalue = selectedMatvalueList.get(0);
            this.matvalueService.deleteMatvalue(selectedMatvalue.getCode());
            message = this.messageFactory.getMessage(MatvalueDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }
        List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
        matvalueResourceList = this.matvalueResourceAssembler.toResources(matvalueList);
        matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(MatvalueListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = MatvalueInsertPermissionName)
    public ModelAndView insertMatvalue (
            @ModelAttribute("matvalueResource") MatvalueResource matvalueResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(matvalueResource, "matvalueResource");
            ValidationUtils.invokeValidator(this.matvalueResourceValidator,
                    matvalueResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Matvalue matvalue = this.conversionService
                    .convert(matvalueResource, Matvalue.class);
            this.matvalueService.insertMatvalue(matvalue);
            matvalueResource = new MatvalueResource();
            matvalueResource.setInitialValues();
            message = this.messageFactory.getMessage(MatvalueInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
        List<UnitofmsrResource> unitofmsrResourceList =
                this.unitofmsrResourceAssembler.toResources(unitofmsrList);
        UnitofmsrResourceListContainer
                unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
        unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

        List<Group> groupList = this.groupService.getGroupList();
        List<GroupResource> groupResourceList = this.groupResourceAssembler.toResources(groupList);
        GroupResourceListContainer
                groupResourceListContainer = new GroupResourceListContainer();
        groupResourceListContainer.setGroupResourceList(groupResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
        modelAndView.addObject("matvalueResource", matvalueResource);
        modelAndView.addObject("message", message);
        modelAndView.addObject("unitofmsrResourceListContainer", unitofmsrResourceListContainer);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(MatvalueAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = MatvalueUpdatePermissionName)
    public ModelAndView updateMatvalue (
            @ModelAttribute("matvalueResource") MatvalueResource matvalueResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(matvalueResource, "matvalueResource");
            ValidationUtils.invokeValidator(this.matvalueResourceValidator,
                    matvalueResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Matvalue matvalue = this.conversionService
                    .convert(matvalueResource, Matvalue.class);
            this.matvalueService.updateMatvalue(matvalue);
            List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
            List<MatvalueResource> matvalueResourceList =
                    this.matvalueResourceAssembler.toResources(matvalueList);
            MatvalueResourceListContainer
                    matvalueResourceListContainer = new MatvalueResourceListContainer();
            matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

            Message message = this.messageFactory.getMessage(MatvalueUpdatedMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(MatvalueListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
            List<UnitofmsrResource> unitofmsrResourceList =
                    this.unitofmsrResourceAssembler.toResources(unitofmsrList);
            UnitofmsrResourceListContainer
                    unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
            unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

            List<Group> groupList = this.groupService.getGroupList();
            List<GroupResource> groupResourceList =
                    this.groupResourceAssembler.toResources(groupList);
            GroupResourceListContainer
                    groupResourceListContainer = new GroupResourceListContainer();
            groupResourceListContainer.setGroupResourceList(groupResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("groupResourceListContainer", groupResourceListContainer);
            modelAndView.addObject("matvalueResource", matvalueResource);
            modelAndView.addObject("message", message);
            modelAndView.addObject("unitofmsrResourceListContainer", unitofmsrResourceListContainer);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(MatvalueEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = MatvalueReadPermissionName)
    public ModelAndView getMatvalues() {

        List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
        List<MatvalueResource> matvalueResourceList =
                this.matvalueResourceAssembler.toResources(matvalueList);
        MatvalueResourceListContainer
                matvalueResourceListContainer = new MatvalueResourceListContainer();
        matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
        modelAndView.setViewName(MatvalueListWebViewerName);
        return modelAndView;
    }
 }