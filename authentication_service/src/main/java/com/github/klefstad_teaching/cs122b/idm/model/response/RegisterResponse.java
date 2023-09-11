package com.github.klefstad_teaching.cs122b.idm.model.response;

import com.gitcodings.stack.core.base.ResponseModel;
import com.gitcodings.stack.core.result.Result;

public class RegisterResponse extends ResponseModel<RegisterResponse> {

    private Result result;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public RegisterResponse setResult(Result result) {
        this.result = result;
        return this;
    }
}
