package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;

import net.bsoftlab.resource.assembler.CurrencyResourceAssembler;
import net.bsoftlab.resource.assembler.DepartmentResourceAssembler;
import net.bsoftlab.resource.assembler.MatvalueResourceAssembler;
import net.bsoftlab.resource.assembler.SalePriceResourceAssembler;

import net.bsoftlab.resource.container.CurrencyResourceListContainer;
import net.bsoftlab.resource.container.DepartmentResourceListContainer;
import net.bsoftlab.resource.container.MatvalueResourceListContainer;
import net.bsoftlab.resource.container.SalePriceResourceListContainer;

import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.SalePriceResource;
import net.bsoftlab.resource.validator.SalePriceResourceValidator;

import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyService;
import net.bsoftlab.service.DepartmentService;
import net.bsoftlab.service.MatvalueService;
import net.bsoftlab.service.SalePriceService;

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
@RequestMapping(path = "saleprices")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SalePriceWebController {

    private static final String SalePriceDeletedMessageCode = "salePrice.deleted";
    private static final String SalePriceInsertedMessageCode = "salePrice.inserted";
    private static final String SalePriceNotExistMessageCode = "salePrice.not.exist";
    private static final String SalePriceNotSelectedMessageCode = "salePrice.not.selected";
    private static final String SalePriceNotSingleSelectedMessageCode = "salePrice.not.single.selected";
    private static final String SalePriceReadMessageCode = "salePrice.read";
    private static final String SalePriceUpdatedMessageCode = "salePrice.updated";

    private static final String SalePriceDeletePermissionName = "permission.salePrice.delete";
    private static final String SalePriceInsertPermissionName = "permission.salePrice.insert";
    private static final String SalePriceReadPermissionName = "permission.salePrice.read";
    private static final String SalePriceUpdatePermissionName = "permission.salePrice.update";

    private static final String SalePriceAddWebViewerName = "salePriceAddWebViewer";
    private static final String SalePriceEditWebViewerName = "salePriceEditWebViewer";
    private static final String SalePriceListWebViewerName = "salePriceListWebViewer";

    private SalePriceResourceAssembler salePriceResourceAssembler;
    private SalePriceResourceValidator salePriceResourceValidator;
    private SalePriceService salePriceService;

    private CurrencyResourceAssembler currencyResourceAssembler = null;
    private CurrencyService currencyService = null;

    private DepartmentResourceAssembler departmentResourceAssembler = null;
    private DepartmentService departmentService = null;

    private MatvalueResourceAssembler matvalueResourceAssembler = null;
    private MatvalueService matvalueService = null;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public SalePriceWebController(
            SalePriceResourceAssembler salePriceResourceAssembler,
            SalePriceResourceValidator salePriceResourceValidator,
            SalePriceService salePriceServiceImpl) {

        this.salePriceResourceAssembler = salePriceResourceAssembler;
        this.salePriceResourceValidator = salePriceResourceValidator;
        this.salePriceService = salePriceServiceImpl;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    public void setCurrencyResourceAssembler(
            CurrencyResourceAssembler currencyResourceAssembler) {
        this.currencyResourceAssembler = currencyResourceAssembler;
    }
    @Autowired
    public void setCurrencyService(
            CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setDepartmentResourceAssembler(
            DepartmentResourceAssembler departmentResourceAssembler) {
        this.departmentResourceAssembler = departmentResourceAssembler;
    }
    @Autowired
    public void setDepartmentService(
            DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setMatvalueResourceAssembler(
            MatvalueResourceAssembler matvalueResourceAssembler) {
        this.matvalueResourceAssembler = matvalueResourceAssembler;
    }
    @Autowired
    public void setMatvalueService(
            MatvalueService matvalueService) {
        this.matvalueService = matvalueService;
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
        String error = Functions.getPrintStackTrace(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = SalePriceInsertPermissionName)
    public ModelAndView addSalePrice() {

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        CurrencyResourceListContainer
                currencyResourceListContainer = new CurrencyResourceListContainer();
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

        List<Department> departmentList = this.departmentService.getDepartmentList();
        List<DepartmentResource> departmentResourceList =
                this.departmentResourceAssembler.toResources(departmentList);
        DepartmentResourceListContainer
                departmentResourceListContainer = new DepartmentResourceListContainer();
        departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

        List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
        List<MatvalueResource> matvalueResourceList =
                this.matvalueResourceAssembler.toResources(matvalueList);
        MatvalueResourceListContainer
                matvalueResourceListContainer = new MatvalueResourceListContainer();
        matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

        SalePriceResource salePriceResource = new SalePriceResource();
        salePriceResource.setInitialValues();
        if (currencyResourceList != null && !currencyResourceList.isEmpty()) {
            salePriceResource.setCurrencyResource(currencyResourceList.get(0));
        }
        if (departmentResourceList != null && !departmentResourceList.isEmpty()) {
            salePriceResource.setDepartmentResource(departmentResourceList.get(0));
        }
        if (matvalueResourceList != null && !matvalueResourceList.isEmpty()) {
            salePriceResource.setMatvalueResource(matvalueResourceList.get(0));
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.addObject("departmentResourceListContainer", departmentResourceListContainer);
        modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
        modelAndView.addObject("salePriceResource", salePriceResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(SalePriceAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = SalePriceUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editSalePrice (
            @ModelAttribute("salePriceResourceListContainer")
            SalePriceResourceListContainer salePriceResourceListContainer) {

        List<SalePriceResource> salePriceResourceList =
                salePriceResourceListContainer.getSalePriceResourceList();
        List<SalePriceResource> selectedSalePriceResourceList;
        List<SalePrice> selectedSalePriceList = null;
        if (salePriceResourceList != null) {
            selectedSalePriceResourceList = salePriceResourceList.stream()
                    .filter(SalePriceResource::getSelected).collect(Collectors.toList());
            selectedSalePriceList = (List<SalePrice>) this.conversionService
                    .convert(selectedSalePriceResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(SalePriceResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(SalePrice.class)));
        }

        try {
            if (selectedSalePriceList == null || selectedSalePriceList.isEmpty()) {
                Message message = this.messageFactory.getMessage(SalePriceNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedSalePriceList.size() > 1) {
                Message message = this.messageFactory.getMessage(SalePriceNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            SalePrice selectedSalePrice = selectedSalePriceList.get(0);
            SalePrice salePrice = this.salePriceService.getSalePrice(selectedSalePrice.getID());
            if(salePrice == null) {
                Message message = this.messageFactory.getMessage(SalePriceNotExistMessageCode);
                throw new ServiceException(message);
            }
            SalePriceResource salePriceResource =
                    this.salePriceResourceAssembler.toResource(salePrice);

            List<Currency> currencyList = this.currencyService.getCurrencyList();
            List<CurrencyResource> currencyResourceList =
                    this.currencyResourceAssembler.toResources(currencyList);
            CurrencyResourceListContainer
                    currencyResourceListContainer = new CurrencyResourceListContainer();
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            List<Department> departmentList = this.departmentService.getDepartmentList();
            List<DepartmentResource> departmentResourceList =
                    this.departmentResourceAssembler.toResources(departmentList);
            DepartmentResourceListContainer
                    departmentResourceListContainer = new DepartmentResourceListContainer();
            departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

            List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
            List<MatvalueResource> matvalueResourceList =
                    this.matvalueResourceAssembler.toResources(matvalueList);
            MatvalueResourceListContainer
                    matvalueResourceListContainer = new MatvalueResourceListContainer();
            matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

            Message message = this.messageFactory.getMessage(SalePriceReadMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("departmentResourceListContainer", departmentResourceListContainer);
            modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.addObject("salePriceResource", salePriceResource);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(SalePriceEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<SalePrice> salePriceList =
                    this.salePriceService.getSalePriceList();
            salePriceResourceList =
                    this.salePriceResourceAssembler.toResources(salePriceList);
            salePriceResourceListContainer.setSalePriceResourceList(salePriceResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", message);
            modelAndView.addObject("salePriceResourceListContainer", salePriceResourceListContainer);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(SalePriceListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = SalePriceDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteSalePrice (
            @ModelAttribute("salePriceResourceListContainer")
            SalePriceResourceListContainer salePriceResourceListContainer) {

        Message message;
        List<SalePriceResource> salePriceResourceList =
                salePriceResourceListContainer.getSalePriceResourceList();
        List<SalePriceResource> selectedSalePriceResourceList;
        List<SalePrice> selectedSalePriceList = null;
        if (salePriceResourceList != null) {
            selectedSalePriceResourceList = salePriceResourceList.stream()
                    .filter(SalePriceResource::getSelected).collect(Collectors.toList());
            selectedSalePriceList = (List<SalePrice>) this.conversionService
                    .convert(selectedSalePriceResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(SalePriceResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Department.class)));
        }

        try {
            if (selectedSalePriceList == null || selectedSalePriceList.isEmpty()) {
                message = this.messageFactory.getMessage(SalePriceNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedSalePriceList.size() > 1) {
                message = this.messageFactory.getMessage(SalePriceNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            SalePrice selectedSalePrice = selectedSalePriceList.get(0);
            this.salePriceService.deleteSalePrice(selectedSalePrice.getID());
            message = this.messageFactory.getMessage(SalePriceDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<SalePrice> salePriceList =
                this.salePriceService.getSalePriceList();
        salePriceResourceList =
                this.salePriceResourceAssembler.toResources(salePriceList);
        salePriceResourceListContainer.setSalePriceResourceList(salePriceResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", message);
        modelAndView.addObject("salePriceResourceListContainer", salePriceResourceListContainer);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(SalePriceListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = SalePriceInsertPermissionName)
    public ModelAndView insertSalePrice (
            @ModelAttribute("salePriceResource") SalePriceResource salePriceResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(salePriceResource, "salePriceResource");
            ValidationUtils.invokeValidator(this.salePriceResourceValidator,
                    salePriceResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            SalePrice salePrice = this.conversionService
                    .convert(salePriceResource, SalePrice.class);
            this.salePriceService.insertSalePrice(salePrice);
            salePriceResource = new SalePriceResource();
            salePriceResource.setInitialValues();
            message = this.messageFactory.getMessage(SalePriceInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        CurrencyResourceListContainer currencyResourceListContainer =
                new CurrencyResourceListContainer();
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

        List<Department> departmentList = this.departmentService.getDepartmentList();
        List<DepartmentResource> departmentResourceList =
                this.departmentResourceAssembler.toResources(departmentList);
        DepartmentResourceListContainer
                departmentResourceListContainer = new DepartmentResourceListContainer();
        departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

        List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
        List<MatvalueResource> matvalueResourceList =
                this.matvalueResourceAssembler.toResources(matvalueList);
        MatvalueResourceListContainer
                matvalueResourceListContainer = new MatvalueResourceListContainer();
        matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.addObject("departmentResourceListContainer", departmentResourceListContainer);
        modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.addObject("salePriceResource", salePriceResource);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(SalePriceAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = SalePriceUpdatePermissionName)
    public ModelAndView updateSalePrice (
            @ModelAttribute("salePriceResource") SalePriceResource salePriceResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(salePriceResource, "salePriceResource");
            ValidationUtils.invokeValidator(this.salePriceResourceValidator,
                    salePriceResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }

            SalePrice salePrice = this.conversionService
                    .convert(salePriceResource, SalePrice.class);
            this.salePriceService.updateSalePrice(salePrice);
            List<SalePrice> salePriceList = this.salePriceService.getSalePriceList();
            List<SalePriceResource> salePriceResourceList =
                    this.salePriceResourceAssembler.toResources(salePriceList);
            SalePriceResourceListContainer salePriceResourceListContainer =
                    new SalePriceResourceListContainer();
            salePriceResourceListContainer
                    .setSalePriceResourceList(salePriceResourceList);

            Message message = this.messageFactory.getMessage(SalePriceUpdatedMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", message);
            modelAndView.addObject("salePriceResourceListContainer", salePriceResourceListContainer);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(SalePriceListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Currency> currencyList = this.currencyService.getCurrencyList();
            List<CurrencyResource> currencyResourceList =
                    this.currencyResourceAssembler.toResources(currencyList);
            CurrencyResourceListContainer
                    currencyResourceListContainer = new CurrencyResourceListContainer();
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            List<Department> departmentList = this.departmentService.getDepartmentList();
            List<DepartmentResource> departmentResourceList =
                    this.departmentResourceAssembler.toResources(departmentList);
            DepartmentResourceListContainer
                    departmentResourceListContainer = new DepartmentResourceListContainer();
            departmentResourceListContainer.setDepartmentResourceList(departmentResourceList);

            List<Matvalue> matvalueList = this.matvalueService.getMatvalueList();
            List<MatvalueResource> matvalueResourceList =
                    this.matvalueResourceAssembler.toResources(matvalueList);
            MatvalueResourceListContainer
                    matvalueResourceListContainer = new MatvalueResourceListContainer();
            matvalueResourceListContainer.setMatvalueResourceList(matvalueResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("departmentResourceListContainer", departmentResourceListContainer);
            modelAndView.addObject("matvalueResourceListContainer", matvalueResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.addObject("salePriceResource", salePriceResource);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(SalePriceEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = SalePriceReadPermissionName)
    public ModelAndView getSalePrices() {

        List<SalePrice> salePriceList =
                this.salePriceService.getSalePriceList();
        List<SalePriceResource> salePriceResourceList =
                this.salePriceResourceAssembler.toResources(salePriceList);
        SalePriceResourceListContainer
                salePriceResourceListContainer = new SalePriceResourceListContainer();
        salePriceResourceListContainer.setSalePriceResourceList(salePriceResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("salePriceResourceListContainer", salePriceResourceListContainer);
        modelAndView.setViewName(SalePriceListWebViewerName);
        return modelAndView;
    }
}