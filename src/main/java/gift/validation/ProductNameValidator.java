package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, String> {
    private static final String ALLOWED_CHARS_PATTERN = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$";

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null)
            return true;
        if (name.length() > 15) {
            updateMessage(context, "상품 이름은 공백 포함 최대 15자까지 입력가능합니다.");
            return false;
        }
        if (name.contains("카카오")) {
            updateMessage(context, "'카카오'가 포함된 문구는 담당 MD와 협의해야 합니다.");
            return false;
        }
        if (!Pattern.matches(ALLOWED_CHARS_PATTERN, name)) {
            updateMessage(context, "허용되지 않는 특수문자가 포함되어 있습니다. (사용 가능: ( ) [ ] + - & / _ )");
            return false;
        }
        return true;
    }

    private void updateMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
