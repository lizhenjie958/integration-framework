package com.jie.verify.spring.importe;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: lizhenjie
 * @date:2025/4/17
 */
@Import(value = VerifyImportSelector.class)
@Configuration
public class VerifyImportSelectConfiguration {
}
