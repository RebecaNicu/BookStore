package com.nagarro.advanced.framework.spel;

import com.nagarro.advanced.framework.persistence.entity.Book;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionParser {

    public String parseAuthor(Book book) {
        org.springframework.expression.ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("author");

        EvaluationContext context = new StandardEvaluationContext(book);
        return (String) expression.getValue(context);
    }
}
