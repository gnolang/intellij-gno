package com.github.intellij.gno.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.github.intellij.gno.language.psi.GnoNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class GnoNamedElementImpl extends ASTWrapperPsiElement implements GnoNamedElement {
    public GnoNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
