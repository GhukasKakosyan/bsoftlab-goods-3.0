package net.bsoftlab.service.aspect;

import net.bsoftlab.message.Message;
import net.bsoftlab.message.MessageFactory;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Permission;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.model.Workman;

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
public class GettingEntityServiceAspect {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());

    private static final String CurrencyRateReadMessageCode = "currencyRate.read";
    private static final String CurrencyReadMessageCode = "currency.read";
    private static final String DepartmentReadMessageCode = "department.read";
    private static final String GroupReadMessageCode = "group.read";
    private static final String MatvalueReadMessageCode = "matvalue.read";
    private static final String PermissionReadMessageCode = "permission.read";
    private static final String RoleReadMessageCode = "role.read";
    private static final String SalePriceReadMessageCode = "salePrice.read";
    private static final String UnitofmsrReadMessageCode = "unitofmsr.read";
    private static final String WorkmanReadMessageCode = "workman.read";

    private MessageFactory messageFactory = null;

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    public void setMessageFactory(
            MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    private Object getEntityServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] arguments = proceedingJoinPoint.getArgs();
        Logger.info(proceedingJoinPoint.getStaticPart().toString());
        Logger.info(proceedingJoinPoint.getTarget().toString());
        try {
            if (arguments != null && arguments.length > 0) {
                Logger.info("Arguments: " + Arrays.toString(arguments));
                return proceedingJoinPoint.proceed(arguments);
            } else {
                return proceedingJoinPoint.proceed();
            }
        } catch (ServiceException serviceException) {
            String messageCode = serviceException.getMessage();
            Message message = this.messageFactory.getMessage(messageCode);
            Logger.info(message.toString());
            throw new ServiceException(message);
        }
    }

    @Around("getCurrencyServicePointcut()")
    public Currency getCurrencyServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Currency currency = (Currency) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyReadMessageCode);
        Logger.info(currency.toString());
        Logger.info(message.toString());
        return currency;
    }

    @Around("getCurrencyRateServicePointcut()")
    public CurrencyRate getCurrencyRateServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CurrencyRate currencyRate = (CurrencyRate)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyRateReadMessageCode);
        Logger.info(currencyRate.toString());
        Logger.info(message.toString());
        return currencyRate;
    }

    @Around("getDepartmentServicePointcut()")
    public Department getDepartmentServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Department department = (Department) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(DepartmentReadMessageCode);
        Logger.info(department.toString());
        Logger.info(message.toString());
        return department;
    }

    @Around("getGroupServicePointcut()")
    public Group getGroupServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Group group = (Group) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(GroupReadMessageCode);
        Logger.info(group.toString());
        Logger.info(message.toString());
        return group;
    }

    @Around("getMatvalueServicePointcut()")
    public Matvalue getMatvalueServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Matvalue matvalue = (Matvalue) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(MatvalueReadMessageCode);
        Logger.info(matvalue.toString());
        Logger.info(message.toString());
        return matvalue;
    }

    @Around("getPermissionServicePointcut()")
    public Permission getPermissionServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Permission permission = (Permission) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(PermissionReadMessageCode);
        Logger.info(permission.toString());
        Logger.info(message.toString());
        return permission;
    }

    @Around("getRoleServicePointcut()")
    public Role getRoleServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Role role = (Role) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(RoleReadMessageCode);
        Logger.info(role.toString());
        Logger.info(message.toString());
        return role;
    }

    @Around("getSalePriceServicePointcut()")
    public SalePrice getSalePriceServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        SalePrice salePrice = (SalePrice) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(SalePriceReadMessageCode);
        Logger.info(salePrice.toString());
        Logger.info(message.toString());
        return salePrice;
    }

    @Around("getUnitofmsrServicePointcut()")
    public Unitofmsr getUnitofmsrServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Unitofmsr unitofmsr = (Unitofmsr) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(UnitofmsrReadMessageCode);
        Logger.info(unitofmsr.toString());
        Logger.info(message.toString());
        return unitofmsr;
    }

    @Around("getWorkmanServicePointcut()")
    public Workman getWorkmanServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Workman workman = (Workman) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(WorkmanReadMessageCode);
        Logger.info(workman.toString());
        Logger.info(message.toString());
        return workman;
    }

    @Pointcut("execution(public net.bsoftlab.model.Currency " +
            "net.bsoftlab.service.CurrencyService.getCurrency(String))")
    public void getCurrencyServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.CurrencyRate " +
            "net.bsoftlab.service.CurrencyRateService.getCurrencyRate(..))")
    public void getCurrencyRateServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Department " +
            "net.bsoftlab.service.DepartmentService.getDepartment(String))")
    public void getDepartmentServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Group " +
            "net.bsoftlab.service.GroupService.getGroup(String))")
    public void getGroupServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Matvalue " +
            "net.bsoftlab.service.MatvalueService.getMatvalue(String))")
    public void getMatvalueServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Permission " +
            "net.bsoftlab.service.PermissionService.getPermission(..))")
    public void getPermissionServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Role " +
            "net.bsoftlab.service.RoleService.getRole(Integer))")
    public void getRoleServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.SalePrice " +
            "net.bsoftlab.service.SalePriceService.getSalePrice(..))")
    public void getSalePriceServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Unitofmsr " +
            "net.bsoftlab.service.UnitofmsrService.getUnitofmsr(String))")
    public void getUnitofmsrServicePointcut(){}
    @Pointcut("execution(public net.bsoftlab.model.Workman " +
            "net.bsoftlab.service.WorkmanService.getWorkman(..))")
    public void getWorkmanServicePointcut(){}
}