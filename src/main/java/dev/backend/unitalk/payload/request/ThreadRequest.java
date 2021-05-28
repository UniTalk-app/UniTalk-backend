package dev.backend.unitalk.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadRequest {
    private String title;
    private Long categoryId = -1L;
}
