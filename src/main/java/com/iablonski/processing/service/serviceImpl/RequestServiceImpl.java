package com.iablonski.processing.service.serviceImpl;

import com.iablonski.processing.domain.PhoneDetails;
import com.iablonski.processing.domain.Request;
import com.iablonski.processing.domain.StatusEnum;
import com.iablonski.processing.domain.User;
import com.iablonski.processing.dto.RequestDTO;
import com.iablonski.processing.dto.RequestDetailsDTO;
import com.iablonski.processing.exception.InvalidRequestStateException;
import com.iablonski.processing.exception.RequestNotFoundException;
import com.iablonski.processing.exception.RequestUpdateException;
import com.iablonski.processing.mapper.RequestMapper;
import com.iablonski.processing.repository.RequestRepo;
import com.iablonski.processing.service.PhoneDetailsService;
import com.iablonski.processing.service.RequestService;
import com.iablonski.processing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepo requestRepo;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final PhoneDetailsService phoneDetailsService;

    @Autowired
    public RequestServiceImpl(RequestRepo requestRepo, RequestMapper requestMapper, UserService userService, PhoneDetailsService phoneDetailsService) {
        this.requestRepo = requestRepo;
        this.requestMapper = requestMapper;
        this.userService = userService;
        this.phoneDetailsService = phoneDetailsService;
    }

    @Override
    @Transactional
    public void createRequest(RequestDTO requestDTO, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        PhoneDetails phoneDetails = phoneDetailsService.createPhoneDetails(requestDTO.phoneNumber());
        Request request = new Request();
        request.setTitle(requestDTO.title());
        request.setText(requestDTO.text());
        request.setPhoneDetails(phoneDetails);
        request.setStatus(StatusEnum.DRAFT);
        request.setUser(user);
        requestRepo.save(request);
    }

    @Override
    public RequestDetailsDTO getRequestByIdAndStatus(UUID requestId) {
        Request request = requestRepo.findRequestByIdAndStatus(requestId, StatusEnum.SENT)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));
        System.out.println(request);
        return requestMapper.toRequestDTO(request);
    }

    @Override
    public void sendRequest(UUID requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus() != StatusEnum.DRAFT) {
            throw new InvalidRequestStateException("Unable to send request with status " + request.getStatus());
        }
        request.setStatus(StatusEnum.SENT);
        requestRepo.save(request);
    }

    @Override
    public void acceptRequest(UUID requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus() != StatusEnum.SENT) {
            throw new InvalidRequestStateException("Unable to accept request with status " + request.getStatus());
        }
        request.setStatus(StatusEnum.ACCEPTED);
        requestRepo.save(request);
    }

    @Override
    public void rejectRequest(UUID requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus() != StatusEnum.SENT) {
            throw new InvalidRequestStateException("Unable to reject request with status " + request.getStatus());
        }
        request.setStatus(StatusEnum.REJECTED);
        requestRepo.save(request);
    }

    @Override
    public void updateRequest(RequestDTO requestDTO) {
        Request request = requestRepo.findRequestByIdAndStatus(requestDTO.id(), StatusEnum.DRAFT)
                .orElseThrow(() -> new RequestUpdateException("Unable to update request: request not found or not in DRAFT status"));
        if (requestDTO.title() != null) request.setTitle(requestDTO.title());
        if (requestDTO.text() != null) request.setText(requestDTO.text());
        if (requestDTO.phoneNumber() != null) {
            PhoneDetails phoneDetails = phoneDetailsService.createPhoneDetails(requestDTO.phoneNumber());
            request.setPhoneDetails(phoneDetails);
        }
        requestRepo.save(request);
    }

    @Override
    public Page<RequestDetailsDTO> getRequestsBuUserParams(PageRequest pageRequest, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        return requestRepo.findAllByUser(user, pageRequest).map(requestMapper::toRequestDTO);
    }

    @Override
    public Page<RequestDetailsDTO> getAllRequestsByStatus(StatusEnum statusEnum, PageRequest pageRequest) {
        return requestRepo.findAllRequestsByStatus(statusEnum, pageRequest).map(requestMapper::toRequestDTO);
    }

    @Override
    public Page<RequestDetailsDTO> getRequestsByAdminParams(String username, PageRequest pageRequest) {
        return requestRepo.findRequestsByAdminParams(username, pageRequest).map(requestMapper::toRequestDTO);
    }

    @Override
    public Page<RequestDetailsDTO> getAllRequestsByOperatorParams(String username, PageRequest pageRequest) {
        return requestRepo.findRequestsByOperatorParams(username, pageRequest).map(requestMapper::toRequestDTO);
    }

    private Request getRequestById(UUID requestId) {
        return requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request with id " + requestId + " not found"));
    }
}