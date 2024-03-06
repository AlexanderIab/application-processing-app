package com.iablonski.processing.mapper;

import com.iablonski.processing.domain.User;
import com.iablonski.processing.dto.UserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    @InheritInverseConfiguration
    UserDTO toUserDTO(User user);
}
