package io.github.snz.primitivedi.stub;

public class CycleServiceA {
    private final CycleServiceB serviceB;

    public CycleServiceA(CycleServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public CycleServiceB getServiceB() {
        return serviceB;
    }
}
