package com.github.klefstad_teaching.cs122b.idm.model.response;


import com.gitcodings.stack.core.base.ResponseModel;
import com.gitcodings.stack.core.result.Result;

public class LoginResponseModel extends ResponseModel<LoginResponseModel> {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public LoginResponseModel setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponseModel setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
