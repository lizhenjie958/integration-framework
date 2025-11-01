package com.jie.verify.spring.enable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Slf4j
public class BarImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        log.info("BarImportSelector selectImports  invoke...");
        return new String[]{Bar.class.getName(), BarConfiguration.class.getName()};
    }
}
