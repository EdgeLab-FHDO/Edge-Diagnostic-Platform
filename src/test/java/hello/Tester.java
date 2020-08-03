package hello;

import org.junit.Assert;
import org.junit.Test;

public class Tester {
    SimpleApp app = new SimpleApp();
    @Test
    public void NullTest(){
        Assert.assertNotNull(app.sayHello());
    }
    @Test
    public void ContentTest() {
        Assert.assertEquals("Wrong Message","Hello World!", app.sayHello());
    }
}
