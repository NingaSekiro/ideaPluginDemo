package org.example.demo;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.rename.RenameProcessor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConverterAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        VirtualFile resourcesDir = e.getData(CommonDataKeys.VIRTUAL_FILE);
        assert resourcesDir != null;
        VirtualFile parent = resourcesDir.getParent();
        assert psiFile != null;
        Project project = e.getProject();
        assert project != null;
        String content = psiFile.getText();
        PsiDirectory directory = PsiManager.getInstance(project).findDirectory(parent);
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiFile newFile = factory.createFileFromText("T"+psiFile.getName(), JavaLanguage.INSTANCE, content);
        PsiJavaFile javaFile = (PsiJavaFile) newFile;
        // 遍历文件中的类
        PsiClass psiClass = javaFile.getClasses()[0];
        String oldClassName = psiClass.getName();
        // 创建RenameProcessor进行类名的修改
        RenameProcessor renameClassProcessor = new RenameProcessor(project, psiClass, "T"+oldClassName, true, false);
        renameClassProcessor.run();
        WriteCommandAction.runWriteCommandAction(project, () -> {
            List<PsiField> fieldsToReplace = new ArrayList<>();
            newFile.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitField(@NotNull PsiField field) {
                    fieldsToReplace.add(field);
                }
            });
            for (PsiField field : fieldsToReplace) {
                field.setName(field.getName().toUpperCase());
            }
            if (directory != null) directory.add(newFile);
        });
    }
}
