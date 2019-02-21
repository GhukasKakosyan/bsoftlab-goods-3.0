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
public class DeletingEntityServiceAspect {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());

    private static final String CurrencyDeletedMessageCode = "currency.deleted";
    private static final String CurrencyRateDeletedMessageCode = "currencyRate.deleted";
    private static final String DepartmentDeletedMessageCode = "department.deleted";
    private static final String GroupDeletedMessageCode = "group.deleted";
    private static final String MatvalueDeletedMessageCode = "matvalue.deleted";
    private static final String SalePriceDeletedMessageCode = "salePrice.deleted";
    private static final String UnitofmsrDeletedMessageCode = "unitofmsr.deleted";
    private static final String WorkmanDeletedMessageCode = "workman.deleted";

    private MessageFactory messageFactory = null;

    @Autowired
    public void setMessageFactory(
            MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    private boolean deleteEntityServiceAdvice (
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

    @Around("deleteCurrencyServicePointcut()")
    public boolean deleteCurrencyServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String currencyCode = (String) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete currency with code: " + currencyCode);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteCurrencyRateServicePointcut()")
    public boolean deleteCurrencyRateServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer ID = (Integer) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete currencyRate with ID: " + ID);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyRateDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteDepartmentServicePointcut()")
    public boolean deleteDepartmentServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String code = (String) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete department with code: " + code);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(DepartmentDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteGroupServicePointcut()")
    public boolean deleteGroupServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String code = (String) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete group with code: " + code);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(GroupDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteMatvalueServicePointcut()")
    public boolean deleteMatvalueServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String code = (String) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete matvalue with code: " + code);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(MatvalueDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteSalePriceServicePointcut()")
    public boolean deleteSalePriceServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer ID = (Integer) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete salePrice with ID: " + ID);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(SalePriceDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteUnitofmsrServicePointcut()")
    public boolean deleteUnitofmsrServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String code = (String) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete unitofmsr with code: " + code);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(UnitofmsrDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Around("deleteWorkmanServicePointcut()")
    public boolean deleteWorkmanServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer ID = (Integer) proceedingJoinPoint.getArgs()[0];
        Logger.info("Preparing to delete workman with ID: " + ID);
        boolean isDeleted = this.deleteEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(WorkmanDeletedMessageCode);
        Logger.info(message.toString());
        return isDeleted;
    }

    @Pointcut("execution(public void net.bsoftlab.service.CurrencyService.deleteCurrency(String))")
    public void deleteCurrencyServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.CurrencyRateService.deleteCurrencyRate(Integer))")
    public void deleteCurrencyRateServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.DepartmentService.deleteDepartment(String))")
    public void deleteDepartmentServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.GroupService.deleteGroup(String))")
    public void deleteGroupServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.MatvalueService.deleteMatvalue(String))")
    public void deleteMatvalueServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.SalePriceService.deleteSalePrice(Integer))")
    public void deleteSalePriceServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.UnitofmsrService.deleteUnitofmsr(String))")
    public void deleteUnitofmsrServicePointcut(){}
    @Pointcut("execution(public void net.bsoftlab.service.WorkmanService.deleteWorkman(Integer))")
    public void deleteWorkmanServicePointcut(){}
}