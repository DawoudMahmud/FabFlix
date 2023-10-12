package com.github.klefstad_teaching.cs122b.idm.model.response;

import com.gitcodings.stack.core.base.ResponseModel;

public class RefreshResponseModel  extends ResponseModel<RefreshResponseModel> {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public RefreshResponseModel setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public RefreshResponseModel setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
