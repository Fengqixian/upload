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

package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query.text;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;
import lombok.ToString;

/**
 * Command query packet for PostgreSQL.
 *
 * @author zhangyonglun
 */
@Getter
@ToString
public final class PostgreSQLComQueryPacket extends PostgreSQLCommandPacket {
    
    private final String sql;
    
    public PostgreSQLComQueryPacket(final PostgreSQLPacketPayload payload) {
        payload.readInt4();
        sql = payload.readStringNul();
    }
    
    @Override
    public void write(final PostgreSQLPacketPayload payload) {
    }
    
    @Override
    public char getMessageType() {
        return PostgreSQLCommandPacketType.QUERY.getValue();
    }
}
