package com.haibin.calendarview.listener;


import com.haibin.calendarview.Calendar;

/**
 * 拦截日期是否可用事件
 */
public interface OnCalendarInterceptListener {
    boolean onCalendarIntercept(Calendar calendar);

    void onCalendarInterceptClick(Calendar calendar, boolean isClick);
}