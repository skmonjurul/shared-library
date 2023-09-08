package com.skmonjurul.shared_library.autoconfigure.web.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;

public class NotReactiveWebApplicationCondition extends NoneNestedConditions {
    
    NotReactiveWebApplicationCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }
    
    
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    private static class ReactiveWebApplication {
    }
}
