package com.example.moneymoney.utils;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseObject {
    private String status;
    private String message;
    private Pagination pagination;
    private Object data;
}
