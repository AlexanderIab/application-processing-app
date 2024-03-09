package com.iablonski.processing.service.serviceImpl;

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

    @Autowired
    public RequestServiceImpl(RequestRepo requestRepo, RequestMapper requestMapper, UserService userService) {
        this.requestRepo = requestRepo;
        this.requestMapper = requestMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void createRequest(RequestDTO requestDTO, Principal principal) {
        User user = userService.getUserFromPrincipal(principal);
        Request request = new Request();
        request.setTitle(requestDTO.title());
        request.setText(requestDTO.text());
        request.setPhoneNumber(requestDTO.phoneNumber());
        request.setStatus(StatusEnum.DRAFT);
        request.setUser(user);
        requestRepo.save(request);
    }

    @Override
    public RequestDetailsDTO getRequestByIdAndStatus(UUID requestId) {
        Request request = requestRepo.findRequestByIdAndStatus(requestId, StatusEnum.SENT)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));
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
        request.setTitle(requestDTO.title());
        request.setText(requestDTO.text());
        request.setPhoneNumber(requestDTO.phoneNumber());
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