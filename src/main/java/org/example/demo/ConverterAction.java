package org.example.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConverterAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        assert psiFile != null;
        WriteCommandAction.runWriteCommandAction(psiFile.getProject(), () -> {
            List<PsiField> fieldsToReplace = new ArrayList<>();

            psiFile.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitField(@NotNull PsiField field) {
                    fieldsToReplace.add(field);
                }
            });



            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiFile.getProject());
            for (PsiField field : fieldsToReplace) {
                field.setName(field.getName().toUpperCase());
            }
        });

//        psiFile.accept(new JavaRecursiveElementVisitor() {
//            @Override
//            public void visitField(@NotNull PsiField field) {
//                System.out.println(field.getName());
//            }
//        });
    }
}
