//package androidx.recyclerview.widget;
//
///**
// * @ClassName LooperLayoutManager
// * @Description TODO
// * @Author Mozhimen & Kolin Zhao
// * @Date 2024/4/30
// * @Version 1.0
// */
//import android.view.View;
//import android.view.ViewGroup;
//import com.mozhimen.xmlk.recyclerk.R;
//
//public class LooperLayoutManager extends RecyclerView.LayoutManager {
//    private static final String TAG = "LooperLayoutManager";
//    private boolean looperEnable = true;
//    private RecyclerView recyclerView;
//    private int pixNumberPer10ms = 3;//每10毫秒移动的像素值
//    private boolean autoScroll = true;
//    private PagerSnapHelper snapHelper;
//
//    public LooperLayoutManager(RecyclerView recyclerView) {
//        this(recyclerView, true, true, 8000);
//    }
//
//    public LooperLayoutManager(RecyclerView recyclerView, boolean looperEnable, boolean autoScroll, int scrollOneScreenUseTimeInMilliSeconds) {
//        this.looperEnable = looperEnable;
//        this.autoScroll = autoScroll;
//        this.recyclerView = recyclerView;
//        clearLooperInfo(recyclerView);
//        LooperInfo looperInfo=new LooperInfo();
//        looperInfo.onScrollListener=onScrollListener;
//        recyclerView.addOnScrollListener(onScrollListener);
//        looperInfo.onAttachStateChangeListener=attachStateChangeListener;
//        this.recyclerView.addOnAttachStateChangeListener(attachStateChangeListener);
//        int widthPixels = recyclerView.getResources().getDisplayMetrics().widthPixels;
//        //毫秒
//        pixNumberPer10ms = (int) (widthPixels * 1.0f / scrollOneScreenUseTimeInMilliSeconds * 10);
//        pixNumberPer10ms = Math.max(1, pixNumberPer10ms);//最低一个像素
//        if (!autoScroll) {
//            //添加snap实现自动停在item中间的功能
//            recyclerView.setOnFlingListener(null);
//            snapHelper = new PagerSnapHelper();
//            snapHelper.attachToRecyclerView(recyclerView);
//            looperInfo.pagerSnapHelper=snapHelper;
//        }
//        looperInfo.runnable=cycleRollRunnable;
//        bindLooperInfo(recyclerView,looperInfo);
//    }
//
//    private void clearLooperInfo(RecyclerView recyclerView) {
//        Object tag = recyclerView.getTag(R.id.looper_info);
//        if(tag instanceof LooperInfo){
//            LooperInfo looperInfo= (LooperInfo) tag;
//            recyclerView.removeOnScrollListener(looperInfo.onScrollListener);
//            recyclerView.removeOnAttachStateChangeListener(looperInfo.onAttachStateChangeListener);
//            if(looperInfo.pagerSnapHelper!=null) {
//                looperInfo.pagerSnapHelper.attachToRecyclerView(null);
//            }
//            recyclerView.removeCallbacks(looperInfo.runnable);
//            recyclerView.setTag(R.id.looper_info,null);
//        }
//
//    }
//
//    private void bindLooperInfo(RecyclerView recyclerView,LooperInfo looperInfo){
//        recyclerView.setTag(R.id.looper_info,looperInfo);
//    }
//
//    private static class LooperInfo{
//        PagerSnapHelper pagerSnapHelper;
//        RecyclerView.OnScrollListener onScrollListener;
//        View.OnAttachStateChangeListener onAttachStateChangeListener;
//        Runnable runnable;
//    }
//
//    //当recycleView出现在屏幕上的时候自动滚动，脱离屏幕取消消息，避免内存泄漏
//    private View.OnAttachStateChangeListener attachStateChangeListener = new View.OnAttachStateChangeListener() {
//        @Override
//        public void onViewAttachedToWindow(View v) {
//            recyclerView.removeCallbacks(cycleRollRunnable);
//            recyclerView.setTag(R.id.tv_title,cycleRollRunnable);
//            recyclerView.postDelayed(cycleRollRunnable, 1000);
//
//        }
//
//        @Override
//        public void onViewDetachedFromWindow(View v) {
//            recyclerView.removeCallbacks(cycleRollRunnable);
//        }
//    };
//
//
//    @Override
//    public boolean isAutoMeasureEnabled() {
//        return true;
//    }
//
//    @Override
//    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
//        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//    }
//
//    @Override
//    public boolean canScrollHorizontally() {
//        return true;
//    }
//
//    @Override
//    public boolean canScrollVertically() {
//        return false;
//    }
//
//    Runnable cycleRollRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (autoScroll) {
//                recyclerView.scrollBy(pixNumberPer10ms, 0);
//                Object tag = recyclerView.getTag(R.id.tv_title);
//                if(tag==this) {
//                    recyclerView.postDelayed(this, 10);
//                }
//            }
//        }
//    };
//
//
//
//    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//        private boolean dragging;
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                //Dragging
//                recyclerView.removeCallbacks(cycleRollRunnable);
//                dragging = true;
//            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                if (dragging) {
//                    dragging = false;
//                    recyclerView.removeCallbacks(cycleRollRunnable);
//                    recyclerView.setTag(R.id.tv_title,cycleRollRunnable);
//                    recyclerView.postDelayed(cycleRollRunnable, 1000);
//                }
//            }
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//        }
//    };
//
//
//    @Override
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        if (getItemCount() <= 0) {
//            return;
//        }
//        //preLayout主要支持动画，直接跳过
//        if (state.isPreLayout()) {
//            return;
//        }
//        //将视图分离放入scrap缓存中，以准备重新对view进行排版
//        detachAndScrapAttachedViews(recycler);
//
//        int autualWidth = 0;
//
//        for (int i = 0; i < getItemCount(); i++) {
//            //初始化，将在屏幕内的view填充
//            View itemView = recycler.getViewForPosition(i);
//            addView(itemView);
//            //测量itemView的宽高
//            measureChildWithMargins(itemView, 0, 0);
//            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
//            int width = getDecoratedMeasuredWidth(itemView) ;
//            int height = getDecoratedMeasuredHeight(itemView);
//            int left=autualWidth;
//            int right=autualWidth+width+lp.rightMargin+lp.leftMargin;
//            int top=0;
//            int bottom=height+lp.topMargin+lp.bottomMargin;
//
//            layoutDecoratedWithMargins(itemView, left, top, right, bottom);
//
//            autualWidth = right;
//            //如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
//           /* if (autualWidth > getWidth()) {
//                break;
//            }*/
//        }
//    }
//
//    @Override
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        //1.左右滑动的时候，填充子view
//        int travl = fill(dx, recycler, state);
//        if (travl == 0) {
//            return 0;
//        }
//
//        //2.滚动
//        offsetChildrenHorizontal(travl * -1);
//
//        //3.回收已经离开界面的
//        recyclerHideView(dx, recycler, state);
//        return travl;
//    }
//
//
//
//    /**
//     * 左右滑动的时候，填充
//     */
//    private int fill(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        if (dx > 0) {
//            //标注1.向左滚动
//            View lastView = getChildAt(getChildCount() - 1);
//            if (lastView == null) {
//                return 0;
//            }
//            int lastPos = getPosition(lastView);
//            //标注2.可见的最后一个itemView完全滑进来了，需要补充新的
//            if (lastView.getRight() < getWidth()) {
//                View scrap = null;
//                //标注3.判断可见的最后一个itemView的索引，
//                // 如果是最后一个，则将下一个itemView设置为第一个，否则设置为当前索引的下一个
//                if (lastPos == getItemCount() - 1) {
//                    if (looperEnable) {
//                        scrap = recycler.getViewForPosition(0);
//                    } else {
//                        dx = 0;
//                    }
//                } else {
//                    scrap = recycler.getViewForPosition(lastPos + 1);
//                }
//                if (scrap == null) {
//                    return dx;
//                }
//                //标注4.将新的itemViewadd进来并对其测量和布局
//                addView(scrap);
//                measureChildWithMargins(scrap, 0, 0);
//                RecyclerView.LayoutParams addViewLp = (RecyclerView.LayoutParams) scrap.getLayoutParams();
//                RecyclerView.LayoutParams lastViewLp = (RecyclerView.LayoutParams) lastView.getLayoutParams();
//                int width = getDecoratedMeasuredWidth(scrap);
//                int height = getDecoratedMeasuredHeight(scrap);
//                int lastViewDecoratedRight = getRightDecorationWidth(lastView);
//                int left=lastView.getRight()+lastViewDecoratedRight+lastViewLp.rightMargin;
//                int right=left+width+addViewLp.rightMargin+addViewLp.leftMargin;
//                int top=0;
//                int bottom=height+addViewLp.bottomMargin+addViewLp.topMargin;
//                layoutDecoratedWithMargins(scrap,left,top,right,bottom);
//                return dx;
//            }
//        } else {
//            //向右滚动
//            View firstView = getChildAt(0);
//            if (firstView == null) {
//                return 0;
//            }
//            int firstPos = getPosition(firstView);
//
//            if (firstView.getLeft() >= 0) {
//                View scrap = null;
//                if (firstPos == 0) {
//                    if (looperEnable) {
//                        scrap = recycler.getViewForPosition(getItemCount() - 1);
//                    } else {
//                        dx = 0;
//                    }
//                } else {
//                    scrap = recycler.getViewForPosition(firstPos - 1);
//                }
//                if (scrap == null) {
//                    return 0;
//                }
//                addView(scrap, 0);
//                measureChildWithMargins(scrap, 0, 0);
//                RecyclerView.LayoutParams addViewLp = (RecyclerView.LayoutParams) scrap.getLayoutParams();
//                RecyclerView.LayoutParams firstViewLp = (RecyclerView.LayoutParams) firstView.getLayoutParams();
//                int width = getDecoratedMeasuredWidth(scrap);
//                int height = getDecoratedMeasuredHeight(scrap);
//                int decoratedLeft = getLeftDecorationWidth(firstView);
//                int left=firstView.getLeft()-decoratedLeft-firstViewLp.leftMargin-addViewLp.rightMargin-addViewLp.leftMargin-width;
//                int right=firstView.getLeft()-decoratedLeft-firstViewLp.leftMargin;
//                int top=0;
//                int bottom=height+addViewLp.bottomMargin+addViewLp.topMargin;
//                layoutDecoratedWithMargins(scrap,left,top,right,bottom);
//
//            }
//        }
//        return dx;
//    }
//
//    /**
//     * 回收界面不可见的view
//     */
//    private void recyclerHideView(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        for (int i = 0; i < getChildCount(); i++) {
//            View view = getChildAt(i);
//            if (view == null) {
//                continue;
//            }
//            if (dx > 0) {
//                //向左滚动，移除一个左边不在内容里的view
//                if (view.getRight() < 0) {
//                    removeAndRecycleView(view, recycler);
////                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
//                }
//            } else {
//                //向右滚动，移除一个右边不在内容里的view
//                if (view.getLeft() > getWidth()) {
//                    removeAndRecycleView(view, recycler);
////                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
//                }
//            }
//        }
//
//    }
//}
//
//
