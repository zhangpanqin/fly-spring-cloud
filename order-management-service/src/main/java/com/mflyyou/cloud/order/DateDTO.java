package com.mflyyou.cloud.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateDTO implements Serializable {
    private static final long serialVersionUID = -5264552202236260878L;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private Instant instant;
}
