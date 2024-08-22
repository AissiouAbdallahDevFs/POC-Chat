package com.POC.Chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.POC.Chat.dto.UserDTO;
import com.POC.Chat.dto.UserLoginRequest;
import com.POC.Chat.dto.UserProfileDTO;
import com.POC.Chat.model.User;
import com.POC.Chat.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/users")
    @ApiOperation(value = "Get all users", notes = "Returns a list of all users.")
    public ResponseEntity<Iterable<User>> getUsers() {
        Iterable<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get user by ID", notes = "Returns a user by its ID.")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new user
    @PostMapping("auth/register")
    @ApiOperation(value = "Create a new user", notes = "Creates a new user.")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        // Validate email format
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if (!matcher.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        // Check if user already exists
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        // Set creation and update timestamps
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save new user
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    // Authenticate user
    @PostMapping("auth/login")
    @ApiOperation(value = "Authenticate user", notes = "Authenticate a user and return a JWT token.")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest loginRequest) {
        String token = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (token != null) {
            HashMap<String, String> response = new HashMap<>();
            response.put("token", token);

            // Convert response to JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(response);
                return ResponseEntity.ok(jsonResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error converting response to JSON");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Get current user
    @GetMapping("/auth/me")
    @ApiOperation(value = "Get current user", notes = "Returns the current user.")
    public ResponseEntity<UserProfileDTO> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract token from "Bearer" prefix
            String token = authorizationHeader.substring(7);
            User user = userService.getUserByToken(token);

            // Create user profile DTO
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setId(user.getId());
            userProfileDTO.setEmail(user.getEmail());
            userProfileDTO.setUsername(user.getFirstName() + " " + user.getLastName());
            

            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @GetMapping("auth/current-user-id")
    public ResponseEntity<Long> getCurrentUserId(@RequestHeader("Authorization") String token) {
        // Assurez-vous que le token commence par "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Extraire le token
        }
        Long userId = userService.getUserIdFromToken(token);
        return ResponseEntity.ok(userId);
    }

    // Endpoint pour récupérer les utilisateurs connectés
    @GetMapping("auth/usersOnline")
    public ResponseEntity<List<UserDTO>> getConnectedUsers() {
        List<UserDTO> connectedUsers = userService.getConnectedUsers();
        return ResponseEntity.ok(connectedUsers);
    }

  
    

    
}
