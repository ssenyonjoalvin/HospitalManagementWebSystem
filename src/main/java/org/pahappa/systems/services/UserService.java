package org.pahappa.systems.services;

import org.pahappa.systems.models.User;

public interface UserService {
    User user = null;
   default  User login(String email, String password){
        return user;
   };

    default void register(User user){};
}