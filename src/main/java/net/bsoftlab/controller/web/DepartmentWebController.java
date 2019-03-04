package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Department;
import net.bsoftlab.resource.assembler.DepartmentResourceAssembler;
import net.bsoftlab.resource.container.DepartmentResourceListContainer;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.validator.DepartmentResourceValidator;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.DepartmentService;
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
@RequestMapping(path = "departments")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentWebController {

    private static final String DepartmentDeletedMessageCode = "department.deleted";
    private static final String DepartmentInsertedMessageCode = "department.inserted";
    private static final String DepartmentNotExistMessageCode = "department.not.exist";
    private static final String DepartmentNotSelectedMessageCode = "department.not.selected";
    private static final String DepartmentNotSingleSelectedMessageCode = "department.not.single.selected";
    private static final String DepartmentReadMessageCode = "department.read";
    private static final String DepartmentUpdatedMessageCode = "department.updated";

    private static final String DepartmentDeletePermissionName = "permission.department.delete";
    private static final String DepartmentInsertPermissionName = "permission.department.insert";
    private static final String DepartmentReadPermissionName = "permission.department.read";
    private static final String DepartmentUpdatePermissionName = "permission.department.update";

    private static final String DepartmentAddWebViewerName = "departmentAddWebViewer";
    private static final String DepartmentEditWebViewerName = "departmentEditWebViewer";
    private static final String DepartmentListWebViewerName = "departmentListWebViewer";

    private DepartmentResourceAssembler departmentResourceAssembler;
    private DepartmentResourceValidator departmentResourceValidator;
    private DepartmentService departmentService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public DepartmentWebController(
            DepartmentResourceAssembler departmentResourceAssembler,
            DepartmentResourceValidator departmentResourceValidator,
            DepartmentService departmentServiceImpl) {
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
        String error = Functions.getPrintStackTrace(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = DepartmentInsertPermissionName)
    public ModelAndView addDepartment() {
        DepartmentResource departmentResource = new DepartmentResource();
        departmentResource.setInitialValues();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentResource", departmentResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(DepartmentAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = DepartmentUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editDepartment(
            @ModelAttribute("departmentResourceListContainer")
            DepartmentResourceListContainer departmentResourceListContainer) {

        List<DepartmentResource> departmentResourceList =
                departmentResourceListContainer.getDepartmentResourceList();
        List<DepartmentResource> selectedDepartmentResourceList;
        List<Department> selectedDepartmentList = null;
        if (departmentResourceList != null) {
            selectedDepartmentResourceList = departmentResourceList.stream()
                    .filter(DepartmentResource::getSelected).collect(Collectors.toList());
            selectedDepartmentList = (List<Department>) this.conversionService
                    .convert(selectedDepartmentResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(DepartmentResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Department.class)));
        }
        try {
            if (selectedDepartmentList == null || selectedDepartmentList.isEmpty()) {
                Message message = this.messageFactory.getMessage(DepartmentNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedDepartmentList.size() > 1) {
                Message message = this.messageFactory.getMessage(DepartmentNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Department selectedDepartment = selectedDepartmentList.get(0);
            Department department =
                    this.departmentService.getDepartment(selectedDepartment.getCode());
            if(department == null) {
                Message message = this.messageFactory.getMessage(DepartmentNotExistMessageCode);
                throw new ServiceException(message);
            }
            DepartmentResource departmentResource =
                    this.departmentResourceAssembler.toResource(department);

            Message message = this.messageFactory.getMessage(DepartmentReadMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("departmentResource", departmentResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(DepartmentEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Department> departmentList = this.departmentService.getDepartmentList();
            departmentResourceList =
                    this.departmentResourceAssembler.toResources(departmentList);
            departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("departmentResourceListContainer",
                    departmentResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(DepartmentListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = DepartmentDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteDepartment(
            @ModelAttribute("departmentResourceListContainer")
            DepartmentResourceListContainer departmentResourceListContainer) {

        Message message;
        List<DepartmentResource> departmentResourceList =
                departmentResourceListContainer.getDepartmentResourceList();
        List<DepartmentResource> selectedDepartmentResourceList;
        List<Department> selectedDepartmentList = null;
        if (departmentResourceList != null) {
            selectedDepartmentResourceList = departmentResourceList.stream()
                    .filter(DepartmentResource::getSelected)
                    .collect(Collectors.toList());

            selectedDepartmentList = (List<Department>) this.conversionService
                    .convert(selectedDepartmentResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(DepartmentResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Department.class)));
        }

        try {
            if (selectedDepartmentList == null || selectedDepartmentList.isEmpty()) {
                message = this.messageFactory.getMessage(DepartmentNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedDepartmentList.size() > 1) {
                message = this.messageFactory.getMessage(DepartmentNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Department selectedDepartment = selectedDepartmentList.get(0);
            this.departmentService.deleteDepartment(selectedDepartment.getCode());
            message = this.messageFactory.getMessage(DepartmentDeletedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }
        List<Department> departmentList = this.departmentService.getDepartmentList();
        departmentResourceList = this.departmentResourceAssembler.toResources(departmentList);
        departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentResourceListContainer",
                departmentResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setViewName(DepartmentListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = DepartmentInsertPermissionName)
    public ModelAndView insertDepartment(
            @ModelAttribute("departmentResource") DepartmentResource departmentResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(departmentResource, "departmentResource");
            ValidationUtils.invokeValidator(this.departmentResourceValidator,
                    departmentResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Department department = this.conversionService
                    .convert(departmentResource, Department.class);
            this.departmentService.insertDepartment(department);
            departmentResource = new DepartmentResource();
            departmentResource.setInitialValues();
            message = this.messageFactory.getMessage(DepartmentInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentResource", departmentResource);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(DepartmentAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = DepartmentUpdatePermissionName)
    public ModelAndView updateDepartment(
            @ModelAttribute("departmentResource") DepartmentResource departmentResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(departmentResource, "departmentResource");
            ValidationUtils.invokeValidator(this.departmentResourceValidator,
                    departmentResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Department department = this.conversionService
                    .convert(departmentResource, Department.class);
            this.departmentService.updateDepartment(department);
            List<Department> departmentList = this.departmentService.getDepartmentList();
            List<DepartmentResource> departmentResourceList =
                    this.departmentResourceAssembler.toResources(departmentList);
            DepartmentResourceListContainer
                    departmentResourceListContainer = new DepartmentResourceListContainer();
            departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

            Message message = this.messageFactory.getMessage(DepartmentUpdatedMessageCode);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(
                    "departmentResourceListContainer", departmentResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(DepartmentListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("departmentResource", departmentResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(DepartmentEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = DepartmentReadPermissionName)
    public ModelAndView getDepartments() {

        List<Department> departmentList = this.departmentService.getDepartmentList();
        List<DepartmentResource> departmentResourceList =
                this.departmentResourceAssembler.toResources(departmentList);
        DepartmentResourceListContainer
                departmentResourceListContainer = new DepartmentResourceListContainer();
        departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentResourceListContainer",
                departmentResourceListContainer);
        modelAndView.setViewName(DepartmentListWebViewerName);
        return modelAndView;
    }
}