package org.jbehavesupport.core.internal.verification;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.jbehavesupport.core.internal.util.ArrayComparator.arraysEquals;
import static org.jbehavesupport.core.verification.VerifierNames.EQ;
import static org.springframework.util.Assert.notNull;

@Component
public final class EqualsVerifier extends AbstractVerifier {

    private static final String MESSAGE = "value '%s' is not equal to '%s'";

    @Override
    public String name() {
        return EQ;
    }

    @Override
    public void verify(final Object actual, final Object expected) {
        if (actual == expected) {
            return;
        }
        notNull(actual, "Actual value must be provided");

        if (actual.equals(expected)) {
            return;
        }

        if (actual.getClass().isArray() && expected != null && expected.getClass().isArray() && arraysEquals(actual, expected)) {
            return;
        }

        if (actual.toString().equals(String.valueOf(expected))) {
            return;
        }
        if(actual instanceof BigDecimal && expected instanceof String &&
                ((BigDecimal)actual).compareTo(new BigDecimal((String)expected)) == 0){
            return;
        }
        throwAssertionError(MESSAGE, actual, expected);
    }

}
