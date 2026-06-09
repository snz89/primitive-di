package io.github.snz.primitivedi.stub;

public class CycleServiceC {
    private final CycleServiceA serviceA;

    public CycleServiceC(CycleServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public CycleServiceA getServiceA() {
        return serviceA;
    }
}
