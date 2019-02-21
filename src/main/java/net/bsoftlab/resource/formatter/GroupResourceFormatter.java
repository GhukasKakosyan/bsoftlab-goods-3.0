package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Group;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.GroupService;
import net.bsoftlab.resource.assembler.GroupResourceAssembler;
import net.bsoftlab.resource.GroupResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupResourceFormatter implements Formatter<GroupResource> {

    private GroupResourceAssembler groupResourceAssembler;
    private GroupService groupService;

    @Autowired
    public GroupResourceFormatter(
            GroupResourceAssembler groupResourceAssembler,
            GroupService groupService) {
        this.groupResourceAssembler = groupResourceAssembler;
        this.groupService = groupService;
    }

    @Override
    public GroupResource parse(String code, Locale locale) throws ServiceException {
        Group group = this.groupService.getGroup(code);
        return this.groupResourceAssembler.toResource(group);
    }
    @Override
    public String print(GroupResource groupResource, Locale locale) {
        try {
            return groupResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
