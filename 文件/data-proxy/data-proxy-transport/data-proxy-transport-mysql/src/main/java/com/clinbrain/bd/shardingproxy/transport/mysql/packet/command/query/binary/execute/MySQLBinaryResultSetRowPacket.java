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

package com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.binary.execute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.clinbrain.bd.shardingproxy.transport.mysql.constant.MySQLColumnType;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.MySQLPacket;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.binary.execute.MySQLNullBitmap;
import com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.binary.execute.protocol.MySQLBinaryProtocolValueFactory;
import com.clinbrain.bd.shardingproxy.transport.mysql.payload.MySQLPacketPayload;

import java.util.List;

/**
 * Binary result set row packet for MySQL.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/binary-protocol-resultset-row.html">Binary Protocol Resultset Row</a>
 *
 * @author zhangyonglun
 */
@RequiredArgsConstructor
public final class MySQLBinaryResultSetRowPacket implements MySQLPacket {
    
    private static final int PACKET_HEADER = 0x00;
    
    private static final int NULL_BITMAP_OFFSET = 2;
    
    @Getter
    private final int sequenceId;
    
    private final List<Object> data;
    
    private final List<MySQLColumnType> columnTypes;
    
    @Override
    public void write(final MySQLPacketPayload payload) {
        payload.writeInt1(PACKET_HEADER);
        writeNullBitmap(payload);
        writeValues(payload);
    }
    
    private void writeNullBitmap(final MySQLPacketPayload payload) {
        for (int each : getNullBitmap().getNullBitmap()) {
            payload.writeInt1(each);
        }
    }
    
    private com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.binary.execute.MySQLNullBitmap getNullBitmap() {
        com.clinbrain.bd.shardingproxy.transport.mysql.packet.command.query.binary.execute.MySQLNullBitmap result = new MySQLNullBitmap(columnTypes.size(), NULL_BITMAP_OFFSET);
        for (int columnIndex = 0; columnIndex < columnTypes.size(); columnIndex++) {
            if (null == data.get(columnIndex)) {
                result.setNullBit(columnIndex);
            }
        }
        return result;
    }
    
    private void writeValues(final MySQLPacketPayload payload) {
        for (int i = 0; i < columnTypes.size(); i++) {
            Object value = data.get(i);
            if (null != value) {
                MySQLBinaryProtocolValueFactory.getBinaryProtocolValue(columnTypes.get(i)).write(payload, value);
            }
        }
    }
}
