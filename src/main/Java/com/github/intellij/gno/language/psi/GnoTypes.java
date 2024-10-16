package com.github.intellij.gno.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.github.intellij.gno.language.psi.impl.*;

public interface GnoTypes {
    IElementType PROPERTY = new GnoElementType("PROPERTY");

    IElementType COMMENT = new GnoTokenType("COMMENT");
    IElementType CRLF = new GnoTokenType("CRLF");
    IElementType KEY = new GnoTokenType("KEY");
    IElementType SEPARATOR = new GnoTokenType("SEPARATOR");
    IElementType VALUE = new GnoTokenType("VALUE");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == PROPERTY) {
                return new GnoPropertyImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}

