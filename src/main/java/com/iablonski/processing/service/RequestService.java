package com.iablonski.processing.service;

import com.iablonski.processing.dto.RequestDTO;
import com.iablonski.processing.dto.RequestDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.security.Principal;
import java.util.UUID;

public interface RequestService {
    void createRequest(RequestDTO requestDTO, Principal principal);
    RequestDetailsDTO getRequestByIdAndStatus(UUID requestId);
    void sendRequest(UUID requestId);
    void acceptRequest(UUID requestId);
    void rejectRequest(UUID requestId);
    void updateRequest(RequestDTO requestDTO);
    Page<RequestDetailsDTO> getRequestsByAdminParams(String username, PageRequest pageRequest);
    Page<RequestDetailsDTO> getAllRequestsByOperatorParams(String username, PageRequest pageRequest);
    Page<RequestDetailsDTO> getRequestsBuUserParams(PageRequest pageRequest, Principal principal);
}

