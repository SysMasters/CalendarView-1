package com.haibin.calendarview.listener;

import com.haibin.calendarview.Calendar;

/**
 * 日历范围选择事件
 */
public interface OnCalendarRangeSelectListener {

    /**
     * 范围选择超出范围越界
     *
     * @param calendar calendar
     */
    void onCalendarSelectOutOfRange(Calendar calendar);

    /**
     * 选择范围超出范围
     *
     * @param calendar        calendar
     * @param isOutOfMinRange 是否小于最小范围，否则为最大范围
     */
    void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange);

    /**
     * 日期选择事件
     *
     * @param calendar calendar
     * @param isEnd    是否结束
     */
    void onCalendarRangeSelect(Calendar calendar, boolean isEnd);
}

