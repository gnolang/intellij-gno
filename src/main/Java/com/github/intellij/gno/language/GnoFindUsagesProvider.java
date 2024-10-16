package com.github.intellij.gno.language;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.github.intellij.gno.language.psi.GnoProperty;
import com.github.intellij.gno.language.psi.GnoTokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GnoFindUsagesProvider implements FindUsagesProvider {
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new GnoLexerAdapter(),
                GnoTokenSet.IDENTIFIERS,
                GnoTokenSet.COMMENTS,
                TokenSet.EMPTY);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof GnoProperty) {
            return "simple property";
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof GnoProperty) {
            return ((GnoProperty) element).getKey();
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof GnoProperty) {
            return ((GnoProperty) element).getKey() +
                    GnoAnnotator.SIMPLE_SEPARATOR_STR +
                    ((GnoProperty) element).getValue();
        }
        return "";
    }
}
