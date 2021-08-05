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
public class CreateUserResponse implements Serializable {
    private static final long serialVersionUID = 5387468228952580023L;
    private Long id;
}
