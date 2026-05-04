package com.garment.common.interfaces;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页查询基类。
 * <p>
 * 所有需要分页的 Controller 入参均可继承此类。
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页码（从 1 开始），默认 1 */
    @Min(value = 1, message = "页码不能小于 1")
    private int pageNum = 1;

    /** 每页条数，默认 10，最大 100 */
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 100, message = "每页条数不能大于 100")
    private int pageSize = 10;

    /** 排序字段 */
    private String orderBy;

    /** 是否升序（默认降序） */
    private boolean asc = false;
}