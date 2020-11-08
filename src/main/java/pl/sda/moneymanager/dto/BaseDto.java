package pl.sda.moneymanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDto {
    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
}
