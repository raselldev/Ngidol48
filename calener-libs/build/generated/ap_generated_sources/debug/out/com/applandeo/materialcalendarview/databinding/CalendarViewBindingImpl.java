package com.applandeo.materialcalendarview.databinding;
import com.applandeo.materialcalendarview.R;
import com.applandeo.materialcalendarview.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class CalendarViewBindingImpl extends CalendarViewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.calendarHeader, 1);
        sViewsWithIds.put(R.id.previousButton, 2);
        sViewsWithIds.put(R.id.forwardButton, 3);
        sViewsWithIds.put(R.id.currentDateLabel, 4);
        sViewsWithIds.put(R.id.abbreviationsBar, 5);
        sViewsWithIds.put(R.id.mondayLabel, 6);
        sViewsWithIds.put(R.id.tuesdayLabel, 7);
        sViewsWithIds.put(R.id.wednesdayLabel, 8);
        sViewsWithIds.put(R.id.thursdayLabel, 9);
        sViewsWithIds.put(R.id.fridayLabel, 10);
        sViewsWithIds.put(R.id.saturdayLabel, 11);
        sViewsWithIds.put(R.id.sundayLabel, 12);
        sViewsWithIds.put(R.id.calendarViewPager, 13);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public CalendarViewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private CalendarViewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[5]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (com.applandeo.materialcalendarview.extensions.CalendarViewPager) bindings[13]
            , (android.widget.TextView) bindings[4]
            , (android.widget.ImageButton) bindings[3]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageButton) bindings[2]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[8]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}