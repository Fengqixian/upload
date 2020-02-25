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

package com.clinbrain.bd.shardingproxy.frontend.mysql;

import lombok.Getter;
import com.clinbrain.bd.core.constant.DatabaseType;
import com.clinbrain.bd.shardingproxy.backend.communication.jdbc.connection.BackendConnection;
import com.clinbrain.bd.shardingproxy.frontend.context.FrontendContext;
import com.clinbrain.bd.shardingproxy.frontend.mysql.auth.MySQLAuthenticationEngine;
import com.clinbrain.bd.shardingproxy.frontend.mysql.command.MySQLCommandExecuteEngine;
import com.clinbrain.bd.shardingproxy.frontend.spi.DatabaseProtocolFrontendEngine;
import com.clinbrain.bd.shardingproxy.transport.codec.DatabasePacketCodecEngine;
import com.clinbrain.bd.shardingproxy.transport.mysql.codec.MySQLPacketCodecEngine;

/**
 * Frontend engine for MySQL protocol.
 *
 * @author zhangliang
 * @author panjuan
 * @author wangkai
 * @author zhangyonglun
 */
@Getter
public final class MySQLProtocolFrontendEngine implements DatabaseProtocolFrontendEngine {
    
    private final FrontendContext frontendContext = new FrontendContext(false, true);
    
    private final MySQLAuthenticationEngine authEngine = new MySQLAuthenticationEngine();
    
    private final MySQLCommandExecuteEngine commandExecuteEngine = new MySQLCommandExecuteEngine();
    
    private final DatabasePacketCodecEngine codecEngine = new MySQLPacketCodecEngine();
    
    @Override
    public String getDatabaseType() {
        return DatabaseType.MySQL.name();
    }
    
    @Override
    public void release(final BackendConnection backendConnection) {
    }
}
