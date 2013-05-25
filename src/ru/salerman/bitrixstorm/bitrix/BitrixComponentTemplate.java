package ru.salerman.bitrixstorm.bitrix;/*
 * Copyright 2011-2013 Salerman <www.salerman.ru>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import static java.io.File.separator;

/**
 * @author Mikhail Medvedev aka r3c130n <mm@salerman.ru>
 * @link http://www.salerman.ru/
 * @date: 20.05.13
 */
public class BitrixComponentTemplate {
    public String name, description, path;
    public boolean isWithParameters = false;
    public boolean isWithResultModifier = false;
    public boolean isWithComponentEpilog = false;
    public boolean isWithLangFiles = false;
    public boolean isWithStyle = false;
    public boolean isWithScript = false;
    protected PsiElement psiTemplate = null;

    protected BitrixComponentTemplate () {

    }

    public BitrixComponentTemplate(BitrixComponent component) {
        this.psiTemplate = findComponentTemplate(component);
        if (this.psiTemplate == null) {
            return;
        }
        this.path = BitrixUtils.getFileNameByPsiElement(psiTemplate);
    }

    public PsiElement toPsiFile () {
        return this.psiTemplate;
    }

    public static PsiElement findComponentTemplate(BitrixComponent component) {
        PsiElement tpl;
        String[] order = getComponentTemplatesPathOrder(component.getNamespace(), component.getName(), component.getTemplateName(), component.getProject());

        if (order != null) {
            for (String path : order) {
                tpl = BitrixUtils.getPsiFileByPath(component.getProject(), path);
                if (tpl != null) {
                    return tpl;
                }
            }
        }

        return null;
    }

    public static String[] getComponentTemplatesPathOrder(String componentNameSpace, String componentName, String templateName, Project project) {
        String sep = BitrixUtils.getEscapedSeparator();
        String[] order;
        if (templateName == "") {
            templateName = ".default";
        }
        int i = 0;
        VirtualFile context = BitrixUtils.getContext(project);
        if (context != null) {
            order = new String[4];

            String path = context.getPath().replace(context.getName(), "");
            order[i++]  = path
                        + componentNameSpace
                        + sep + componentName
                        + sep + templateName
                        + sep + "template.php";
        } else {
            order = new String[3];
        }

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.getInstance(project).BITRIX_SITE_TEMPLATES_PATH
                + BitrixSiteTemplate.getInstance(project).getName()
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.getInstance(project).BITRIX_SITE_TEMPLATES_PATH
                + ".default"
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + templateName
                + sep + "template.php";

        order[i++]    = project.getBasePath()
                + BitrixSiteTemplate.getInstance(project).BITRIX_ROOT_PATH
                + sep + "components"
                + sep + componentNameSpace
                + sep + componentName
                + sep + "templates"
                + sep + templateName
                + sep + "template.php";

        return order;
    }

    /*
    public ResolveResult[] getResolveList() {
        return new ResolveResult[0];
    }
    */
}
