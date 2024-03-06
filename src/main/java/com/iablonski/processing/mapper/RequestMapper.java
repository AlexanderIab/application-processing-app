package com.iablonski.processing.mapper;

import com.iablonski.processing.domain.Request;
import com.iablonski.processing.dto.RequestDetailsDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    Request toRequest(RequestDetailsDTO requestDetailsDTO);
    @InheritInverseConfiguration
    RequestDetailsDTO toRequestDTO(Request request);
}
