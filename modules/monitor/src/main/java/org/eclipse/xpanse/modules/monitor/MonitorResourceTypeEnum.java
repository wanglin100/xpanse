/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * The monitor resource enum.
 */
public enum MonitorResourceTypeEnum {

    CPU("cpu"),
    MEM("mem");

    private final String value;

    MonitorResourceTypeEnum(String value) {
        this.value = value;
    }

    /**
     * For HuaweiResource deserialize.
     */
    @JsonValue
    public String toValue() {
        return this.value;
    }

    /**
     * For HuaweiResource serialize.
     */
    @JsonCreator
    public MonitorResourceTypeEnum getByValue(String name) {
        for (MonitorResourceTypeEnum huaweiResourceEnum : values()) {
            if (huaweiResourceEnum.value.equals(StringUtils.lowerCase(name))) {
                return huaweiResourceEnum;
            }
        }
        return null;
    }
}
