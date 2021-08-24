package com.haibin.calendarview.listener;


import com.haibin.calendarview.Calendar;

/**
 * 日历多选事件
 */
public interface OnCalendarMultiSelectListener {

    /**
     * 多选超出范围越界
     *
     * @param calendar calendar
     */
    void onCalendarMultiSelectOutOfRange(Calendar calendar);

    /**
     * 多选超出大小
     *
     * @param maxSize  最大大小
     * @param calendar calendar
     */
    void onMultiSelectOutOfSize(Calendar calendar, int maxSize);

    /**
     * 多选事件
     *
     * @param calendar calendar
     * @param curSize  curSize
     * @param maxSize  maxSize
     */
    void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize);
}
