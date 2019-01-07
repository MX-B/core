package io.gr1d.core.resolver;

import io.gr1d.core.exception.Gr1dConstraintException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

@Configuration
public class LocalDateArgResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this);
    }

    @Override
    public boolean supportsParameter(final MethodParameter methodParameter) {
        return LocalDate.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer modelAndViewContainer, final NativeWebRequest nativeWebRequest, final WebDataBinderFactory webDataBinderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        final String parameterName = LOWER_CAMEL.to(LOWER_UNDERSCORE, methodParameter.getParameterName());
        try {
            final String param = request.getParameter(parameterName);
            return Optional.ofNullable(param).map(LocalDate::parse).orElse(null);
        } catch (DateTimeParseException e) {
            throw new Gr1dConstraintException(parameterName, "io.gr1d.invalidFormat.message");
        }

    }

}
