package com.iablonski.processing.mapper;

import com.iablonski.processing.domain.Request;
import com.iablonski.processing.dto.RequestDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "phoneNumber", source = "phoneDetails.phone")
    RequestDetailsDTO toRequestDTO(Request request);
}