package com.mflyyou.cloud.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "name is empty")
    @Min(10)
    private String name;
}
