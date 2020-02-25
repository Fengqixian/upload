package com.clinbrain.bd.core.parse.util;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang3.StringUtils;
import com.clinbrain.bd.core.parse.PostgreSQLTreeToStringListener;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementLexer;
import com.clinbrain.bd.core.parse.autogen.PostgreSQLStatementParser;

public class PostgreSQLParseUtil {
    public static String tree2Sql(ParseTree tree){
        ParseTreeListener listener = new PostgreSQLTreeToStringListener(new StringBuilder());
        ParseTreeWalker.DEFAULT.walkLMR(listener,tree);
        return ((PostgreSQLTreeToStringListener) listener).getBuilder().toString();
    }
    public static PostgreSQLStatementParser.ExprContext createExpressionContext(String expr){
        CharStream input = CharStreams.fromString(expr.toUpperCase());
        PostgreSQLStatementLexer lexer=new PostgreSQLStatementLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostgreSQLStatementParser parser = new PostgreSQLStatementParser(tokens);
        return parser.expr();
    }
    public static PostgreSQLStatementParser.ExprContext createOrAppendExpressionContext(PostgreSQLStatementParser.ExprContext expressionContext,String expr){
        CharStream input = CharStreams.fromString(expr);
        PostgreSQLStatementLexer lexer=new PostgreSQLStatementLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostgreSQLStatementParser parser = new PostgreSQLStatementParser(tokens);
        //如果没有where条件
        if(expressionContext == null){
            return parser.expr();
        }else if(StringUtils.isNotBlank(expr)){
            PostgreSQLStatementParser.LogicalOperatorContext operatorContext = new PostgreSQLStatementParser.LogicalOperatorContext(expressionContext,0);
            operatorContext.addAnyChild(new TerminalNodeImpl(new CommonToken(0,"AND")));
            expressionContext.addChild(operatorContext);
            expressionContext.addChild(createExpressionContext(expr));
            return expressionContext;
        }else{
            return null;
        }
    }
}