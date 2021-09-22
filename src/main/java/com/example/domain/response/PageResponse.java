package com.example.domain.response;

import lombok.Data;

import java.util.List;

/**
 * 分页响应类
 */
@Data
public class PageResponse<T> {

    private Integer pageNum, pageSize;

    private long total;

    private List<T> data;
}
