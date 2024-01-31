package damon.backend.util.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메서드에 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점에 결정되도록 한다
public @interface Trace {
}
