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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

@Aspect
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GettingEntityListServiceAspect {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());

    private static final String CurrencyRateListReadMessageCode = "currencyRateList.read";
    private static final String CurrencyListReadMessageCode = "currencyList.read";
    private static final String DepartmentListReadMessageCode = "departmentList.read";
    private static final String GroupListReadMessageCode = "groupList.read";
    private static final String MatvalueListReadMessageCode = "matvalueList.read";
    private static final String PermissionListReadMessageCode = "permissionList.read";
    private static final String RoleListReadMessageCode = "roleList.read";
    private static final String SalePriceListReadMessageCode = "salePriceList.read";
    private static final String UnitofmsrListReadMessageCode = "unitofmsrList.read";
    private static final String WorkmanListReadMessageCode = "workmanList.read";

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

    @Around("getCurrencyListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Currency> getCurrencyListServiceAdvice(
        ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Currency> currencyList = (List<Currency>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyListReadMessageCode);
        Logger.info(message.toString());

        Queue<Currency> currencyQueue = new PriorityQueue<>(currencyList.size());
        for (Currency currency : currencyList) {
            if (!currencyQueue.contains(currency))
                currencyQueue.offer(currency);
            Logger.info("Offered currency: " + currency);
            Logger.info("The size of currencyQueue is: " + currencyQueue.size());
        }
        Logger.info("Interface Queue implements FIFO ");
        Logger.info("Interface Queue is implemented in classes: LinkedList, PriorityQueue");
        Logger.info("The elements of the PriorityQueue are ordered to their natural ordering or");
        Logger.info("by Comparator provided at queue construction time, depending on which constructor is used");
        Logger.info("A PriorityQueue does not permit null elements.");
        Logger.info("A PriorityQueue relying on natural ordering also does not permit insertion of non-comparable objects");
        while (currencyQueue.peek() != null) {
            Logger.info("polled currency: " + currencyQueue.poll());
        }

        return currencyList;
    }

    @Around("getCurrencyRateListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<CurrencyRate> getCurrencyRateList(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<CurrencyRate> currencyRateList =
                (List<CurrencyRate>) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(CurrencyRateListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("Array deque have no capacity restrictions so they grow as necessary to support usage");
        Logger.info("They are not thread-safe; in the absence of external synchronization");
        Logger.info("They do not support concurrent access by multiple threads");
        Logger.info("Null elements are prohibited in the array deque");
        Logger.info("They are faster than Stack and LinkedList");
        Deque<CurrencyRate> currencyRateDeque = new ArrayDeque<>(currencyRateList.size());
        ListIterator<CurrencyRate> currencyRateListIterator = currencyRateList.listIterator();
        while (currencyRateListIterator.hasNext()) {
            CurrencyRate currencyRate = currencyRateListIterator.next();
            Logger.info("added as first: " + currencyRateDeque.offerFirst(currencyRate));
            Logger.info("get first: " + currencyRateDeque.peekFirst());
            Logger.info("remove first: " + currencyRateDeque.pollFirst());
            Logger.info("added as last: " + currencyRateDeque.offerLast(currencyRate));
            Logger.info("get last: " + currencyRateDeque.peekLast());
            Logger.info("remove last: " + currencyRateDeque.pollLast());
            currencyRateDeque.push(currencyRate);
            Logger.info("pushed as first: " + currencyRateDeque.getFirst());
            if (!currencyRateListIterator.hasNext()) {
                Logger.info("Currencies rates list is finished");
            }
        }

        return currencyRateList;
    }

    @Around("getDepartmentListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Department> getDepartmentListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Department> departmentList =
                (List<Department>) this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(DepartmentListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("HashSet class implements Set interface");
        Logger.info("HashSet does not remember the input order of elements during iteration");
        Set<Department> departmentSet = new HashSet<>(departmentList);
        for (Department department : departmentSet) {
            Logger.info(department.toString());
        }

        return departmentList;
    }

    @Around("getGroupListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Group> getGroupListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Group> groupList = (List<Group>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(GroupListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("LinkedHashSet class implements Set interface");
        Logger.info("LinkedHashSet remembers the input order of elements during iteration");
        Set<Group> groupSet = new LinkedHashSet<>();
        for (Group group : groupList) {
            groupSet.add(group);
            Logger.info("is added group: " + groupSet.contains(group));
            Logger.info("added group: " + group.toString());
        }

        return groupList;
    }

    @Around("getMatvalueListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Matvalue> getMatvalueListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Matvalue> matvalueList = (List<Matvalue>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(MatvalueListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("TreeSet provides an implementation of the Set, SortedSet, NavigableSet interfaces");
        Logger.info("Objects are stored in a sorted and ascending order");
        Logger.info("Access and retrieval times are quite fast");
        Logger.info("which makes TreeSet an excellent choice when storing large amounts");
        Logger.info("of sorted information that must be found quickly.");
        NavigableSet<Matvalue> matvalueNavigableSet = new TreeSet<>(matvalueList);
        Iterator<Matvalue> matvalueIterator = matvalueNavigableSet.descendingIterator();
        while (matvalueIterator.hasNext()) {
            Matvalue matvalue = matvalueIterator.next();
            Logger.info("current matvalue: " + matvalue);
            Logger.info("ceiling matvalue: " + matvalueNavigableSet.ceiling(matvalue));
            Logger.info("floor matvalue: " + matvalueNavigableSet.floor(matvalue));
            Logger.info("higher matvalue: " + matvalueNavigableSet.higher(matvalue));
            Logger.info("lower matvalue: " + matvalueNavigableSet.lower(matvalue));
        }

        return matvalueList;
    }

    @Around("getPermissionListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Permission> permissionList = (List<Permission>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(PermissionListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("The HashMap class uses a hashtable to implement the Map interface");
        Logger.info("This allows the execution time of basic operations such as add(), contains(), get(), remove(), put(), size()");
        Logger.info("to remain constant even for large sets.");
        Logger.info("HashMap class implements Map interface");
        Logger.info("HashMap does not remember the input order of elements during iteration");
        Map<Integer, Permission> permissionMap = new HashMap<>(permissionList.size());
        for (Permission permission : permissionList) {
            permissionMap.put(permission.getID(), permission);
        }
        Set<Map.Entry<Integer, Permission>> permissionEntrySet = permissionMap.entrySet();
        for (Map.Entry<Integer, Permission> permissionEntry : permissionEntrySet) {
            Logger.info("entry.hashCode: " + permissionEntry.hashCode());
            Logger.info("entry.key: " + permissionEntry.getKey());
            Logger.info("entry.value: " + permissionEntry.getValue());
        }

        return permissionList;
    }

    @Around("getRoleListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Role> getRoleListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Role> roleList = (List<Role>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(RoleListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("The LinkedHashMap class uses a hashtable to implement the Map interface");
        Logger.info("This allows the execution time of basic operations such as add(), contains(), get(), remove(), put(), size()");
        Logger.info("to remain constant even for large maps.");
        Logger.info("LinkedHashMap class implements Map interface");
        Logger.info("LinkedHashMap remembers the input order of elements during iteration");
        Map<Integer, Role> roleMap = new LinkedHashMap<>(roleList.size(), 0.75f, false);
        for (Role role : roleList) {
            roleMap.putIfAbsent(role.getID(), role);
        }
        Set<Integer> idRoleSet = roleMap.keySet();
        for (Integer idRole : idRoleSet) {
            Logger.info("role: " + roleMap.get(idRole));
        }

        return roleList;
    }

    @Around("getSalePriceListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<SalePrice> getSalePriceListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<SalePrice> salePriceList = (List<SalePrice>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(SalePriceListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("TreeMap class uses a tree structure to implement the Map interface");
        Logger.info("TreeMap guarantees that its elements will be sorted in an ascending key order");
        Logger.info("TreeMap class implements Map, SortedMap, NavigableMap interfaces");
        Logger.info("TreeMap contains only unique elements.");
        Logger.info("TreeMap cannot have a null key but can have multiple null values.");
        NavigableMap<Integer, SalePrice> salePriceNavigableMap = new TreeMap<>();
        for (SalePrice salePrice : salePriceList) {
            salePriceNavigableMap.putIfAbsent(salePrice.getID(), salePrice);
        }
        SalePrice firstSalePrice = salePriceNavigableMap.firstEntry().getValue();
        Logger.info("first salePrice in navigableMap: " + firstSalePrice.toString());
        SalePrice lastSalePrice = salePriceNavigableMap.lastEntry().getValue();
        Logger.info("last salePrice in navigableMap: " + lastSalePrice.toString());
        SortedSet<Integer> idSalePriceSortedSet = salePriceNavigableMap.navigableKeySet();
        for (Integer idSalePrice : idSalePriceSortedSet) {
            SalePrice currentSalePrice = salePriceNavigableMap.get(idSalePrice);
            Logger.info("current salePrice: " + currentSalePrice.toString());
            Map.Entry<Integer, SalePrice> ceilingSalePriceEntryMap =
                    salePriceNavigableMap.ceilingEntry(idSalePrice);
            if (ceilingSalePriceEntryMap != null) {
                Logger.info("ceiling salePrice: " +
                        ceilingSalePriceEntryMap.getValue().toString());
            } else {
                Logger.info("ceiling salePrice is null pointer object");
            }
            Map.Entry<Integer, SalePrice> floorSalePriceEntryMap =
                    salePriceNavigableMap.floorEntry(idSalePrice);
            if (floorSalePriceEntryMap != null) {
                Logger.info("floor salePrice: " +
                        floorSalePriceEntryMap.getValue().toString());
            } else {
                Logger.info("floor salePrice is null pointer object");
            }
            Map.Entry<Integer, SalePrice> higherSalePriceEntryMap =
                    salePriceNavigableMap.higherEntry(idSalePrice);
            if (higherSalePriceEntryMap != null) {
                Logger.info("higher salePrice: " +
                        higherSalePriceEntryMap.getValue().toString());
            } else {
                Logger.info("higher salePrice is null pointer object.");
            }
            Map.Entry<Integer, SalePrice> lowerSalePriceEntryMap =
                    salePriceNavigableMap.lowerEntry(idSalePrice);
            if (lowerSalePriceEntryMap != null) {
                Logger.info("lower salePrice: " +
                        lowerSalePriceEntryMap.getValue().toString());
            } else {
                Logger.info("lower salePrice is null pointer object.");
            }
        }

        return salePriceList;
    }

    @Around("getUnitofmsrListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Unitofmsr> getUnitofmsrListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Unitofmsr> unitofmsrList = (List<Unitofmsr>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(UnitofmsrListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("It is similar to HashMap except that it uses reference equality when comparing the elements.");
        Map<String, Unitofmsr> unitofmsrIdentityHashMap = new IdentityHashMap<>();
        for (Unitofmsr unitofmsr : unitofmsrList) {
            unitofmsrIdentityHashMap.put(unitofmsr.getCode(), unitofmsr);
        }
        Set<Map.Entry<String, Unitofmsr>> unitofmsrEntrySet = unitofmsrIdentityHashMap.entrySet();
        for (Map.Entry<String, Unitofmsr> unitofmsrEntry : unitofmsrEntrySet) {
            Unitofmsr unitofmsr = unitofmsrIdentityHashMap.get(unitofmsrEntry.getKey());
            Logger.info("unitofmsr.code: " + unitofmsrEntry.getKey());
            Logger.info("unitofmsr: " + unitofmsr.toString());
        }

        return unitofmsrList;
    }

    @Around("getWorkmanListServicePointcut()")
    @SuppressWarnings("unchecked")
    public List<Workman> getWorkmanListServiceAdvice(
            ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Workman> workmanList = (List<Workman>)
                this.getEntityServiceAdvice(proceedingJoinPoint);
        Message message = this.messageFactory.getMessage(WorkmanListReadMessageCode);
        Logger.info(message.toString());

        Logger.info("TreeMap class uses a tree structure to implement the Map interface");
        Logger.info("TreeMap guarantees that its elements will be sorted in an ascending key order");
        Logger.info("TreeMap class implements Map, SortedMap, NavigableMap interfaces");
        Logger.info("TreeMap contains only unique elements.");
        Logger.info("TreeMap cannot have a null key but can have multiple null values.");
        NavigableMap<Integer, Workman> workmanNavigableMap = new TreeMap<>();
        ListIterator<Workman> workmanListIterator = workmanList.listIterator();
        while (workmanListIterator.hasNext()) {
            Integer index = workmanListIterator.nextIndex();
            Workman workman = workmanListIterator.next();
            workmanNavigableMap.put(index, workman);
        }
        Set<Map.Entry<Integer, Workman>> workmanEntryNavigableSet = workmanNavigableMap.entrySet();
        for (Map.Entry<Integer, Workman> workmanEntry : workmanEntryNavigableSet) {
            Logger.info("entry.hashCode: " + workmanEntry.hashCode());
            Logger.info("entry.key: " + workmanEntry.getKey());
            Logger.info("entry.value: " + workmanEntry.getValue());
        }

        return workmanList;
    }

    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Currency> " +
            "net.bsoftlab.service.CurrencyService.getCurrencyList(..))")
    public void getCurrencyListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.CurrencyRate> " +
            "net.bsoftlab.service.CurrencyRateService.getCurrencyRateList(..))")
    public void getCurrencyRateListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Department> " +
            "net.bsoftlab.service.DepartmentService.getDepartmentList(..))")
    public void getDepartmentListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Group> " +
            "net.bsoftlab.service.GroupService.getGroupList(..))")
    public void getGroupListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Matvalue> " +
            "net.bsoftlab.service.MatvalueService.getMatvalueList(..))")
    public void getMatvalueListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Permission> " +
            "net.bsoftlab.service.PermissionService.getPermissionList())")
    public void getPermissionListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Role> " +
            "net.bsoftlab.service.RoleService.getRoleList())")
    public void getRoleListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.SalePrice> " +
            "net.bsoftlab.service.SalePriceService.getSalePriceList(..))")
    public void getSalePriceListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Unitofmsr> " +
            "net.bsoftlab.service.UnitofmsrService.getUnitofmsrList(..))")
    public void getUnitofmsrListServicePointcut(){}
    @Pointcut("execution(public java.util.List<net.bsoftlab.model.Workman> " +
            "net.bsoftlab.service.WorkmanService.getWorkmanList())")
    public void getWorkmanListServicePointcut(){}
}