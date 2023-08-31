package com.github.klefstad_teaching.cs122b.idm.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.model.request.AuthenticateRequestModel;

public class AuthenticateResponseModel extends ResponseModel<AuthenticateResponseModel> {
    private Result result;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public AuthenticateResponseModel setResult(Result result) {
        this.result = result;
        return this;
    }
}
