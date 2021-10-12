package com.freckie.week3.controller;

import com.freckie.week3.payload.user.GetUserListResponse;
import com.freckie.week3.payload.user.GetUserProfileResponse;
import com.freckie.week3.payload.user.PostSignUpRequest;
import com.freckie.week3.payload.user.PostSignUpResponse;
import com.freckie.week3.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
@Api(value="Users", tags={"User Management"})
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value="[ADMIN] Get all users")
    public ResponseEntity<GetUserListResponse> getUserList() {
        GetUserListResponse resp = userService.getUserList();

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/users")
    @ApiOperation(value="Add new user")
    @ApiImplicitParams({
        @ApiImplicitParam(name="name", value="User's name", required=true, dataType="String"),
        @ApiImplicitParam(name="email", value="User's unique email", required=true, dataType="String"),
        @ApiImplicitParam(name="password", value="User's plain password", required=true, dataType="String")
    })
    @ApiResponses({
        @ApiResponse(code=200, message="Success"),
        @ApiResponse(code=400, message="Bad request body")
    })
    public ResponseEntity<PostSignUpResponse> signUp(
        @Valid @RequestBody PostSignUpRequest req
    ) {
        try {
            PostSignUpResponse resp = userService.signUp(req);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{user_id}/profile")
    @ApiOperation(value="Get profile of a specific user")
    @ApiImplicitParams({
        @ApiImplicitParam(name="user_id", value="User's id", required=true, dataType="Long")
    })
    @ApiResponses({
        @ApiResponse(code=200, message="Success"),
        @ApiResponse(code=404, message="user_id does not exists.")
    })
    public ResponseEntity<GetUserProfileResponse> getUserProfile(
        @PathVariable(value="user_id") String userId
    ) {
        Long _userId = Long.parseLong(userId);
        try {
            GetUserProfileResponse resp = userService.getUserProfile(_userId);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/signout")
    @ApiOperation(value="Sign out from user session.")
    public ResponseEntity<String> signOut(
        HttpServletRequest req, HttpServletResponse resp
    ) {
        new SecurityContextLogoutHandler().logout(req, resp, SecurityContextHolder.getContext().getAuthentication());
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
