package com.haibin.calendarview.listener;

import com.haibin.calendarview.Calendar;

public interface OnCalendarLongClickListener {

    /**
     * 超出范围越界
     *
     * @param calendar calendar
     */
    void onCalendarLongClickOutOfRange(Calendar calendar);

    /**
     * 日期长按事件
     *
     * @param calendar calendar
     */
    void onCalendarLongClick(Calendar calendar);
}