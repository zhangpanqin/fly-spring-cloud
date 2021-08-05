package com.mflyyou.cloud.sdk.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 6693509985689881097L;
    private Long orderId;
    private Long userId;
}
