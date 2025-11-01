package com.jie.verify.spring.enable;

import com.jie.verify.spring.importe.VerifyDeferredImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: lizhenjie
 * @date:2025/3/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(
{       Boss.class,
        BarImportSelector.class,
        BartenderConfiguration.class,
        VerifyDeferredImportSelector.class,
        WaiterRegistrar.class
}) // 将老板导入，酒保导入，酒吧导入
public @interface EnableTavern {
}
