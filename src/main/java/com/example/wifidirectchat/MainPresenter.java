package com.example.wifidirectchat;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private Interactor interactor;
    private MainContract.View view;
    private AppCompatActivity context;

    public MainPresenter(MainContract.View view, DependencyInjector dependencyInjector, AppCompatActivity context) {
        this.interactor = dependencyInjector.interactor();
        this.view = view;
        this.context = context;
    }

    @Override
    public List<Chat> getChats() {
        return interactor.getChats(context);
    }

    @Override
    public void clearHistory() {
        interactor.clearHistory(context);
    }

    @Override
    public void deleteChat(Chat chat) {
        interactor.deleteChat(context, chat);
    }

    @Override
    public void insertChat(Chat chat) {
        interactor.insertChat(context, chat);
    }

    @Override
    public Chat getChat(String name) {
        return interactor.getChat(context, name);
    }
}
