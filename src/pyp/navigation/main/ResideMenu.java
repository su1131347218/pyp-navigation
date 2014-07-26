package pyp.navigation.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * @Title: ResideMenu
 * @Description: 菜单栏实现类
 * @author qsuron
 * @date 2014-7-26
 * @email admin@qiushurong.cn
 */
public class ResideMenu extends FrameLayout{

    public  static final int DIRECTION_LEFT  			= 0;	/* 左 */
    public  static final int DIRECTION_RIGHT 			= 1;	/* 右 */
    private static final int PRESSED_MOVE_HORIZANTAL 	= 2;	/* 按下 - 水平移动 */
    private static final int PRESSED_DOWN 				= 3;	/* 按下 */
    private static final int PRESSED_DONE 				= 4;	/* 按下完成 */
    private static final int PRESSED_MOVE_VERTICAL 		= 5;	/* 按下 - 垂直移动 */

    private static final float ScaleMoveOnY 	= 0.5f;		/* 菜单显示动画移动的垂直距离 */
    private static final float ScaleMoveOnX1 	= 1.4f;		/* 菜单显示动画移动的水平距离-右 */
    private static final float ScaleMoveOnX2 	= -0.4f;	/* 菜单显示动画移动的水平距离-左 */
    private static final int ScaleOpenTime 		= 250;		/* 菜单显示-主页面-动画时间 */
    private static final int ScaleCloseTime 	= 250;		/* 菜单关闭-主页面-动画时间 */
    private static final int ScaleMenuOpenTime 	= 250;		/* 菜单显示and关闭-菜单栏-动画时间 */
    private static final int ScaleMenuCloseTime = 250;		/* 菜单显示and关闭-菜单栏-动画时间 */
    
    private FragmentActivity activity;
    private ImageView imageViewShadow;
    private ImageView imageViewBackground;
    private LinearLayout layoutLeftMenu;
    private LinearLayout layoutRightMenu;
    private ScrollView scrollViewMenu;
    private ScrollView scrollViewLeftMenu;
    private ScrollView scrollViewRightMenu;
    private ViewGroup viewDecor; 									/* 装饰视图	*/
    private TouchDisableView viewActivity;							/* 视图容器 */
    private float lastRawX;
    private boolean isOpened;										/* 菜单栏状态(是/否打开) */
    private float shadowAdjustScaleX;								/* 阴影 - X */
    private float shadowAdjustScaleY;								/* 阴影 - Y */
    private List<View> ignoredViews;								/* 视图 - 不拦截手势 */
    private List<ResideMenuItem> leftMenuItems;						/* 菜单栏 - 左 */
    private List<ResideMenuItem> rightMenuItems;					/* 菜单栏 - 右 */
    private DisplayMetrics displayMetrics = new DisplayMetrics();	/* 用于Android获取屏幕分辨率 */
    private OnMenuListener menuListener;							/* 菜单监听器 */
    private boolean isInIgnoredView = false;						/* 视图是否不拦截手势 */
    private int scaleDirection = DIRECTION_LEFT;
    private int pressedState   = PRESSED_DOWN;
    /* 无视手势操作的视图 */
    private List<Integer> disabledSwipeDirection = new ArrayList<Integer>();
    private float mScaleValue = 0.5f;								/* 透明度 */
    private float lastActionDownX, lastActionDownY;
    
    public ResideMenu(Context context) {
        super(context);
        initViews(context);
    }

    /**
     * 初始化视图
     * @param context
     */
    private void initViews(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_residemenu_main, this);
        scrollViewLeftMenu = (ScrollView) findViewById(R.id.main_sv_left_menu);
        scrollViewRightMenu = (ScrollView) findViewById(R.id.main_sv_right_menu);
        imageViewShadow = (ImageView) findViewById(R.id.main_iv_shadow);
        layoutLeftMenu = (LinearLayout) findViewById(R.id.main_layout_left_menu);
        layoutRightMenu = (LinearLayout) findViewById(R.id.main_layout_right_menu);
        imageViewBackground = (ImageView) findViewById(R.id.main_iv_background);
    }

    /**
     * 显示菜单栏操作对应的avtivity
     * @param activity
     */
    public void attachToActivity(FragmentActivity activity){
        initValue(activity);
        setShadowAdjustScaleXByOrientation();
        viewDecor.addView(this, 0);
        setViewPadding();
    }

    /**
     * 初始化数据
     * @param activity
     */
    private void initValue(FragmentActivity activity){
        this.activity   = activity;
        leftMenuItems   = new ArrayList<ResideMenuItem>();
        rightMenuItems  = new ArrayList<ResideMenuItem>();
        if(ignoredViews == null)
        	ignoredViews    = new ArrayList<View>();
        viewDecor = (ViewGroup) activity.getWindow().getDecorView();
        viewActivity = new TouchDisableView(this.activity);
        View mContent   = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);
        viewActivity.setContent(mContent);
        addView(viewActivity);
    }

    
    /**
     * 设置阴影面积
     */
    private void setShadowAdjustScaleXByOrientation(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            shadowAdjustScaleX = 0.034f;
            shadowAdjustScaleY = 0.12f;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            shadowAdjustScaleX = 0.06f;
            shadowAdjustScaleY = 0.07f;
        }
    }

    /**
     * 设置菜单栏背景
     * @param imageResrouce
     */
    public void setBackground(int imageResrouce){
        imageViewBackground.setImageResource(imageResrouce);
    }

    /**
     * 设置活动视图中阴影是否可见
     * @param isVisible
     */
    public void setShadowVisible(boolean isVisible){
        if (isVisible)
            imageViewShadow.setImageResource(R.drawable.main_shadow);
        else
            imageViewShadow.setImageBitmap(null);
    }

    /**
     * 添加一个条目到左菜单
     * @param menuItem
     */
    @Deprecated
    public void addMenuItem(ResideMenuItem menuItem){
        this.leftMenuItems.add(menuItem);
        layoutLeftMenu.addView(menuItem);
    }

    /**
     * 添加一个条目
     * @param menuItem
     * @param direction
     * 左/右
     */
    public void addMenuItem(ResideMenuItem menuItem, int direction){
        if (direction == DIRECTION_LEFT){
            this.leftMenuItems.add(menuItem);
            layoutLeftMenu.addView(menuItem);
        }else{
            this.rightMenuItems.add(menuItem);
            layoutRightMenu.addView(menuItem);
        }
    }

    /**
     * 遍历数组列表，设置左边菜单栏
     * @param menuItems
     */
    @Deprecated
    public void setMenuItems(List<ResideMenuItem> menuItems){
        this.leftMenuItems = menuItems;
        rebuildMenu();
    }

    /**
     * 遍历数组列表，设置菜单栏
     * @param menuItems
     * @param direction
     * 左/右
     */
    public void setMenuItems(List<ResideMenuItem> menuItems, int direction){
        if (direction == DIRECTION_LEFT)
            this.leftMenuItems = menuItems;
        else
            this.rightMenuItems = menuItems;
        rebuildMenu();
    }

    
    /**
     * 遍历数组，重建菜单栏
     */
    private void rebuildMenu(){
        layoutLeftMenu.removeAllViews();
        layoutRightMenu.removeAllViews();
        for(int i = 0; i < leftMenuItems.size() ; i ++)
            layoutLeftMenu.addView(leftMenuItems.get(i), i);
        for(int i = 0; i < rightMenuItems.size() ; i ++)
            layoutRightMenu.addView(rightMenuItems.get(i), i);
    }

    /**
     * 获取左菜单栏
     * @return List<ResideMenuItem>
     */
    @Deprecated
    public List<ResideMenuItem> getMenuItems() {
        return leftMenuItems;
    }

    /**
     * 获取菜单栏
     * @param direction
     * 左/右
     * @return List<ResideMenuItem>
     */
    public List<ResideMenuItem> getMenuItems(int direction) {
        if ( direction == DIRECTION_LEFT)
            return leftMenuItems;
        else
            return rightMenuItems;
    }

    /**
     * 设置打开/关闭菜单栏的监听器
     * @param menuListener
     */
    public void setMenuListener(OnMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * 获取 打开/关闭菜单栏的监听器
     * @return OnMenuListener
     */
    public OnMenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * 需要在菜单显示前调用该方法，因为目前的onCreateView();不能得到activity的填充
     */
    private void setViewPadding(){
        this.setPadding(viewActivity.getPaddingLeft(),
                viewActivity.getPaddingTop(),
                viewActivity.getPaddingRight(),
                viewActivity.getPaddingBottom());
    }

    /**
     * 显示驻留菜单-动画组合
     */
    public void openMenu(int direction){

        setScaleDirection(direction);

        isOpened = true;
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValue);
        AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
        		mScaleValue + shadowAdjustScaleX, mScaleValue + shadowAdjustScaleY);
        AnimatorSet alpha_menu = buildMenuOpenAnimation(scrollViewMenu, 1.0f);
        scaleDown_shadow.addListener(animationListener);
        scaleDown_activity.playTogether(scaleDown_shadow);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    /**
     * 关闭驻留菜单-动画组合
     */
    public void closeMenu(){
        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, 1.0f, 1.0f);
        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuCloseAnimation(scrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(scaleUp_shadow);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    @Deprecated
    public void setDirectionDisable(int direction){
        disabledSwipeDirection.add(direction);
    }

    /**
     * 设置视图无视手势操作
     * @param direction
     * 视图ID
     */
    public void setSwipeDirectionDisable(int direction){
        disabledSwipeDirection.add(direction);
    }

    /**
     * 返回视图是否无视手势操作
     * @param direction
     * 视图ID
     * @return boolean
     * 返回 true-无视手势操作 / false-不无视手势操作
     */
    private boolean isInDisableDirection(int direction){
        return disabledSwipeDirection.contains(direction);
    }

    /**
     * 设置动画 
     * @param direction
     */
    private void setScaleDirection(int direction){
        int screenWidth = getScreenWidth();
        float pivotX;
        
		float pivotY = getScreenHeight() * ScaleMoveOnY;
        if (direction == DIRECTION_LEFT){
            scrollViewMenu = scrollViewLeftMenu;
			pivotX  = screenWidth * ScaleMoveOnX1;
        }else{
            scrollViewMenu = scrollViewRightMenu;
            pivotX  = screenWidth * ScaleMoveOnX2;
        }
        ViewHelper.setPivotX(viewActivity, pivotX);
        ViewHelper.setPivotY(viewActivity, pivotY);
        ViewHelper.setPivotX(imageViewShadow, pivotX);
        ViewHelper.setPivotY(imageViewShadow, pivotY);
        scaleDirection = direction;
    }

    /**
     * 返回菜单状态
     * @return
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * OnClickListener ： viewActivityOnClickListener
     * TODO
     */
    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeMenu();
        }
    };

    /**
     * 过场动画
     */
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()){
                scrollViewMenu.setVisibility(VISIBLE);
                if (menuListener != null)
                    menuListener.openMenu();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(isOpened()){
                viewActivity.setTouchDisable(true);
                viewActivity.setOnClickListener(viewActivityOnClickListener);
            }else{
                viewActivity.setTouchDisable(false);
                viewActivity.setOnClickListener(null);
                scrollViewMenu.setVisibility(GONE);
                if (menuListener != null)
                    menuListener.closeMenu();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * 用于构建ScaleDown动画
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleDownAnimation(View target,float targetScaleX,float targetScaleY){

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
                android.R.anim.decelerate_interpolator));
        scaleDown.setDuration(ScaleOpenTime);
        return scaleDown;
    }

    /**
     * 用于构建ScaleUp动画
     * @param target
     * @param targetScaleX
     * @param targetScaleY
     * @return
     */
    private AnimatorSet buildScaleUpAnimation(View target,float targetScaleX,float targetScaleY){

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(ScaleCloseTime);
        return scaleUp;
    }

    /**
     * 用于构建[菜单栏]-[显示]动画
     * @param target
     * @param alpha
     * @return
     */
    private AnimatorSet buildMenuOpenAnimation(View target, float alpha){

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );
        alphaAnimation.setDuration(ScaleMenuOpenTime);
        return alphaAnimation;
    }
    
    /**
     * 用于构建[菜单栏]-[关闭]动画
     * @param target
     * @param alpha
     * @return
     */
    private AnimatorSet buildMenuCloseAnimation(View target, float alpha){

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha)
        );
        alphaAnimation.setDuration(ScaleMenuCloseTime);
        return alphaAnimation;
    }

    /**
     * 设置视图View无视手势菜单栏操作
     * @param v
     */
    public void addIgnoredView(View v){
        ignoredViews.add(v);
    }

    /**
     * 取消视图View无视手势菜单栏操作
     * @param v
     */
    public void removeIgnoredView(View v){
        ignoredViews.remove(v);
    }

    /**
     * 清空所有视图View无视手势菜单栏操作
     */
    public void clearIgnoredViewList(){
    	
        ignoredViews.clear();
    }

    /**
     * 返回当前操作是否是无视手势菜单栏操作(就是在那个范围内拉不动)
     * @param ev
     * @return
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : ignoredViews) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())){
            	//Log.i("qsuron","isInIgnoredView - True");
                return true;
            }
        }
        //Log.i("qsuron","isInIgnoredView - False");
        return false;
    }

    /**
     * 设置动画
     * @param currentRawX
     */
    private void setScaleDirectionByRawX(float currentRawX){
        if (currentRawX < lastRawX)
            setScaleDirection(DIRECTION_RIGHT);
        else
            setScaleDirection(DIRECTION_LEFT);
    }

    /**
     * 获取动画设置值
     * @param currentRawX
     * @return float
     */
    private float getTargetScale(float currentRawX){
        float scaleFloatX = ((currentRawX - lastRawX) / getScreenWidth()) * 0.75f;
        scaleFloatX = scaleDirection == DIRECTION_RIGHT ? - scaleFloatX : scaleFloatX;

        float targetScale = ViewHelper.getScaleX(viewActivity) - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    

    /* (non-Javadoc)
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = ViewHelper.getScaleX(viewActivity);
        if (currentActivityScaleX == 1.0f)
            setScaleDirectionByRawX(ev.getRawX());

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                isInIgnoredView = isInIgnoredView(ev) && !isOpened();
                pressedState    = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isInIgnoredView || isInDisableDirection(scaleDirection))
                    break;

                if(pressedState != PRESSED_DOWN &&
                        pressedState != PRESSED_MOVE_HORIZANTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if(pressedState == PRESSED_DOWN) {
                    if(yOffset > 25 || yOffset < -25) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if(xOffset < -50 || xOffset > 50) {
                        pressedState = PRESSED_MOVE_HORIZANTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if(pressedState == PRESSED_MOVE_HORIZANTAL) {
                    if (currentActivityScaleX < 0.95)
                        scrollViewMenu.setVisibility(VISIBLE);

                    float targetScale = getTargetScale(ev.getRawX());
                    ViewHelper.setScaleX(viewActivity, targetScale);
                    ViewHelper.setScaleY(viewActivity, targetScale);
                    ViewHelper.setScaleX(imageViewShadow, targetScale + shadowAdjustScaleX);
                    ViewHelper.setScaleY(imageViewShadow, targetScale + shadowAdjustScaleY);
                    ViewHelper.setAlpha(scrollViewMenu, (1 - targetScale) * 2.0f);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isInIgnoredView) break;
                if (pressedState != PRESSED_MOVE_HORIZANTAL) break;

                pressedState = PRESSED_DONE;
                if (isOpened()){
                    if (currentActivityScaleX > 0.56f)
                        closeMenu();
                    else
                        openMenu(scaleDirection);
                }else{
                    if (currentActivityScaleX < 0.94f){
                        openMenu(scaleDirection);
                    }else{
                        closeMenu();
                    }
                }

                break;

        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取屏幕高度 - 用于自适应屏幕
     * @return int
     */
    public int getScreenHeight(){
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度 - 用于自适应屏幕
     * @return int
     */
    public int getScreenWidth(){
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    
    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnMenuListener{

        /**
         * 该方法会在菜单栏打开动画完成后调用
         */
        public void openMenu();

        /**
         * 该方法会在菜单栏关闭动画完成后调用
         */
        public void closeMenu();
    }

}