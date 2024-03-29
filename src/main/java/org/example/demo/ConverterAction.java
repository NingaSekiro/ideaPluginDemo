package org.example.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;

public class ConverterAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前项目
        Project project = e.getData(PlatformDataKeys.PROJECT);
        // 获取当前文件
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(e.getData(PlatformDataKeys.EDITOR), project);
        // 创建并注册访问器
        psiFile.accept(new MyFieldVisitor());
    }
}
