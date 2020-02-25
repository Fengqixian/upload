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

package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.query;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.PostgreSQLPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Row description packet for PostgreSQL.
 *
 * @author zhangyonglun
 */
@RequiredArgsConstructor
public final class PostgreSQLRowDescriptionPacket implements PostgreSQLPacket {
    
    @Getter
    private final char messageType = PostgreSQLCommandPacketType.ROW_DESCRIPTION.getValue();
    
    @Getter
    private final int fieldCount;
    
    private final List<PostgreSQLColumnDescription> columnDescriptions;
    
    @Override
    public void write(final PostgreSQLPacketPayload payload) {
        payload.writeInt2(fieldCount);
        for (PostgreSQLColumnDescription each : columnDescriptions) {
            //columnname
            payload.writeStringNul(each.getColumnName());
            //table oid
            payload.writeInt4(each.getTableOID());
            //special table oid
            payload.writeInt2(each.getColumnIndex());


            payload.writeInt4(each.getTypeOID());
            payload.writeInt2(each.getColumnLength());
            payload.writeInt4(each.getTypeModifier());
            payload.writeInt2(each.getDataFormat());
        }
    }
}
