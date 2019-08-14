package com.kingbogo.superplayer.model;

/**
 * <p>
 * 播放器状态<br>
 * 缓冲中 -> 准备中 -> 已缓冲 -> 已准备 -> 播放中 -> 已完成 <br>
 * 缓冲中 -> 已缓冲 <br>
 * </p>
 *
 * @author Kingbo
 * @date 2019/8/12
 */
public enum SuperPlayerState {

    ERROR(-1, "错误"),
    IDLE(0, "空闲"),
    PREPARING(1, "准备中"),
    PREPARED(2, "已准备"),
    PLAYING(3, "播放中"),
    PAUSED(4, "暂停"),
    COMPLETED(5, "已完成"),
    BUFFERING(6, "缓冲中"),
    BUFFERED(7, "已缓冲"),
    STOPPED(8, "停止");

    private int stateCode;
    private String stateName;

    SuperPlayerState(int stateCode, String stateName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public static SuperPlayerState valueOfStateCode(int stateCode) {
        SuperPlayerState[] playerStateArray = values();
        for (SuperPlayerState playerState : playerStateArray) {
            if (playerState.getStateCode() == stateCode) {
                return playerState;
            }
        }
        return null;
    }

    public int getStateCode() {
        return stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "stateCode=" + stateCode +
                ", stateName='" + stateName + '\'' +
                '}';
    }

}
