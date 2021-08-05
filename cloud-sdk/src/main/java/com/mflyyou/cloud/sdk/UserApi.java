package com.mflyyou.cloud.sdk;

import com.mflyyou.cloud.sdk.request.CreateUserRequest;
import com.mflyyou.cloud.sdk.response.CreateUserResponse;
import com.mflyyou.cloud.sdk.response.GetUserResponse;
import com.mflyyou.cloud.sdk.response.UserResponse;

public interface UserApi {
    UserResponse getUser(Long userId);
    GetUserResponse getUserInfo(Long userId);
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
}
