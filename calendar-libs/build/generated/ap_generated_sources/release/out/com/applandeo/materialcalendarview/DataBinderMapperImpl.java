package com.applandeo.materialcalendarview;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.applandeo.materialcalendarview.databinding.CalendarViewBindingImpl;
import com.applandeo.materialcalendarview.databinding.CalendarViewDayBindingImpl;
import com.applandeo.materialcalendarview.databinding.CalendarViewGridBindingImpl;
import com.applandeo.materialcalendarview.databinding.CalendarViewPickerDayBindingImpl;
import com.applandeo.materialcalendarview.databinding.DatePickerDialogBindingImpl;
import com.applandeo.materialcalendarview.databinding.ItemEventDateBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_CALENDARVIEW = 1;

  private static final int LAYOUT_CALENDARVIEWDAY = 2;

  private static final int LAYOUT_CALENDARVIEWGRID = 3;

  private static final int LAYOUT_CALENDARVIEWPICKERDAY = 4;

  private static final int LAYOUT_DATEPICKERDIALOG = 5;

  private static final int LAYOUT_ITEMEVENTDATE = 6;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(6);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.calendar_view, LAYOUT_CALENDARVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.calendar_view_day, LAYOUT_CALENDARVIEWDAY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.calendar_view_grid, LAYOUT_CALENDARVIEWGRID);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.calendar_view_picker_day, LAYOUT_CALENDARVIEWPICKERDAY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.date_picker_dialog, LAYOUT_DATEPICKERDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.applandeo.materialcalendarview.R.layout.item_event_date, LAYOUT_ITEMEVENTDATE);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_CALENDARVIEW: {
          if ("layout/calendar_view_0".equals(tag)) {
            return new CalendarViewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for calendar_view is invalid. Received: " + tag);
        }
        case  LAYOUT_CALENDARVIEWDAY: {
          if ("layout/calendar_view_day_0".equals(tag)) {
            return new CalendarViewDayBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for calendar_view_day is invalid. Received: " + tag);
        }
        case  LAYOUT_CALENDARVIEWGRID: {
          if ("layout/calendar_view_grid_0".equals(tag)) {
            return new CalendarViewGridBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for calendar_view_grid is invalid. Received: " + tag);
        }
        case  LAYOUT_CALENDARVIEWPICKERDAY: {
          if ("layout/calendar_view_picker_day_0".equals(tag)) {
            return new CalendarViewPickerDayBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for calendar_view_picker_day is invalid. Received: " + tag);
        }
        case  LAYOUT_DATEPICKERDIALOG: {
          if ("layout/date_picker_dialog_0".equals(tag)) {
            return new DatePickerDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for date_picker_dialog is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMEVENTDATE: {
          if ("layout/item_event_date_0".equals(tag)) {
            return new ItemEventDateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_event_date is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(6);

    static {
      sKeys.put("layout/calendar_view_0", com.applandeo.materialcalendarview.R.layout.calendar_view);
      sKeys.put("layout/calendar_view_day_0", com.applandeo.materialcalendarview.R.layout.calendar_view_day);
      sKeys.put("layout/calendar_view_grid_0", com.applandeo.materialcalendarview.R.layout.calendar_view_grid);
      sKeys.put("layout/calendar_view_picker_day_0", com.applandeo.materialcalendarview.R.layout.calendar_view_picker_day);
      sKeys.put("layout/date_picker_dialog_0", com.applandeo.materialcalendarview.R.layout.date_picker_dialog);
      sKeys.put("layout/item_event_date_0", com.applandeo.materialcalendarview.R.layout.item_event_date);
    }
  }
}
