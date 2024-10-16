package com.github.intellij.gno.language.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.github.intellij.gno.services.GnoFileType;
import com.github.intellij.gno.services.GnoLanguage;
import org.jetbrains.annotations.NotNull;

public class GnoFile extends PsiFileBase  {
    public GnoFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, GnoLanguage.INSTANCE);
    }

    protected GnoFile(@NotNull FileViewProvider viewProvider, @NotNull Language language) {
        super(viewProvider, language);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return GnoFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Gno File";
    }
}
