package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;

import net.bsoftlab.resource.assembler.CurrencyRateResourceAssembler;
import net.bsoftlab.resource.assembler.CurrencyResourceAssembler;
import net.bsoftlab.resource.container.CurrencyRateResourceListContainer;
import net.bsoftlab.resource.container.CurrencyResourceListContainer;
import net.bsoftlab.resource.CurrencyRateResource;
import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.validator.CurrencyRateResourceValidator;

import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyRateService;
import net.bsoftlab.service.CurrencyService;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "currenciesrates")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateWebController {

    private static final String CurrencyRateDeletedMessageCode = "currencyRate.deleted";
    private static final String CurrencyRateInsertedMessageCode = "currencyRate.inserted";
    private static final String CurrencyRateNotExistMessageCode = "currencyRate.not.exist";
    private static final String CurrencyRateNotSelectedMessageCode = "currencyRate.not.selected";
    private static final String CurrencyRateNotSingleSelectedMessageCode = "currencyRate.not.single.selected";
    private static final String CurrencyRateReadMessageCode = "currencyRate.read";
    private static final String CurrencyRateUpdatedMessageCode = "currencyRate.updated";

    private static final String CurrencyRateDeletePermissionName = "permission.currencyRate.delete";
    private static final String CurrencyRateInsertPermissionName = "permission.currencyRate.insert";
    private static final String CurrencyRateReadPermissionName = "permission.currencyRate.read";
    private static final String CurrencyRateUpdatePermissionName = "permission.currencyRate.update";

    private static final String CurrencyRateAddWebViewerName = "currencyRateAddWebViewer";
    private static final String CurrencyRateEditWebViewerName = "currencyRateEditWebViewer";
    private static final String CurrencyRateListWebViewerName = "currencyRateListWebViewer";

    private CurrencyRateResourceAssembler currencyRateResourceAssembler;
    private CurrencyRateResourceValidator currencyRateResourceValidator;
    private CurrencyRateService currencyRateService;

    private CurrencyResourceAssembler currencyResourceAssembler = null;
    private CurrencyService currencyService = null;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;

    @Autowired
    public CurrencyRateWebController(
            CurrencyRateResourceAssembler currencyRateResourceAssembler,
            CurrencyRateResourceValidator currencyRateResourceValidator,
            CurrencyRateService currencyRateServiceImpl) {
        this.currencyRateResourceAssembler = currencyRateResourceAssembler;
        this.currencyRateResourceValidator = currencyRateResourceValidator;
        this.currencyRateService = currencyRateServiceImpl;
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
        Function <Throwable, String> stackTraceToStringFunction = throwableParameter -> {
            StringWriter stringWriter = new StringWriter();
            throwableParameter.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.toString();
        };
        String error = stackTraceToStringFunction.apply(throwable);
        Message message = this.messageFactory.getInternalServerErrorMessage(error);
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(message, httpHeaders,
                HttpStatus.valueOf(message.getHttpStatus()));
    }

    @RequestMapping(path = "add", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = CurrencyRateInsertPermissionName)
    public ModelAndView addCurrencyRate() {

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        CurrencyResourceListContainer
                currencyResourceListContainer = new CurrencyResourceListContainer();
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);
        CurrencyResource currencyResource = null;
        if (currencyResourceList != null && !currencyResourceList.isEmpty()) {
            currencyResource = currencyResourceList.get(0);
        }
        CurrencyRateResource currencyRateResource = new CurrencyRateResource();
        currencyRateResource.setInitialValues();
        currencyRateResource.setCurrencyResource(currencyResource);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(
                "currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.addObject("currencyRateResource", currencyRateResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(CurrencyRateAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = CurrencyRateUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editCurrencyRate (
            @ModelAttribute("currencyRateResourceListContainer")
            CurrencyRateResourceListContainer currencyRateResourceListContainer) {

        List<CurrencyRateResource> currencyRateResourceList =
                currencyRateResourceListContainer.getCurrencyRateResourceList();
        List<CurrencyRateResource> selectedCurrencyRateResourceList;
        List<CurrencyRate> selectedCurrencyRateList = null;
        if (currencyRateResourceList != null) {
            selectedCurrencyRateResourceList = currencyRateResourceList.stream()
                    .filter(CurrencyRateResource::getSelected).collect(Collectors.toList());
            selectedCurrencyRateList = (List<CurrencyRate>) this.conversionService
                    .convert(selectedCurrencyRateResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyRateResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyRate.class)));
        }
        try {
            if (selectedCurrencyRateList == null || selectedCurrencyRateList.isEmpty()) {
                Message message = this.messageFactory.getMessage(CurrencyRateNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedCurrencyRateList.size() > 1) {
                Message message = this.messageFactory.getMessage(CurrencyRateNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            CurrencyRate selectedCurrencyRate = selectedCurrencyRateList.get(0);
            CurrencyRate currencyRate =
                    this.currencyRateService.getCurrencyRate(selectedCurrencyRate.getID());
            if(currencyRate == null) {
                Message message = this.messageFactory.getMessage(CurrencyRateNotExistMessageCode);
                throw new ServiceException(message);
            }
            CurrencyRateResource currencyRateResource =
                    this.currencyRateResourceAssembler.toResource(currencyRate);

            List<Currency> currencyList = this.currencyService.getCurrencyList();
            List<CurrencyResource> currencyResourceList =
                    this.currencyResourceAssembler.toResources(currencyList);
            CurrencyResourceListContainer
                    currencyResourceListContainer = new CurrencyResourceListContainer();
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            Message message = this.messageFactory.getMessage(CurrencyRateReadMessageCode);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyRateResource", currencyRateResource);
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyRateEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<CurrencyRate> currencyRateList =
                    this.currencyRateService.getCurrencyRateList();
            currencyRateResourceList =
                    this.currencyRateResourceAssembler.toResources(currencyRateList);
            currencyRateResourceListContainer.setCurrencyRateResourceList(currencyRateResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(
                    "currencyRateResourceListContainer", currencyRateResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyRateListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyRateDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteCurrencyRate (
            @ModelAttribute("currencyRateResourceListContainer")
            CurrencyRateResourceListContainer currencyRateResourceListContainer) {

        Message message;
        List<CurrencyRateResource> currencyRateResourceList =
                currencyRateResourceListContainer.getCurrencyRateResourceList();
        List<CurrencyRateResource> selectedCurrencyRateResourceList;
        List<CurrencyRate> selectedCurrencyRateList = null;
        if (currencyRateResourceList != null) {
            selectedCurrencyRateResourceList = currencyRateResourceList.stream()
                    .filter(CurrencyRateResource::getSelected).collect(Collectors.toList());
            selectedCurrencyRateList = (List<CurrencyRate>) this.conversionService
                    .convert(selectedCurrencyRateResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyRateResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyRate.class)));
        }

        try {
            if (selectedCurrencyRateList == null || selectedCurrencyRateList.isEmpty()) {
                message = this.messageFactory.getMessage(CurrencyRateNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedCurrencyRateList.size() > 1) {
                message = this.messageFactory.getMessage(CurrencyRateNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            CurrencyRate selectedCurrencyRate = selectedCurrencyRateList.get(0);
            this.currencyRateService.deleteCurrencyRate(selectedCurrencyRate.getID());
            message = this.messageFactory.getMessage(CurrencyRateDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<CurrencyRate> currencyRateList =
                this.currencyRateService.getCurrencyRateList();
        currencyRateResourceList =
                this.currencyRateResourceAssembler.toResources(currencyRateList);
        currencyRateResourceListContainer.setCurrencyRateResourceList(currencyRateResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyRateResourceListContainer",
                currencyRateResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(CurrencyRateListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyRateInsertPermissionName)
    public ModelAndView insertCurrencyRate (
            @ModelAttribute("currencyRateResource")
            CurrencyRateResource currencyRateResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(currencyRateResource, "currencyRateResource");
            ValidationUtils.invokeValidator(this.currencyRateResourceValidator,
                    currencyRateResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            CurrencyRate currencyRate = this.conversionService
                    .convert(currencyRateResource, CurrencyRate.class);
            this.currencyRateService.insertCurrencyRate(currencyRate);
            currencyRateResource = new CurrencyRateResource();
            currencyRateResource.setInitialValues();
            message = this.messageFactory.getMessage(CurrencyRateInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        CurrencyResourceListContainer currencyResourceListContainer =
                new CurrencyResourceListContainer();
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyRateResource", currencyRateResource);
        modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
        modelAndView.setViewName(CurrencyRateAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyRateUpdatePermissionName)
    public ModelAndView updateCurrencyRate (
            @ModelAttribute("currencyRateResource")
            CurrencyRateResource currencyRateResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(currencyRateResource, "currencyRateResource");
            ValidationUtils.invokeValidator(this.currencyRateResourceValidator,
                    currencyRateResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && !objectErrorList.isEmpty()) {
                String codeMessage = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }

            CurrencyRate currencyRate = this.conversionService
                    .convert(currencyRateResource, CurrencyRate.class);
            this.currencyRateService.updateCurrencyRate(currencyRate);
            List<CurrencyRate> currencyRateList = this.currencyRateService.getCurrencyRateList();
            List<CurrencyRateResource> currencyRateResourceList =
                    this.currencyRateResourceAssembler.toResources(currencyRateList);
            CurrencyRateResourceListContainer currencyRateResourceListContainer =
                    new CurrencyRateResourceListContainer();
            currencyRateResourceListContainer
                    .setCurrencyRateResourceList(currencyRateResourceList);

            Message message = this.messageFactory.getMessage(CurrencyRateUpdatedMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyRateResourceListContainer", currencyRateResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyRateListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Currency> currencyList = this.currencyService.getCurrencyList();
            List<CurrencyResource> currencyResourceList =
                    this.currencyResourceAssembler.toResources(currencyList);
            CurrencyResourceListContainer
                    currencyResourceListContainer = new CurrencyResourceListContainer();
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyRateResource", currencyRateResource);
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyRateEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = CurrencyRateReadPermissionName)
    public ModelAndView getCurrencyRates() {

        List<CurrencyRate> currencyRateList =
                this.currencyRateService.getCurrencyRateList();
        List<CurrencyRateResource> currencyRateResourceList =
                this.currencyRateResourceAssembler.toResources(currencyRateList);
        CurrencyRateResourceListContainer
                currencyRateResourceListContainer = new CurrencyRateResourceListContainer();
        currencyRateResourceListContainer.setCurrencyRateResourceList(currencyRateResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyRateResourceListContainer",
                currencyRateResourceListContainer);
        modelAndView.setViewName(CurrencyRateListWebViewerName);
        return modelAndView;
    }
}