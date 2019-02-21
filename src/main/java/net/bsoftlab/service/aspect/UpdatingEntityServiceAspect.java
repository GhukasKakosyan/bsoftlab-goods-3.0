package net.bsoftlab.service.aspect;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;

import net.bsoftlab.service.exception.ServiceException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Arrays;

@Aspect
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UpdatingEntityServiceAspect {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());

    private static final String CurrencyRateUpdatedMessageCode = "currencyRate.updated";
    private static final String CurrencyUpdatedMessageCode = "currency.updated";
    private static final String DepartmentUpdatedMessageCode = "department.updated";
    private static final String GroupUpdatedMessageCode = "group.updated";
    private static final String MatvalueUpdatedMessageCode = "matvalue.updated";
    private static final String SalePriceUpdatedMessageCode = "salePrice.updated";
    private static final String UnitofmsrUpdatedMessageCode = "unitofmsr.updated";
    private static final String WorkmanUpdatedMessageCode = "workman.updated";

    private MessageFactory messageFactory = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    public void setMessageFactory(
            MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    private boolean updateEntityServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] arguments = proceedingJoinPoint.getArgs();
        Logger.info(proceedingJoinPoint.getStaticPart().toString());
        Logger.info(proceedingJoinPoint.getTarget().toString());
        try {
            if (arguments != null && arguments.length > 0) {
                Logger.info("Arguments: " + Arrays.toString(arguments));
                proceedingJoinPoint.proceed(arguments);
            } else {
                proceedingJoinPoint.proceed();
            }
        } catch (ServiceException serviceException) {
            String messageCode = serviceException.getMessage();
            Message message = this.messageFactory.getMessage(messageCode);
            Logger.info(message.toString());
            throw new ServiceException(message);
        }
        return true;
    }

    @Around("updateCurrencyServicePointcut()")
    public boolean updateCurrencyServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateCurrencyRateServicePointcut()")
    public boolean updateCurrencyRateServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyRateUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateDepartmentServicePointcut()")
    public boolean updateDepartmentServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(DepartmentUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateGroupServicePointcut()")
    public boolean updateGroupServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(GroupUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateMatvalueServicePointcut()")
    public boolean updateMatvalueServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(MatvalueUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateSalePriceServicePointcut()")
    public boolean updateSalePriceServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(SalePriceUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateUnitofmsrServicePointcut()")
    public boolean updateUnitofmsrServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(UnitofmsrUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Around("updateWorkmanServicePointcut()")
    public boolean updateWorkmanServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isUpdated = this.updateEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(WorkmanUpdatedMessageCode);
        Logger.info(message.toString());
        return isUpdated;
    }

    @Pointcut("execution(public void net.bsoftlab.service.CurrencyService" +
            ".updateCurrency(net.bsoftlab.model.Currency))")
    public void updateCurrencyServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.CurrencyRateService" +
            ".updateCurrencyRate(net.bsoftlab.model.CurrencyRate))")
    public void updateCurrencyRateServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.DepartmentService" +
            ".updateDepartment(net.bsoftlab.model.Department))")
    public void updateDepartmentServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.GroupService" +
            ".updateGroup(net.bsoftlab.model.Group))")
    public void updateGroupServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.MatvalueService" +
            ".updateMatvalue(net.bsoftlab.model.Matvalue))")
    public void updateMatvalueServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.SalePriceService" +
            ".updateSalePrice(net.bsoftlab.model.SalePrice))")
    public void updateSalePriceServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.UnitofmsrService" +
            ".updateUnitofmsr(net.bsoftlab.model.Unitofmsr))")
    public void updateUnitofmsrServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.WorkmanService" +
            ".updateWorkman(net.bsoftlab.model.Workman))")
    public void updateWorkmanServicePointcut(){}
}