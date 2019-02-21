package net.bsoftlab.controller.web;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;
import net.bsoftlab.model.Currency;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyService;
import net.bsoftlab.utility.UtilityFunctions;

import net.bsoftlab.resource.assembler.CurrencyResourceAssembler;
import net.bsoftlab.resource.container.CurrencyResourceListContainer;
import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.validator.CurrencyResourceValidator;

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
@RequestMapping(path = "currencies")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyWebController {

    private static final String CurrencyDeletedMessageCode = "currency.deleted";
    private static final String CurrencyInsertedMessageCode = "currency.inserted";
    private static final String CurrencyNotExistMessageCode = "currency.not.exist";
    private static final String CurrencyNotSelectedMessageCode = "currency.not.selected";
    private static final String CurrencyNotSingleSelectedMessageCode = "currency.not.single.selected";
    private static final String CurrencyReadMessageCode = "currency.read";
    private static final String CurrencyUpdatedMessageCode = "currency.updated";

    private static final String CurrencyDeletePermissionName = "permission.currency.delete";
    private static final String CurrencyInsertPermissionName = "permission.currency.insert";
    private static final String CurrencyReadPermissionName = "permission.currency.read";
    private static final String CurrencyUpdatePermissionName = "permission.currency.update";

    private static final String CurrencyAddWebViewerName = "currencyAddWebViewer";
    private static final String CurrencyEditWebViewerName = "currencyEditWebViewer";
    private static final String CurrencyListWebViewerName = "currencyListWebViewer";

    private CurrencyResourceAssembler currencyResourceAssembler;
    private CurrencyResourceValidator currencyResourceValidator;
    private CurrencyService currencyService;

    private ConversionService conversionService = null;
    private MessageFactory messageFactory = null;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public CurrencyWebController(
            CurrencyResourceAssembler currencyResourceAssembler,
            CurrencyResourceValidator currencyResourceValidator,
            CurrencyService currencyServiceImpl) {
        this.currencyResourceAssembler = currencyResourceAssembler;
        this.currencyResourceValidator = currencyResourceValidator;
        this.currencyService = currencyServiceImpl;
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
    @Secured(value = CurrencyInsertPermissionName)
    public ModelAndView addCurrency() {
        CurrencyResource currencyResource = new CurrencyResource();
        currencyResource.setInitialValues();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResource", currencyResource);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName(CurrencyAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "edit", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = CurrencyUpdatePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView editCurrency(
            @ModelAttribute("currencyResourceListContainer")
            CurrencyResourceListContainer currencyResourceListContainer) {

        List<CurrencyResource> currencyResourceList =
                currencyResourceListContainer.getCurrencyResourceList();
        List<CurrencyResource> selectedCurrencyResourceList;
        List<Currency> selectedCurrencyList = null;
        if (currencyResourceList != null) {
            selectedCurrencyResourceList = currencyResourceList.stream()
                    .filter(CurrencyResource::getSelected).collect(Collectors.toList());
            selectedCurrencyList = (List<Currency>) this.conversionService
                    .convert(selectedCurrencyResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Currency.class)));
        }

        try {
            if (selectedCurrencyList == null || selectedCurrencyList.isEmpty()) {
                Message message = this.messageFactory.getMessage(CurrencyNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedCurrencyList.size() > 1) {
                Message message = this.messageFactory.getMessage(CurrencyNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }

            Currency selectedCurrency = selectedCurrencyList.get(0);
            Currency currency = this.currencyService.getCurrency(selectedCurrency.getCode());
            if(currency == null) {
                Message message = this.messageFactory.getMessage(CurrencyNotExistMessageCode);
                throw new ServiceException(message);
            }
            CurrencyResource currencyResource =
                    this.currencyResourceAssembler.toResource(currency);

            Message message = this.messageFactory.getMessage(CurrencyReadMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResource", currencyResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyEditWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            List<Currency> currencyList = this.currencyService.getCurrencyList();
            currencyResourceList = this.currencyResourceAssembler.toResources(currencyList);
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyListWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyDeletePermissionName)
    @SuppressWarnings("unchecked")
    public ModelAndView deleteCurrency(
            @ModelAttribute("currencyResourceListContainer")
            CurrencyResourceListContainer currencyResourceListContainer) {

        Message message;
        List<CurrencyResource> currencyResourceList =
                currencyResourceListContainer.getCurrencyResourceList();
        List<CurrencyResource> selectedCurrencyResourceList;
        List<Currency> selectedCurrencyList = null;
        if (currencyResourceList != null) {
            selectedCurrencyResourceList = currencyResourceList.stream()
                    .filter(CurrencyResource::getSelected).collect(Collectors.toList());
            selectedCurrencyList = (List<Currency>) this.conversionService
                    .convert(selectedCurrencyResourceList,
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(CurrencyResource.class)),
                            TypeDescriptor.collection(List.class,
                                    TypeDescriptor.valueOf(Currency.class)));
        }

        try {
            if (selectedCurrencyList == null || selectedCurrencyList.isEmpty()) {
                message = this.messageFactory.getMessage(CurrencyNotSelectedMessageCode);
                throw new ServiceException(message);
            } else if (selectedCurrencyList.size() > 1) {
                message = this.messageFactory.getMessage(CurrencyNotSingleSelectedMessageCode);
                throw new ServiceException(message);
            }
            Currency selectedCurrency = selectedCurrencyList.get(0);
            this.currencyService.deleteCurrency(selectedCurrency.getCode());
            message = this.messageFactory.getMessage(CurrencyDeletedMessageCode);

        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        currencyResourceList = this.currencyResourceAssembler.toResources(currencyList);
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.addObject("message", message);
        modelAndView.setViewName(CurrencyListWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "insert", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyInsertPermissionName)
    public ModelAndView insertCurrency(
            @ModelAttribute("currencyResource") CurrencyResource currencyResource) {

        Message message;
        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(currencyResource, "currencyResource");
            ValidationUtils.invokeValidator(this.currencyResourceValidator,
                    currencyResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String codeMessage = objectErrorList.get(0).getCode();
                message = this.messageFactory.getMessage(codeMessage);
                throw new ServiceException(message);
            }
            Currency currency = this.conversionService
                    .convert(currencyResource, Currency.class);
            this.currencyService.insertCurrency(currency);
            currencyResource = new CurrencyResource();
            currencyResource.setInitialValues();
            message = this.messageFactory.getMessage(CurrencyInsertedMessageCode);
        } catch (ServiceException serviceException) {
            message = serviceException.getErrorMessage();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResource", currencyResource);
        modelAndView.addObject("message", message);
        modelAndView.setViewName(CurrencyAddWebViewerName);
        return modelAndView;
    }

    @RequestMapping(path = "update", method = {RequestMethod.POST, RequestMethod.GET})
    @Secured(value = CurrencyUpdatePermissionName)
    public ModelAndView updateCurrency(
            @ModelAttribute("currencyResource") CurrencyResource currencyResource) {

        try {
            BeanPropertyBindingResult beanPropertyBindingResult =
                    new BeanPropertyBindingResult(currencyResource, "currencyResource");
            ValidationUtils.invokeValidator(this.currencyResourceValidator,
                    currencyResource, beanPropertyBindingResult);
            List<ObjectError> objectErrorList = beanPropertyBindingResult.getAllErrors();
            if(objectErrorList != null && objectErrorList.size() > 0) {
                String errorID = objectErrorList.get(0).getCode();
                Message message = this.messageFactory.getMessage(errorID);
                throw new ServiceException(message);
            }

            Currency currency = this.conversionService
                    .convert(currencyResource, Currency.class);
            this.currencyService.updateCurrency(currency);
            List<Currency> currencyList = this.currencyService.getCurrencyList();
            List<CurrencyResource> currencyResourceList =
                    this.currencyResourceAssembler.toResources(currencyList);
            CurrencyResourceListContainer
                    currencyResourceListContainer = new CurrencyResourceListContainer();
            currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

            Message message = this.messageFactory.getMessage(CurrencyUpdatedMessageCode);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyListWebViewerName);
            return modelAndView;

        } catch (ServiceException serviceException) {
            Message message = serviceException.getErrorMessage();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("currencyResource", currencyResource);
            modelAndView.addObject("message", message);
            modelAndView.setStatus(HttpStatus.valueOf(message.getHttpStatus()));
            modelAndView.setViewName(CurrencyEditWebViewerName);
            return modelAndView;
        }
    }

    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    @Secured(value = CurrencyReadPermissionName)
    public ModelAndView getCurrencies() {

        List<Currency> currencyList = this.currencyService.getCurrencyList();
        List<CurrencyResource> currencyResourceList =
                this.currencyResourceAssembler.toResources(currencyList);
        CurrencyResourceListContainer currencyResourceListContainer =
                new CurrencyResourceListContainer();
        currencyResourceListContainer.setCurrencyResourceList(currencyResourceList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currencyResourceListContainer", currencyResourceListContainer);
        modelAndView.setViewName(CurrencyListWebViewerName);
        return modelAndView;
    }
}