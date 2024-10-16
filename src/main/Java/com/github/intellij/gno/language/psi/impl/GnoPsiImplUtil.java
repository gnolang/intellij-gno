package com.github.intellij.gno.language.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.github.intellij.gno.language.psi.GnoElementFactory;
import com.github.intellij.gno.language.psi.GnoProperty;
import com.github.intellij.gno.language.psi.GnoTypes;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GnoPsiImplUtil {
    public static String getKey(GnoProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(GnoTypes.KEY);
        if (keyNode != null) {
            // IMPORTANT: Convert embedded escaped spaces to simple spaces
            return keyNode.getText().replaceAll("\\\\ ", " ");
        } else {
            return null;
        }
    }

    public static String getValue(GnoProperty element) {
        ASTNode valueNode = element.getNode().findChildByType(GnoTypes.VALUE);
        if (valueNode != null) {
            return valueNode.getText();
        } else {
            return null;
        }
    }

    public static String getName(GnoProperty element) {
        return getKey(element);
    }

    public static PsiElement setName(GnoProperty element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(GnoTypes.KEY);
        if (keyNode != null) {
            GnoProperty property = GnoElementFactory.createProperty(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement getNameIdentifier(GnoProperty element) {
        ASTNode keyNode = element.getNode().findChildByType(GnoTypes.KEY);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static ItemPresentation getPresentation(final GnoProperty element) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return element.getKey();
            }

            @Nullable
            @Override
            public String getLocationString() {
                PsiFile containingFile = element.getContainingFile();
                return containingFile == null ? null : containingFile.getName();
            }

            @Override
            public Icon getIcon(boolean unused) {
                return element.getIcon(0);
            }
        };
    }
}
