package io.github.snz.primitivedi.stub;

public class CycleServiceB {
    private final CycleServiceC serviceC;

    public CycleServiceB(CycleServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public CycleServiceC getServiceC() {
        return serviceC;
    }
}
