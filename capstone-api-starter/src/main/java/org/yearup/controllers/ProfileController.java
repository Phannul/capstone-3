package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;
    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }
    // a method to get the loggedin user
    @GetMapping
    public Profile getProfile(Principal principal) {
        String name = principal.getName();
        User user = userDao.getByUserName(name);
        Profile profile = profileDao.getByUserId(user.getId());
        return profile;
    }
    // a method to update the information of the logged in user
    @PutMapping
    public Profile updateProfile(Principal principal, @RequestBody Profile profile){
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        profile.setUserId(user.getId());
        Profile updatedProfile = profileDao.update(profile);
        return updatedProfile;
    }

}
