package com.github.intellij.gno.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class GnoVisitor extends PsiElementVisitor {
    public void visitProperty(@NotNull GnoProperty o) {
        visitNamedElement(o);
    }

    public void visitNamedElement(@NotNull GnoNamedElement o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }
}
