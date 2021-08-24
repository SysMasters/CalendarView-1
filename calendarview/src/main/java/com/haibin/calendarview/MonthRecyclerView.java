package com.haibin.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.util.List;

public class MonthRecyclerView extends RecyclerView {

    private boolean isUpdateMonthView;

    private int mMonthCount;

    private CalendarViewDelegate mDelegate;

    private int mNextViewHeight, mPreViewHeight, mCurrentViewHeight;

    CalendarLayout mParentLayout;

    WeekViewPager mWeekPager;

    WeekBar mWeekBar;
    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    public MonthRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MonthRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;
        setAdapter(new MonthRecyclerViewAdapter());
        init();
    }

    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
    }


    private final class MonthRecyclerViewAdapter extends RecyclerView.Adapter<MonthRecyclerViewAdapter.ViewHolder> {


        @NonNull
        @Override
        public MonthRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthRecyclerViewAdapter.ViewHolder viewHolder, int position) {
            int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
            int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
            viewHolder.baseMonthView.setTag(position);
            viewHolder.baseMonthView.initMonthWithDate(year, month);
        }

        @Override
        public int getItemCount() {
            return mMonthCount;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private BaseMonthView baseMonthView;

            public ViewHolder(View itemView) {
                super(itemView);

                try {
                    Constructor constructor = mDelegate.getMonthViewClass().getConstructor(Context.class);
                    baseMonthView = (BaseMonthView) constructor.newInstance(itemView.getContext());
                } catch (Exception e) {
                    e.printStackTrace();
                    baseMonthView = new DefaultMonthView(itemView.getContext());
                }
                baseMonthView.mParentLayout = mParentLayout;
                baseMonthView.setup(mDelegate);
                baseMonthView.setSelectedCalendar(mDelegate.mSelectedCalendar);
            }
        }

    }


    /**
     * 刷新
     */
    void notifyDataSetChanged() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        notifyAdapterDataSetChanged();
    }

    /**
     * 更新月视图Class
     */
    void updateMonthViewClass() {
        isUpdateMonthView = true;
        notifyAdapterDataSetChanged();
        isUpdateMonthView = false;
    }

    /**
     * 更新日期范围
     */
    final void updateRange() {
        isUpdateMonthView = true;
        notifyDataSetChanged();
        isUpdateMonthView = false;
        if (getVisibility() != VISIBLE) {
            return;
        }
        isUsingScrollToCalendar = false;
        Calendar calendar = mDelegate.mSelectedCalendar;
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position, false);
        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        updateSelected();
    }

    /**
     * 滚动到指定日期
     *
     * @param year           年
     * @param month          月
     * @param day            日
     * @param invokeListener 调用日期事件
     */
    void scrollToCalendar(int year, int month, int day, boolean smoothScroll, boolean invokeListener) {
        isUsingScrollToCalendar = true;
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setCurrentDay(calendar.equals(mDelegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        mDelegate.mIndexCalendar = calendar;
        mDelegate.mSelectedCalendar = calendar;
        mDelegate.updateSelectCalendarScheme();
        int y = calendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendar.getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position, smoothScroll);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }

        if (mDelegate.mCalendarSelectListener != null && invokeListener) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendar, false);
        }
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendar, false);
        }

        updateSelected();
    }

    /**
     * 滚动到当前日期
     */
    void scrollToCurrent(boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();

        setCurrentItem(position, smoothScroll);

        BaseMonthView view = findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
            }
        }

        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendar, false);
        }
    }


    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新字体颜色大小
     */
    final void updateStyle() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateStyle();
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateCurrentDate();
        }
    }


    /**
     * 更新周起始
     */
    void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateWeekStart();
            view.requestLayout();
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.mSelectedCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }
        updateSelected();
    }

    /**
     * 清除选择范围
     */
    final void clearSelectRange() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearSingleSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    /**
     * 清除单选选择
     */
    final void clearMultiSelect() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.mCurrentItem = -1;
            view.invalidate();
        }
    }

    private void notifyAdapterDataSetChanged() {
        if (getAdapter() == null) {
            return;
        }
        getAdapter().notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onInterceptTouchEvent(ev);
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (smoothScroll) {
            smoothScrollToPosition(item);
        } else {
            scrollToPosition(item);
        }
    }
}