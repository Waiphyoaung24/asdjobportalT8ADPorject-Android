package com.example.myapplication.delegates;

public interface ReviewItemDelegate {
    void onTapSendMessage(String userId,String userName);
    void onTapReport(Long reviewId,String userName);
}
