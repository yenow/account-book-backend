//package com.ysy.accountbook.global.config.p6y;
//
////import com.p6spy.engine.logging.Category;
////import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
//import org.hibernate.engine.jdbc.internal.FormatStyle;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@SuppressWarnings("ALL")
//public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {
//    // 표기에 허용되지 않는 filter
//    private final List<String> DENIED_FILTER = Arrays.asList("<generated>", "Filter", "Controller", this.getClass().getSimpleName());
//    // 표기에 허용되는 filter
//    private final String ALLOW_FILTER = "com.ysy.accountbook";
//
//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
//        sql = formatSql(category, sql);
//        Date currentDate = new Date();
//
//        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
//
//        //return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;
//        return format1.format(currentDate) + " | "+ "OperationTime : "+ elapsed + "ms | " + category
//                + createStack(connectionId, elapsed)
//                + sql;
//    }
//
//    private String createStack(int connectionId, long elapsed) {
//        Stack<String> callStack = new Stack<>();
//        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
//
//        for (StackTraceElement stackTraceElement : stackTrace) {
//            String trace = stackTraceElement.toString();
//
//            // trace 항목을 보고 내게 맞는 것만 필터
//            if(trace.startsWith(ALLOW_FILTER) && !containDenied(trace)) {
//                callStack.push(trace);
//            }
//        }
//
//        StringBuffer sb = new StringBuffer();
//        int order = 1;
//        while (callStack.size() != 0) {
//            sb.append("\n").append(callStack.pop());
//        }
//
//        return sb.toString();
//    }
//
//    boolean containDenied(String trace) {
//        for (String deniedString : DENIED_FILTER) {
//            if (trace.contains(deniedString)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private String formatSql(String category,String sql) {
//        if(sql == null || sql.trim().equals("")) return sql;
//
//        // Only format Statement, distinguish DDL And DML
//        if (Category.STATEMENT.getName().equals(category)) {
//            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
//            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
//                sql = FormatStyle.DDL.getFormatter().format(sql);
//            }else {
//                sql = FormatStyle.BASIC.getFormatter().format(sql);
//            }
//            sql = " |\nHeFormatSql(P6Spy sql,Hibernate format):"+ sql;
//        }
//
//        return sql;
//    }
//}