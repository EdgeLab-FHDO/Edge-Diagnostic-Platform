package InfrastructureManager.Modules;

import org.junit.Assert;
import org.junit.function.ThrowingRunnable;

public class CommonTestingMethods {

    public static void assertException(Class<? extends  Throwable> exceptionClass, String expectedMessage, ThrowingRunnable function) {
        var e = Assert.assertThrows(exceptionClass, function);
        Assert.assertEquals(expectedMessage, e.getMessage());
    }

}
