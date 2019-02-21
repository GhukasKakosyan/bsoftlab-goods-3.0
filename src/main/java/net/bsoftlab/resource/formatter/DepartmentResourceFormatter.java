package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Department;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.DepartmentService;
import net.bsoftlab.resource.assembler.DepartmentResourceAssembler;
import net.bsoftlab.resource.DepartmentResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentResourceFormatter implements Formatter<DepartmentResource> {

    private DepartmentResourceAssembler departmentResourceAssembler;
    private DepartmentService departmentService;

    @Autowired
    public DepartmentResourceFormatter(
            DepartmentResourceAssembler departmentResourceAssembler,
            DepartmentService departmentService) {
        this.departmentResourceAssembler = departmentResourceAssembler;
        this.departmentService = departmentService;
    }

    @Override
    public DepartmentResource parse(String code, Locale locale) throws ServiceException {
        Department department = this.departmentService.getDepartment(code);
        return this.departmentResourceAssembler.toResource(department);
    }
    @Override
    public String print(DepartmentResource departmentResource, Locale locale) {
        try {
            return departmentResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
