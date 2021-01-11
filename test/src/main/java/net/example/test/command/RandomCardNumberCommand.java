package net.example.test.command;

import org.jbehavesupport.core.expression.ExpressionCommand;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.springframework.util.Assert.isTrue;

@Component
public class RandomCardNumberCommand implements ExpressionCommand {

    private Random random = new Random();

    @Override
    public String execute(Object... params) {
        isTrue(params.length == 0, "None parameter is expected");

        String numStr;
        do {
            StringBuilder stringBuilder = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                int n = random.nextInt(10);
                stringBuilder.append(n);
            }
            numStr = stringBuilder.toString();
        } while (!validateCardNumber(numStr));
        return numStr;
    }

    private boolean validateCardNumber(String str) {
        int[] ints = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        if (sum % 10 != 0) {
            return false;
        }
        return true;
    }
}
