package gift.validation;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

public class ProductNameValidatorTest {
    private ProductNameValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp()
    {
        validator = new ProductNameValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder= Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
    }

    @Test
    void testValidProductName()
    {
        String validName="상품(123)";
        boolean result=validator.isValid(validName, context);
        assertThat(result).isTrue();
    }

    @Test
    void testNameLengthExceeded()
    {
        String longName="이 상품의 이름은 15자를 훌쩍 넘습니다.";
        boolean result=validator.isValid(longName, context);
        assertThat(result).isFalse();
    }

    @Test
    void testNameMaxLength()
    {
        String maxName="123456789012345";
        boolean result=validator.isValid(maxName, context);
        assertThat(result).isTrue();
    }

    @Test
    void testForbiddenWordKaKao()
    {
        String forbiddenName="카카오프렌즈";
        boolean result=validator.isValid(forbiddenName, context);
        assertThat(result).isFalse();
    }
    @ParameterizedTest
    @ValueSource(strings = {"상품!", "상품#", "상품$", "상품*"})
    void testInvalidCharacters(String invalidName)
    {
        boolean result=validator.isValid(invalidName, context);
        assertThat(result).isFalse();
    }

}
