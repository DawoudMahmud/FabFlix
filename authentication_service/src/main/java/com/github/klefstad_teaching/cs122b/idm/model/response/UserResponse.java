package com.github.klefstad_teaching.cs122b.idm.model.response;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

import java.util.List;

public class UserResponse {
    private List<User> users;

    public List<User> getUsers()
    {
        return users;
    }

    public UserResponse setUsers(List<User> users)
    {
        this.users = users;
        return this;
    }

}
