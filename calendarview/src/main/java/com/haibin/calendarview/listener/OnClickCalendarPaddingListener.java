package com.haibin.calendarview.listener;

import com.haibin.calendarview.Calendar;

/**
 * 点击Padding位置事件
 */
public interface OnClickCalendarPaddingListener {
    /**
     * 点击Padding位置的事件
     *
     * @param x                x坐标
     * @param y                y坐标
     * @param isMonthView      是否是月视图，不是则为周视图
     * @param adjacentCalendar 相邻的日历日期
     * @param obj              此处的对象，自行设置
     */
    void onClickCalendarPadding(float x, float y, boolean isMonthView,
                                Calendar adjacentCalendar, Object obj);
}