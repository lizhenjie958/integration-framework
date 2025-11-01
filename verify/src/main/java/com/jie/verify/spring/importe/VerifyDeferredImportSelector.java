package com.jie.verify.spring.importe;

import com.jie.verify.spring.enable.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Slf4j
public class VerifyDeferredImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        log.info("VerifyDeferredImportSelector selectImports invoked...");
        return new String[]{Customer.class.getName()};
    }
}
