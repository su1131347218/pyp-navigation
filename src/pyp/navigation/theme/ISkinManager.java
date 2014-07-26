package pyp.navigation.theme;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @Title: ISkinManager
 * @Description: 接口(用来触发改变界面皮肤的)
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public interface ISkinManager extends OnClickListener {
	/**
	 * 把需要换肤的ISkin关联到ISkinManager上
	 * @param iSkin
	 * 需要换肤的类
	 */
	public void setSkin(ISkin iSkin);
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v);
}
