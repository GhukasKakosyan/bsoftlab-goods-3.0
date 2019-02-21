package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.UnitofmsrService;
import net.bsoftlab.utility.UtilityFunctions;

import net.bsoftlab.resource.assembler.UnitofmsrResourceAssembler;
import net.bsoftlab.resource.container.UnitofmsrResourceListContainer;
import net.bsoftlab.resource.UnitofmsrResource;
import net.bsoftlab.resource.validator.UnitofmsrResourceValidator;

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
@RequestMapping(path = "unitsofmsrs")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrWebController {

    private static final String UnitofmsrDeletedMessageCode = "unitofmsr.deleted";
    private static final String UnitofmsrInsertedMessageCode = "unitofmsr.inserted";
    private static final String UnitofmsrNotExistMessageCode = "unitofmsr.not.exist";
    private static final String UnitofmsrNotSelectedMessageCode = "unitofmsr.not.selected";
    private static final String UnitofmsrNotSingleSelectedMessageCode = "unitofmsr.not.single.selected";
    private static final String UnitofmsrReadMessageCode = "unitofmsr.read";
    private static final String UnitofmsrUpdatedMessageCode = "unitofmsr.updated";

    private static final String UnitofmsrDeletePermissionName = "permission.unitofmsr.delete";
    private static final String UnitofmsrInsertPermissionName = "permission.unitofmsr.insert";
    private static final String UnitofmsrReadPermissionName = "permission.unitofmsr.read";
    private static final String UnitofmsrUpdatePermissionName = "permission.unitofmsr.update";

    private static final String UnitofmsrAddWebViewerName = "unitofmsrAddWebViewer";
    private static final String UnitofmsrEditWebViewerName = "unitofmsrEditWebViewer";
    private static final String UnitofmsrListWebViewerName = "unitofmsrListWebViewer";

    private UnitofmsrResourceAssembler unitofmsrResourceAssembler;
    private UnitofmsrResourceValidator unitofmsrResourceValidator;
    private UnitofmsrService unitofmsrService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public UnitofmsrWebController(
            UnitofmsrResourceAssembler unitofmsrResourceAssembler,
            UnitofmsrResourceValidator unitofmsrResourceValidator,
            UnitofmsrService unitofmsrServiceImpl) {

        this.unitofmsrResourceAssembler = unitofmsrResourceAssembler;
        this.unitofmsrResourceValidator = unitofmsrResourceValidator;
        this.unitofmsrService = unitofmsrServiceImpl;
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
        String error = this.utilityFunctions.getPrintStackTrace(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = UnitofmsrInsertPermissionName)
    public ModelAndView addUnitofmsr() {
        UnitofmsrResource unitofmsrResource = new UnitofmsrResource();
        unitofmsrResource.setInitialValues();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("unitofmsrResource", unitofmsrResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(UnitofmsrAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = UnitofmsrUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editUnitofmsr (
            @ModelAttribute("unitofmsrResourceListContainer")
            UnitofmsrResourceListContainer unitofmsrResourceListContainer) {

        List<UnitofmsrResource> unitofmsrResourceList =
                unitofmsrResourceListContainer.getUnitofmsrResourceList();
        List<UnitofmsrResource> selectedUnitofmsrResourceList;
        List<Unitofmsr> selectedUnitofmsrList = null;
        if (unitofmsrResourceList != null) {
            selectedUnitofmsrResourceList = unitofmsrResourceList.stream()
                    .filter(UnitofmsrResource::getSelected).collect(Collectors.toList());
            selectedUnitofmsrList = (List<Unitofmsr>)this.conversionService
                    .convert(selectedUnitofmsrResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(UnitofmsrResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Unitofmsr.class)));
        }

        try {
            if (selectedUnitofmsrList == null || selectedUnitofmsrList.isEmpty()) {
                Message message = this.messageFactory.getMessage(UnitofmsrNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedUnitofmsrList.size() > 1) {
                Message message = this.messageFactory.getMessage(UnitofmsrNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Unitofmsr selectedUnitofmsr = selectedUnitofmsrList.get(0);
            Unitofmsr unitofmsr = this.unitofmsrService.getUnitofmsr(selectedUnitofmsr.getCode());
            if(unitofmsr == null) {
                Message message = this.messageFactory.getMessage(UnitofmsrNotExistMessageCode);
                throw new ServiceException(message);
            }
            UnitofmsrResource unitofmsrResource =
                    this.unitofmsrResourceAssembler.toResource(unitofmsr);

            Message message = this.messageFactory.getMessage(UnitofmsrReadMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("unitofmsrResource", unitofmsrResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(UnitofmsrEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
            unitofmsrResourceList =
                    this.unitofmsrResourceAssembler.toResources(unitofmsrList);
            unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(
                    "unitofmsrResourceListContainer", unitofmsrResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(UnitofmsrListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = UnitofmsrDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteUnitofmsr (
            @ModelAttribute("unitofmsrResourceListContainer")
            UnitofmsrResourceListContainer unitofmsrResourceListContainer) {

        Message message;
        List<UnitofmsrResource> unitofmsrResourceList =
                unitofmsrResourceListContainer.getUnitofmsrResourceList();
        List<UnitofmsrResource> selectedUnitofmsrResourceList;
        List<Unitofmsr> selectedUnitofmsrList = null;
        if (unitofmsrResourceList != null) {
            selectedUnitofmsrResourceList = unitofmsrResourceList.stream()
                    .filter(UnitofmsrResource::getSelected).collect(Collectors.toList());
            selectedUnitofmsrList = (List<Unitofmsr>) this.conversionService
                    .convert(selectedUnitofmsrResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(UnitofmsrResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Unitofmsr.class)));
        }

        try {
            if (selectedUnitofmsrList == null || selectedUnitofmsrList.isEmpty()) {
                message = this.messageFactory.getMessage(UnitofmsrNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedUnitofmsrList.size() > 1) {
                message = this.messageFactory.getMessage(UnitofmsrNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Unitofmsr selectedUnitofmsr = selectedUnitofmsrList.get(0);
            this.unitofmsrService.deleteUnitofmsr(selectedUnitofmsr.getCode());
            message = this.messageFactory.getMessage(UnitofmsrDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }
        List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
        unitofmsrResourceList = this.unitofmsrResourceAssembler.toResources(unitofmsrList);
        unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(
                "unitofmsrResourceListContainer", unitofmsrResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(UnitofmsrListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = UnitofmsrInsertPermissionName)
    public ModelAndView insertUnitofmsr (
            @ModelAttribute("unitofmsrResource") UnitofmsrResource unitofmsrResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(unitofmsrResource, "unitofmsrResource");
            ValidationUtils.invokeValidator(this.unitofmsrResourceValidator,
                    unitofmsrResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Unitofmsr unitofmsr = this.conversionService
                    .convert(unitofmsrResource, Unitofmsr.class);
            this.unitofmsrService.insertUnitofmsr(unitofmsr);
            unitofmsrResource = new UnitofmsrResource();
            unitofmsrResource.setInitialValues();
            message = this.messageFactory.getMessage(UnitofmsrInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("unitofmsrResource", unitofmsrResource);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(UnitofmsrAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = UnitofmsrUpdatePermissionName)
    public ModelAndView updateUnitofmsr (
            @ModelAttribute("unitofmsrResource") UnitofmsrResource unitofmsrResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(unitofmsrResource, "unitofmsrResource");
            ValidationUtils.invokeValidator(this.unitofmsrResourceValidator,
                    unitofmsrResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Unitofmsr unitofmsr = this.conversionService
                    .convert(unitofmsrResource, Unitofmsr.class);
            this.unitofmsrService.updateUnitofmsr(unitofmsr);
            List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
            List<UnitofmsrResource> unitofmsrResourceList =
                    this.unitofmsrResourceAssembler.toResources(unitofmsrList);
            UnitofmsrResourceListContainer
                    unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
            unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

            Message message = this.messageFactory.getMessage(UnitofmsrUpdatedMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(
                    "unitofmsrResourceListContainer", unitofmsrResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(UnitofmsrListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("unitofmsrResource", unitofmsrResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(UnitofmsrEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = UnitofmsrReadPermissionName)
    public ModelAndView getUnitofmsrs() {

        List<Unitofmsr> unitofmsrList = this.unitofmsrService.getUnitofmsrList();
        List<UnitofmsrResource> unitofmsrResourceList =
                this.unitofmsrResourceAssembler.toResources(unitofmsrList);
        UnitofmsrResourceListContainer
                unitofmsrResourceListContainer = new UnitofmsrResourceListContainer();
        unitofmsrResourceListContainer.setUnitofmsrResourceList(unitofmsrResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("unitofmsrResourceListContainer", unitofmsrResourceListContainer);
        modelAndView.setViewName(UnitofmsrListWebViewerName);
        return modelAndView;
    }
}