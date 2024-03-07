package com.iablonski.processing.controller;

import com.iablonski.processing.dto.UserDTO;
import com.iablonski.processing.payload.response.MessageResponse;
import com.iablonski.processing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsersList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/all")
    public ResponseEntity<MessageResponse> giveOperatorRole(@RequestBody UUID userId){
        userService.giveOperatorRoleToUser(userId);
        return ResponseEntity.ok(new MessageResponse("Role changed to OPERATOR"));
    }
}
