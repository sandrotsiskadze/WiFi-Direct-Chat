package com.example.wifidirectchat;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InteractorImpl implements Interactor {
    private static final Object lock = new Object();
    private static InteractorImpl instance;

    public InteractorImpl() {

    }

    public static InteractorImpl getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new InteractorImpl();
        }
        return instance;
    }

    @Override
    public List<Chat> getChats(Context context) {
        List<Chat> chatList = new ArrayList<>();
        GetChats getChats = new GetChats();
        try {
            chatList = getChats.execute(context).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chatList;
    }

    @Override
    public void clearHistory(Context context) {
        ClearHistory clearHistory = new ClearHistory();
        clearHistory.execute(context);
    }

    @Override
    public void deleteChat(Context context, Chat chat) {
        DeleteChat deleteChat = new DeleteChat();
        deleteChat.execute(new DeleteChatParams(context, chat));
    }

    @Override
    public void insertChat(Context context, Chat chat) {
        InsertChat insertChat = new InsertChat();
        insertChat.execute(new InsertChatParams(context, chat));
    }

    @Override
    public Chat getChat(Context context, String name) {
        Chat chat = new Chat("", "", new ArrayList<Message>());
        GetChat getChat = new GetChat();
        try {
            chat = getChat.execute(new GetChatParams(context, name)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chat;
    }

    private static class GetChat extends AsyncTask<GetChatParams, Void, Chat> {

        @Override
        protected Chat doInBackground(GetChatParams... getChatParams) {
            return Database.getInstance(getChatParams[0].getContext()).dao().getChat(getChatParams[0].getName());
        }
    }

    private static class InsertChat extends AsyncTask<InsertChatParams, Void, Void> {

        @Override
        protected Void doInBackground(InsertChatParams... insertChatParams) {
            Database.getInstance(insertChatParams[0].getContext()).dao().insertChat(insertChatParams[0].getChat());
            return null;
        }
    }

    private static class GetChats extends AsyncTask<Context, Void, List<Chat>> {

        @Override
        protected List<Chat> doInBackground(Context... contexts) {
            return Database.getInstance(contexts[0]).dao().getChatList();
        }
    }

    private static class ClearHistory extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... contexts) {
            Database.getInstance(contexts[0]).dao().clearHistory();
            return null;
        }
    }

    private static class DeleteChat extends AsyncTask<DeleteChatParams, Void, Void> {

        @Override
        protected Void doInBackground(DeleteChatParams... deleteChatParams) {
            Database.getInstance(deleteChatParams[0].getContext()).dao().deleteChat(deleteChatParams[0].getChat());
            return null;
        }
    }

    private class GetChatParams {
        private Context context;
        private String name;

        public GetChatParams(Context context, String name) {
            this.context = context;
            this.name = name;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class DeleteChatParams {
        private Context context;
        private Chat chat;

        public DeleteChatParams(Context context, Chat chat) {
            this.context = context;
            this.chat = chat;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }
    }

    private class InsertChatParams {
        private Context context;
        private Chat chat;

        public InsertChatParams(Context context, Chat chat) {
            this.context = context;
            this.chat = chat;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }
    }
}
