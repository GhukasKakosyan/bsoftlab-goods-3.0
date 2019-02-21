package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Group;
import net.bsoftlab.controller.rest.GroupRestController;
import net.bsoftlab.resource.GroupResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupResourceAssembler extends
        ResourceAssemblerSupport<Group, GroupResource> {

    public GroupResourceAssembler(
            Class<GroupRestController> groupRestControllerClass,
            Class<GroupResource> groupResourceClass) {
        super(groupRestControllerClass, groupResourceClass);
    }
    public GroupResourceAssembler() {
        this(GroupRestController.class, GroupResource.class);
    }

    @Override
    public GroupResource toResource(Group group) {
        if (group == null) {
            return null;
        }
        GroupResource groupResource = this.instantiateResource(group);
        groupResource.setCode(group.getCode());
        groupResource.setName(group.getName());
        groupResource.setEnhancedName(group.getEnhancedName());
        groupResource.add(ControllerLinkBuilder.linkTo(
                GroupRestController.class).slash(group.getCode()).withSelfRel());
        return groupResource;
    }

    public List<GroupResource> toResources(List<Group> groupList) {
        if (groupList == null) {
            return null;
        }
        return super.toResources(groupList);
    }
}