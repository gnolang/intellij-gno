package com.github.intellij.gno.language;

import com.github.intellij.gno.services.GnoFileType;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.github.intellij.gno.language.psi.GnoFile;
import com.github.intellij.gno.language.psi.GnoProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GnoUtil {
    /**
     * Searches the entire project for Simple language files with instances of the Simple property with the given key.
     *
     * @param project current project
     * @param key     to check
     * @return matching properties
     */
    public static List<GnoProperty> findProperties(Project project, String key) {
        List<GnoProperty> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(GnoFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            GnoFile gnoFile = (GnoFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (gnoFile != null) {
                GnoProperty[] properties = PsiTreeUtil.getChildrenOfType(gnoFile, GnoProperty.class);
                if (properties != null) {
                    for (GnoProperty property : properties) {
                        if (key.equals(property.getKey())) {
                            result.add(property);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<GnoProperty> findProperties(Project project) {
        List<GnoProperty> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(GnoFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            GnoFile gnoFile = (GnoFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (gnoFile != null) {
                GnoProperty[] properties = PsiTreeUtil.getChildrenOfType(gnoFile, GnoProperty.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

    /**
     * Attempts to collect any comment elements above the Simple key/value pair.
     */
    public static @NotNull String findDocumentationComment(GnoProperty property) {
        List<String> result = new LinkedList<>();
        PsiElement element = property.getPrevSibling();
        while (element instanceof PsiComment || element instanceof PsiWhiteSpace) {
            if (element instanceof PsiComment) {
                String commentText = element.getText().replaceFirst("[!# ]+", "");
                result.add(commentText);
            }
            element = element.getPrevSibling();
        }
        return StringUtil.join(Lists.reverse(result), "\n ");
    }
}
