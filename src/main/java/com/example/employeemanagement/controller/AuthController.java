// package com.example.employeemanagement.controller;

// import com.example.employeemanagement.model.User;
// import com.example.employeemanagement.repository.UserRepository;
// import com.example.employeemanagement.security.JwtTokenUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;
// import com.example.employeemanagement.model.Role;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import org.springframework.security.core.Authentication;


// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;

// /** This class represents the REST API controller for user authentication. */
// @RestController
// @Tag(name = "Authentication APIs", description = "API Operations related to user authentication")
// @RequestMapping("/api/auth")
// public class AuthController {

//   /** The authentication manager. */
//   @Autowired
//   private AuthenticationManager authenticationManager;

//   /** The user details service. */
//   @Autowired
//   private UserDetailsService userDetailsService;

//   /** The user repository. */
//   @Autowired
//   private UserRepository userRepository;

//   /** The password encoder. */
//   @Autowired
//   private PasswordEncoder passwordEncoder;

//   /** The JWT token util. */
//   @Autowired
//   private JwtTokenUtil jwtTokenUtil;

//   /**
//    * Register user API.
//    *
//    * @param user The user to be registered
//    * @return Success message
//    */
//   @Operation(summary = "Register user", description = "Register a new user")
//   @ApiResponses(
//       value = {
//           @ApiResponse(responseCode = "200", description = "User registered successfully"),
//           @ApiResponse(responseCode = "409", description = "Username already exists"),
//           @ApiResponse(responseCode = "500", description = "Unable to register user")
//       })
//   @PostMapping("/register")
// public ResponseEntity<?> registerUser(@RequestBody User user) {

//     if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//         return ResponseEntity.status(HttpStatus.CONFLICT)
//                 .body(Map.of("message", "Username already exists"));
//     }

//     // Mặc định role là EMPLOYEE khi tự đăng ký
//     if (user.getRole() == null) {
//         user.setRole(Role.EMPLOYEE);
//     }

//     // Chỉ ADMIN mới được tạo tài khoản ADMIN hoặc DEPARTMENT
//     // (nên xử lý logic này ở API quản lý user riêng, không ở register công khai)

//     user.setPassword(passwordEncoder.encode(user.getPassword()));
//     user.setEnabled(true);
//     userRepository.save(user);

//     return ResponseEntity.ok(Map.of(
//         "message", "User registered successfully",
//         "username", user.getUsername(),
//         "role", user.getRole().name()
//     ));
// }

//   /**
//    * Authenticate user API.
//    *
//    * @param user The user to be authenticated
//    * @return JWT token
//    * @throws Exception If authentication fails
//    */
//   @Operation(summary = "Authenticate user", description = "Authenticate a user and generate a JWT token")
//   @ApiResponses(
//       value = {
//           @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
//           @ApiResponse(responseCode = "401", description = "Invalid username or password"),
//           @ApiResponse(responseCode = "500", description = "Unable to authenticate user")
//       })
//   @PostMapping("/authenticate")
//   public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
//     try {
//       authenticationManager.authenticate(
//           new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//       );

//       final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//       final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

//       Map<String, String> response = new HashMap<>();
//       response.put("token", jwt);
//       return ResponseEntity.ok(response);

//     } catch (BadCredentialsException e) {
//       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password");
//     } catch (Exception e) {
//       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to authenticate");
//     }
//   }

//   /**
//    * Verify if a username exists.
//    *
//    * @param username The username to verify
//    * @return Response message indicating whether the username exists
//    */
//   @Operation(summary = "Verify username", description = "Verify if a username exists in the system")
//   @ApiResponses(
//       value = {
//           @ApiResponse(responseCode = "200", description = "Username exists"),
//           @ApiResponse(responseCode = "404", description = "Username not found")
//       })
//   @GetMapping("/verify-username/{username}")
//   public ResponseEntity<?> verifyUsername(@PathVariable String username) {
//     Optional<User> user = userRepository.findByUsername(username);
//     if (user.isPresent()) {
//       return ResponseEntity.ok("Username exists");
//     } else {
//       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Username not found");
//     }
//   }

//   /**
//    * Reset password for a given username.
//    *
//    * @param request Map containing the username and new password
//    * @return Response message indicating success or failure of the operation
//    */
//   @Operation(summary = "Reset password", description = "Reset the password for the given username")
//   @ApiResponses(
//       value = {
//           @ApiResponse(responseCode = "200", description = "Password reset successfully"),
//           @ApiResponse(responseCode = "404", description = "Username not found"),
//           @ApiResponse(responseCode = "500", description = "Unable to reset password")
//       })
//   @PostMapping("/reset-password")
//   public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
//     String username = request.get("username");
//     String newPassword = request.get("newPassword");

//     Optional<User> user = userRepository.findByUsername(username);

//     if (user.isPresent()) {
//       User existingUser = user.get();
//       existingUser.setPassword(passwordEncoder.encode(newPassword));
//       userRepository.save(existingUser);
//       return ResponseEntity.ok("Password reset successfully");
//     } else {
//       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Username not found");
//     }
//   }

//     @GetMapping("/me")
// public User currentUser(Authentication authentication) {

//     String username = authentication.getName(); // lấy từ JWT

//     return userRepository.findByUsername(username)
//         .orElseThrow(() -> new RuntimeException("User not found"));
// }

// }

package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Role;
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.security.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /* ================= REGISTER ================= */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // default role
        if (user.getRole() == null) {
            user.setRole(Role.EMPLOYEE);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);

        return ResponseEntity.ok("Register success");
    }

    /* ================= LOGIN ================= */

    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestBody User req) {

        try {
            // authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );

            // load user
            User user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // generate JWT (username + role)
            String token = jwtTokenUtil.generateToken(
                    user.getUsername(),
                    user.getRole().name()
            );

            // response
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("username", user.getUsername());
            res.put("role", user.getRole().name());

            return ResponseEntity.ok(res);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    /* ================= CURRENT USER ================= */

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  không trả password
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }

    /* ================= RESET PASSWORD ================= */

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {

        String username = req.get("username");
        String newPassword = req.get("newPassword");

        if (username == null || newPassword == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Username and newPassword are required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }
}
