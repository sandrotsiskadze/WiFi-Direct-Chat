package com.example.wifidirectchat;

public class DependencyInjectorImpl implements DependencyInjector {
    @Override
    public Interactor interactor() {
        return InteractorImpl.getInstance();
    }
}
