package com.haibin.calendarview.listener;


import com.haibin.calendarview.Calendar;

import java.util.List;

/**
 * 周视图切换事件
 */
public interface OnWeekChangeListener {
    void onWeekChange(List<Calendar> weekCalendars);
}
