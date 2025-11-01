package com.jie.verify.spring.importe;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: lizhenjie
 * @date:2025/4/17
 */
public class VerifyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{Guitar.class.getName(),Singer.class.getName()};
    }
}
