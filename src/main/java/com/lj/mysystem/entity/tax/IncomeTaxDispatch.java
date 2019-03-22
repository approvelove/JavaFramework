package com.lj.mysystem.entity.tax;

import com.lj.mysystem.entity.base.BaseEntity;
import lombok.Data;

@Data
public class IncomeTaxDispatch extends BaseEntity {
    private Integer taxId;

    private String taxLevel;

    private String taxDeduction;

    private String taxRate;

    private String taxQuick;

    private String taxUpper;

    private String taxLower;
}