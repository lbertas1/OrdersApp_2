package bartos.lukasz.service.extensions;

import bartos.lukasz.service.OrderService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ExtensionForOrders implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(OrderService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new OrderService("C:\\Users\\Łukasz\\Desktop\\java-022-cdi-master\\03_StreamsNew\\03_ParentModule\\service\\src\\test\\resources\\orders.json");
    }
}
