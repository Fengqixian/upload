package com.clinbrain.bd.core.parse;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.core.parse.SQLRebuildListener
 * @createdDate 2019/6/12 15:35
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (shardingsphere)
 */
public abstract class SQLRebuildListener implements ParseTreeListener {
    @Getter
    @Setter
    private String username = null;
}
