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

package com.clinbrain.bd.core.parse.rule.jaxb.entity.statement;

import lombok.Getter;
import com.clinbrain.bd.core.parse.rule.jaxb.entity.RuleDefinitionEntity;
import com.clinbrain.bd.core.parse.rule.jaxb.entity.statement.SQLStatementRuleEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.LinkedList;

/**
 * SQL statement rule definition entity for JAXB.
 *
 * @author zhangliang
 */
@XmlRootElement(name = "sql-statement-rule-definition")
@Getter
public final class SQLStatementRuleDefinitionEntity implements RuleDefinitionEntity {
    
    @XmlElement(name = "sql-statement-rule")
    private Collection<SQLStatementRuleEntity> rules = new LinkedList<>();
}
