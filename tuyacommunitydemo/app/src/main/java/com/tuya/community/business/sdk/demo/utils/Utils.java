package com.tuya.community.business.sdk.demo.utils;

public class Utils {

    public static long mCurrentHouseId = 0;
    public static String currentCommunityId = "";
    public static String currentRoomId = "";

    public static String getCurrentCommunityId() {
        return currentCommunityId;
    }

    public static void setCurrentCommunityId(String currentCommunityId) {
        Utils.currentCommunityId = currentCommunityId;
    }

    public static String getCurrentRoomId() {
        return currentRoomId;
    }

    public static void setCurrentRoomId(String currentRoomId) {
        Utils.currentRoomId = currentRoomId;
    }

    public static void setCurrentHouseId(long homeId){
        mCurrentHouseId = homeId;
    }
    public static long getHouseId(){
        return mCurrentHouseId;
    }
}
