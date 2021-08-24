package com.haibin.calendarview.listener;


import com.haibin.calendarview.Calendar;

/**
 * 内部日期选择，不暴露外部使用
 * 主要是用于更新日历CalendarLayout位置
 */
public interface OnInnerDateSelectedListener {
    /**
     * 月视图点击
     *
     * @param calendar calendar
     * @param isClick  是否是点击
     */
    void onMonthDateSelected(Calendar calendar, boolean isClick);

    /**
     * 周视图点击
     *
     * @param calendar calendar
     * @param isClick  是否是点击
     */
    void onWeekDateSelected(Calendar calendar, boolean isClick);
}