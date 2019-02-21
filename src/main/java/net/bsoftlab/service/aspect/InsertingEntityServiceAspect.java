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
public class InsertingEntityServiceAspect {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());

    private static final String CurrencyRateInsertedMessageCode = "currencyRate.inserted";
    private static final String CurrencyInsertedMessageCode = "currency.inserted";
    private static final String DepartmentInsertedMessageCode = "department.inserted";
    private static final String GroupInsertedMessageCode = "group.inserted";
    private static final String MatvalueInsertedMessageCode = "matvalue.inserted";
    private static final String SalePriceInsertedMessageCode = "salePrice.inserted";
    private static final String UnitofmsrInsertedMessageCode = "unitofmsr.inserted";
    private static final String WorkmanInsertedMessageCode = "workman.inserted";

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

    private boolean insertEntityServiceAdvice(
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

    @Around("insertCurrencyServicePointcut()")
    public boolean insertCurrencyServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertCurrencyRateServicePointcut()")
    public boolean insertCurrencyRateServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyRateInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertDepartmentServicePointcut()")
    public boolean insertDepartmentServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(DepartmentInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertGroupServicePointcut()")
    public boolean insertGroupServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(GroupInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertMatvalueServicePointcut()")
    public boolean insertMatvalueServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(MatvalueInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertSalePriceServicePointcut()")
    public boolean insertSalePriceServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(SalePriceInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertUnitofmsrServicePointcut()")
    public boolean insertUnitofmsrServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(UnitofmsrInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Around("insertWorkmanServicePointcut()")
    public boolean insertWorkmanServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean isInserted = this.insertEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(WorkmanInsertedMessageCode);
        Logger.info(message.toString());
        return isInserted;
    }

    @Pointcut("execution(public void net.bsoftlab.service.CurrencyService." +
            "insertCurrency(net.bsoftlab.model.Currency))")
    public void insertCurrencyServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.CurrencyRateService." +
            "insertCurrencyRate(net.bsoftlab.model.CurrencyRate))")
    public void insertCurrencyRateServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.DepartmentService." +
            "insertDepartment(net.bsoftlab.model.Department))")
    public void insertDepartmentServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.GroupService." +
            "insertGroup(net.bsoftlab.model.Group))")
    public void insertGroupServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.MatvalueService." +
            "insertMatvalue(net.bsoftlab.model.Matvalue))")
    public void insertMatvalueServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.SalePriceService." +
            "insertSalePrice(net.bsoftlab.model.SalePrice))")
    public void insertSalePriceServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.UnitofmsrService." +
            "insertUnitofmsr(net.bsoftlab.model.Unitofmsr))")
    public void insertUnitofmsrServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.WorkmanService." +
            "insertWorkman(net.bsoftlab.model.Workman))")
    public void insertWorkmanServicePointcut(){}
}