package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.handshake;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.PostgreSQLPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;

/**
 * @author WANGYI
 * */
@Getter
public class PasswordMessagePacket implements PostgreSQLPacket {
    private final char messageType = '\0';

    private String password;

    public PasswordMessagePacket(final PostgreSQLPacketPayload payload) {
        int len = payload.readInt4();
        password = payload.readStringNul();
    }

    @Override
    public void write(PostgreSQLPacketPayload payload) {

    }
}
