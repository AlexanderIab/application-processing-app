package com.iablonski.processing.controller;

import com.iablonski.processing.dto.RequestDTO;
import com.iablonski.processing.dto.RequestDetailsDTO;
import com.iablonski.processing.payload.response.MessageResponse;
import com.iablonski.processing.search.RequestSearchValues;
import com.iablonski.processing.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/request")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createRequest(@RequestBody RequestDTO requestDTO,
                                                         Principal principal){
        requestService.createRequest(requestDTO, principal);
        return ResponseEntity.ok(new MessageResponse("Your request has been successfully created"));
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateRequest(@RequestBody RequestDTO requestDTO){
        requestService.updateRequest(requestDTO);
        return ResponseEntity.ok(new MessageResponse("Your request has been successfully updated"));
    }

    @PostMapping("/id")
    public ResponseEntity<RequestDetailsDTO> getRequestById(@RequestBody UUID requestId){
        RequestDetailsDTO requestDetailsDTO = requestService.getRequestByIdAndStatus(requestId);
        return ResponseEntity.ok(requestDetailsDTO);
    }

    @PutMapping("/send")
    public ResponseEntity<MessageResponse> changeRequestStatusToSent(@RequestBody UUID requestId){
        requestService.sendRequest(requestId);
        return ResponseEntity.ok(new MessageResponse("Your request status has been changed to SENT"));
    }

    @PutMapping("/accept")
    public ResponseEntity<MessageResponse> changeRequestStatusToAccepted(@RequestBody UUID requestId){
        requestService.acceptRequest(requestId);
        return ResponseEntity.ok(new MessageResponse("Your request status has been changed to ACCEPTED"));
    }

    @PutMapping("/reject")
    public ResponseEntity<MessageResponse> changeRequestStatusToRejected(@RequestBody UUID requestId){
        requestService.rejectRequest(requestId);
        return ResponseEntity.ok(new MessageResponse("Your request status has been changed to REJECTED"));
    }

    @PostMapping("/admin/search")
    public ResponseEntity<Page<RequestDetailsDTO>> searchRequestByAdminParams(@RequestBody RequestSearchValues values){
        Sort sort = Sort.by("created");
        PageRequest pageRequest = PageRequest.of(values.pageNumber(), 5, sort);
        Page<RequestDetailsDTO> requestList = requestService.getRequestsByAdminParams(values.username(), pageRequest);
        return ResponseEntity.ok(requestList);
    }

    @PostMapping("/operator/search")
    public ResponseEntity<Page<RequestDetailsDTO>> searchRequestByOperatorParams(@RequestBody RequestSearchValues values){
        Sort sort = Sort.by(values.getSortDirection(), "created");
        PageRequest pageRequest = PageRequest.of(values.pageNumber(), 5, sort);
        Page<RequestDetailsDTO> requestList = requestService.getAllRequestsByOperatorParams(values.username(), pageRequest);
        return ResponseEntity.ok(requestList);
    }

    @PostMapping("/user/search")
    public ResponseEntity<Page<RequestDetailsDTO>> searchRequestByUserParams(@RequestBody RequestSearchValues values,
                                                                             Principal principal){
        Sort sort = Sort.by(values.getSortDirection(), "created");
        PageRequest pageRequest = PageRequest.of(values.pageNumber(), 5, sort);
        Page<RequestDetailsDTO> requestList = requestService.getRequestsBuUserParams(pageRequest, principal);
        return ResponseEntity.ok(requestList);
    }





}
