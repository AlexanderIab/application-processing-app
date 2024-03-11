package com.iablonski.processing.mapper;

import com.iablonski.processing.domain.User;
import com.iablonski.processing.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);
}