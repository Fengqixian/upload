package com.clinbrain.bd.core.parse;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementBaseListener;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.core.parse.PostgreSQLTreeToStringListener
 * @createdDate 2019/6/12 16:20
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (shardingsphere)
 */
public class PostgreSQLTreeToStringListener extends PostgreSQLStatementBaseListener {
    private static final String SPACE = " ";
    @Getter
    @Setter
    private StringBuilder builder;
    public PostgreSQLTreeToStringListener(){}
    public PostgreSQLTreeToStringListener(StringBuilder builder){this.builder=builder;}
    @Override
    public void visitTerminal(TerminalNode node){
        builder.append(SPACE).append(node.getText());
    }
    @Override
    public void visitErrorNode(ErrorNode node){
        String errorText = node.getText();
        if(errorText.startsWith("<")) return;
        builder.append(SPACE).append(node.getText());
    }
}
