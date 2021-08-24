package com.haibin.calendarview.listener;

import com.haibin.calendarview.Calendar;

/**
 * 日历选择事件
 */
public interface OnCalendarSelectListener {

    /**
     * 超出范围越界
     *
     * @param calendar calendar
     */
    void onCalendarOutOfRange(Calendar calendar);

    /**
     * 日期选择事件
     *
     * @param calendar calendar
     * @param isClick  isClick
     */
    void onCalendarSelect(Calendar calendar, boolean isClick);
}
