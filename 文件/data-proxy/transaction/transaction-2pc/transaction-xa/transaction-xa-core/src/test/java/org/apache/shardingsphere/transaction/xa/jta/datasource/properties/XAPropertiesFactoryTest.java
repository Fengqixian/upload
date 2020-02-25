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

package com.clinbrain.bd.transaction.xa.jta.datasource.properties;

import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect.H2XAProperties;
import com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect.MySQLXAProperties;
import com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect.OracleXAProperties;
import com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect.PostgreSQLXAProperties;
import com.clinbrain.bd.transaction.xa.jta.datasource.properties.dialect.SQLServerXAProperties;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public final class XAPropertiesFactoryTest {
    
    @Test
    public void assertCreateXAPropertiesForH2() {
        assertThat(XAPropertiesFactory.createXAProperties(DatabaseType.H2), instanceOf(H2XAProperties.class));
    }
    
    @Test
    public void assertCreateXAPropertiesForMySQL() {
        assertThat(XAPropertiesFactory.createXAProperties(DatabaseType.MySQL), instanceOf(MySQLXAProperties.class));
    }
    
    @Test
    public void assertCreateXAPropertiesForPostgreSQL() {
        assertThat(XAPropertiesFactory.createXAProperties(DatabaseType.PostgreSQL), instanceOf(PostgreSQLXAProperties.class));
    }
    
    @Test
    public void assertCreateXAPropertiesForOracle() {
        assertThat(XAPropertiesFactory.createXAProperties(DatabaseType.Oracle), instanceOf(OracleXAProperties.class));
    }
    
    @Test
    public void assertCreateXAPropertiesForSQLServer() {
        assertThat(XAPropertiesFactory.createXAProperties(DatabaseType.SQLServer), instanceOf(SQLServerXAProperties.class));
    }
}
