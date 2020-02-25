package com.clinbrain.bd.shardingproxy.transport.postgresql.packet.handshake;

import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.PostgreSQLPacket;
import com.clinbrain.bd.shardingproxy.transport.postgresql.packet.command.PostgreSQLCommandPacketType;
import com.clinbrain.bd.shardingproxy.transport.postgresql.payload.PostgreSQLPacketPayload;
import lombok.Getter;

/**
 * @author WANGYI
 */
public class AuthenticationCleartextPasswordPacket  implements PostgreSQLPacket {
    @Getter
    private final char messageType = PostgreSQLCommandPacketType.AUTHENTICATION_CLEARTEXT_PASSWORD.getValue();
    public AuthenticationCleartextPasswordPacket() {
    }
    @Override
    public void write(final PostgreSQLPacketPayload payload) {
        payload.writeInt4(3);
    }
}
