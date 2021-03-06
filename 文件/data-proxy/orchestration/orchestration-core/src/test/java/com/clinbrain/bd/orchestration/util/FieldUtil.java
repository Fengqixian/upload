/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.orchestration.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * Field Utility.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class FieldUtil {
    
    /**
     * Set field.
     * 
     * @param target target to be settled
     * @param fieldName field name to be settled
     * @param fieldValue field value to be settled
     */
    @SneakyThrows
    public static void setField(final Object target, final String fieldName, final Object fieldValue) {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, fieldValue);
    }
    
    /**
     * Get field value.
     * 
     * @param target target to be getter
     * @param fieldName field name to be getter
     * @return field value
     */
    @SneakyThrows
    public static Object getFieldValue(final Object target, final String fieldName) {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
