package com.github.intellij.gno.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.github.intellij.gno.language.psi.GnoProperty;
import com.github.intellij.gno.language.psi.GnoVisitor;
import org.jetbrains.annotations.NotNull;

public class GnoPropertyImpl extends GnoNamedElementImpl implements GnoProperty {

    public GnoPropertyImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull GnoVisitor visitor) {
        visitor.visitProperty(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof GnoVisitor) accept((GnoVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    public String getKey() {
        return GnoPsiImplUtil.getKey(this);
    }

    @Override
    public String getValue() {
        return GnoPsiImplUtil.getValue(this);
    }

    @Override
    public String getName() {
        return GnoPsiImplUtil.getName(this);
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        return GnoPsiImplUtil.setName(this, newName);
    }

    @Override
    public PsiElement getNameIdentifier() {
        return GnoPsiImplUtil.getNameIdentifier(this);
    }

    @Override
    public ItemPresentation getPresentation() {
        return GnoPsiImplUtil.getPresentation(this);
    }
}
