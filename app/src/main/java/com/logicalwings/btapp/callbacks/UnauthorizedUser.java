package com.logicalwings.btapp.callbacks;

public interface UnauthorizedUser {
    void onUnauthorized(int code);

    void onAuthorizedSuccess(int code);

    void onAuthorizedFail(int code);
}
