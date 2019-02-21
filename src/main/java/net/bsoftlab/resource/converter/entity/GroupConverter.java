package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Group;
import net.bsoftlab.resource.GroupResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupConverter implements Converter<GroupResource, Group> {

    @Override
    public Group convert(GroupResource groupResource) {
        if (groupResource == null) {
            return null;
        }
        Group group = new Group();
        group.setCode(groupResource.getCode());
        group.setName(groupResource.getName());
        group.setEnhancedName(groupResource.getEnhancedName());
        return group;
    }
}
