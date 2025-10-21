package com.project.machine.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {

    /**
     * 建立分頁物件（預設排序欄位 machineId，升冪）
     */
    public static Pageable createPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by("machineId").ascending());
    }

    /**
     * 建立分頁物件（自訂排序欄位與方向）
     */
    public static Pageable createPageable(int page, int size, String sortField, boolean asc) {
        Sort sort = asc ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }
}