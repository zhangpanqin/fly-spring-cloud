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
public class GetUserResponse implements Serializable {
    private static final long serialVersionUID = -4750077363460707365L;
    private Long id;
    private String username;
}
