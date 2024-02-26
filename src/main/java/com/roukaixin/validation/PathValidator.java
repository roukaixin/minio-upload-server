package com.roukaixin.validation;

import com.roukaixin.utils.UploadUtils;
import com.roukaixin.validation.annotation.Path;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注解 path 的校验实现
 *
 * @author 不北咪
 * @date 2024/2/26 下午4:27
 */
public class PathValidator implements ConstraintValidator<Path, String> {

    private final String WINDOWS_PATTERN = "^([a-zA-Z]:\\\\)?[^/\0<>:\"|?*]+(\\\\[^/\0<>:\"|?*]+)*\\\\?$";
    private final String LINUX_PATTERN = "^[/a-zA-Z0-9_]+$";

    private final Pattern windows_pattern = Pattern.compile(WINDOWS_PATTERN);
    private final Pattern linux_pattern = Pattern.compile(LINUX_PATTERN);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        switch (UploadUtils.getOsName()) {
            case LINUX -> {
                Matcher matcher = linux_pattern.matcher(s);
                return matcher.matches();
            }
            case WINDOWS -> {
                Matcher matcher = windows_pattern.matcher(s);
                return matcher.matches();
            }
            default -> {
                return false;
            }
        }
    }
}
