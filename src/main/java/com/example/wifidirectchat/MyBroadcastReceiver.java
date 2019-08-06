package com.example.wifidirectchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private ChatActivity chatActivity;

    public MyBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ChatActivity chatActivity) {
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.chatActivity = chatActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)) {

        } else if (action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)) {
            if (wifiP2pManager != null)
                wifiP2pManager.requestPeers(channel, chatActivity.peerListListener);
        } else if (action.equals(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)) {
            if (wifiP2pManager == null) return;
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                wifiP2pManager.requestConnectionInfo(channel, chatActivity.connectionInfoListener);
                chatActivity.setConnected(true);
            } else chatActivity.setConnected(false);
        } else if (action.equals(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)) {

        }
    }
}
