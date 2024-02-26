package com.roukaixin.validation.annotation;

import com.roukaixin.validation.PathValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 校验文件路径是否正确
 *
 * @author 不北咪
 * @date 2024/2/26 下午4:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PathValidator.class)
public @interface Path {

    String message() default "路径格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
