package com.example.wifidirectchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ConnectItemAdapter.OnConnectItemClickListener, MainContract.View {
    static final int MESSAGE_READ = 1;
    private MainContract.Presenter presenter;
    private ServerClass serverClass;
    private ClientClass clientClass;
    private Toolbar toolbar;
    private EditText chat_editText;
    private ChatItemAdapter chatItemAdapter;
    private List<com.example.wifidirectchat.Message> messageList;
    private String deviceName;
    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                enterChat();
                serverClass = new ServerClass();
                serverClass.start();
            } else if (wifiP2pInfo.groupFormed) {
                enterChat();
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };
    private boolean connected = false;
    private RecyclerView connect_recyclerView;
    private SendReceive sendReceive;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    private List<String> deviceNameArray;
    private List<WifiP2pDevice> deviceArray;
    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if (!wifiP2pDeviceList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());

                deviceNameArray = new ArrayList<>();
                deviceArray = new ArrayList<>();

                for (int i = 0; i < peers.size(); i++) {
                    deviceNameArray.add(peers.get(i).deviceName);
                    deviceArray.add(peers.get(i));
                }
            }
            if (peers.isEmpty()) return;
            else {
                setContentView(R.layout.content_connect);
                ConnectItemAdapter connectItemAdapter = new ConnectItemAdapter(deviceNameArray, ChatActivity.this);
                connect_recyclerView = findViewById(R.id.connect_recyclerView);
                connect_recyclerView.setAdapter(connectItemAdapter);
                connect_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(connect_recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                connect_recyclerView.addItemDecoration(dividerItemDecoration);
                deviceName = deviceNameArray.get(0);
            }
        }
    };
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) message.obj;
                    String tempMsg = new String(readBuff, 0, message.arg1);
                    com.example.wifidirectchat.Message msg = new com.example.wifidirectchat.Message(1, tempMsg);
                    chatItemAdapter.insert(msg);
                    presenter.insertChat(new Chat(deviceName, Calendar.getInstance().getTime().toString(), messageList));
                    break;
            }
            return true;
        }
    });

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    private void enterChat() {
        setContentView(R.layout.app_bar_chat);
        toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        chat_editText = findViewById(R.id.chat_editText);
        Chat chat = presenter.getChat(deviceName);
        if (chat == null) messageList = new ArrayList<>();
        else messageList = chat.getMessageList();
        chatItemAdapter = new ChatItemAdapter(messageList);
        RecyclerView recyclerView = findViewById(R.id.chat_recyclerView);
        recyclerView.setAdapter(chatItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toolbar.setTitle(deviceName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void discover() {
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    private void connectToPeer(int i) {
        final WifiP2pDevice wifiP2pDevice = deviceArray.get(i);
        deviceName = deviceNameArray.get(i);
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;

        wifiP2pManager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    public void sendMsg(View view) {
        if (isConnected()) {
            sendReceive.write(chat_editText.getText().toString().getBytes());
            com.example.wifidirectchat.Message message = new com.example.wifidirectchat.Message(0, chat_editText.getText().toString());
            chatItemAdapter.insert(message);
            presenter.insertChat(new Chat(deviceName, Calendar.getInstance().getTime().toString(), messageList));
            chat_editText.setText("");
        } else Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.content_loading);

        presenter = new MainPresenter(this, new DependencyInjectorImpl(), this);

        Chat chat = presenter.getChat(deviceName);
        if (chat == null) messageList = new ArrayList<>();
        else messageList = chat.getMessageList();

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);

        broadcastReceiver = new MyBroadcastReceiver(wifiP2pManager, channel, this);
        intentFilter = new IntentFilter();

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        registerReceiver(broadcastReceiver, intentFilter);

        discover();
    }

    private void disconnect() {
        if (wifiP2pManager != null && channel != null) {
            wifiP2pManager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && wifiP2pManager != null && channel != null
                            && group.isGroupOwner()) {
                        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(int reason) {
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        disconnect();
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onConnectItemClick(int position) {
        connectToPeer(position);
    }

    private static class Send extends AsyncTask<SendReceive.ToSend, Void, Void> {

        @Override
        protected Void doInBackground(SendReceive.ToSend... toSends) {
            try {
                toSends[0].getOutputStream().write(toSends[0].getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ServerClass extends Thread {
        private Socket socket;
        private ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket socket) {
            this.socket = socket;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (socket != null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0)
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean write(byte[] bytes) {
            Send send = new Send();
            send.execute(new ToSend(outputStream, bytes));
            return false;
        }

        private class ToSend {
            private OutputStream outputStream;
            private byte[] bytes;

            public ToSend(OutputStream outputStream, byte[] bytes) {
                this.outputStream = outputStream;
                this.bytes = bytes;
            }

            public OutputStream getOutputStream() {
                return outputStream;
            }

            public void setOutputStream(OutputStream outputStream) {
                this.outputStream = outputStream;
            }

            public byte[] getBytes() {
                return bytes;
            }

            public void setBytes(byte[] bytes) {
                this.bytes = bytes;
            }
        }
    }

    public class ClientClass extends Thread {
        private Socket socket;
        private String hostAddress;

        public ClientClass(InetAddress hostAddress) {
            this.hostAddress = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress, 8888), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
