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
public class CreateOrderResponse implements Serializable {
    private static final long serialVersionUID = -5020827357883115662L;
    private Long id;
}
